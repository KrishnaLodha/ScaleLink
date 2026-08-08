# Deployment Guide

## Prerequisites

- AWS account with EC2, RDS, ElastiCache access
- Domain name pointed to EC2/ALB
- Docker installed on EC2
- GitHub repository with Actions enabled

## EC2 Setup

### 1. Launch Instance

| Setting | Value |
|---------|-------|
| AMI | Amazon Linux 2023 or Ubuntu 22.04 |
| Instance type | t3.medium (dev) / t3.large (prod) |
| Storage | 30GB gp3 |
| Security Group | See below |

### 2. Security Groups

**Application SG (scalink-app):**
| Direction | Port | Source | Purpose |
|-----------|------|--------|---------|
| Inbound | 80 | 0.0.0.0/0 | HTTP (Nginx) |
| Inbound | 443 | 0.0.0.0/0 | HTTPS |
| Inbound | 22 | Your IP | SSH |
| Outbound | All | 0.0.0.0/0 | External access |

**Database SG (scalink-db):**
| Direction | Port | Source | Purpose |
|-----------|------|--------|---------|
| Inbound | 5432 | scalink-app SG | PostgreSQL |

**Redis SG (scalink-redis):**
| Direction | Port | Source | Purpose |
|-----------|------|--------|---------|
| Inbound | 6379 | scalink-app SG | Redis |

### 3. Install Dependencies

```bash
sudo yum update -y
sudo yum install -y docker nginx
sudo systemctl enable docker nginx
sudo systemctl start docker
sudo usermod -aG docker ec2-user
```

### 4. Configure Environment

```bash
sudo mkdir -p /opt/scalink
sudo cp .env.example /opt/scalink/.env
# Edit /opt/scalink/.env with production values
```

### 5. Deploy Application

```bash
docker build -f Dockerfile.prod -t scalink:latest .
docker run -d \
  --name scalink-green \
  --env-file /opt/scalink/.env \
  -e SPRING_PROFILES_ACTIVE=production \
  -p 8081:8080 \
  --restart unless-stopped \
  scalink:latest
```

## Nginx Reverse Proxy

```nginx
# /etc/nginx/sites-available/scalink
upstream scalink {
    server 127.0.0.1:8081;
}

server {
    listen 80;
    server_name scalink.example.com;

    location / {
        proxy_pass http://scalink;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

```bash
sudo ln -s /etc/nginx/sites-available/scalink /etc/nginx/sites-enabled/
sudo nginx -t && sudo systemctl reload nginx
```

## SSL with Let's Encrypt

```bash
sudo yum install -y certbot python3-certbot-nginx
sudo certbot --nginx -d scalink.example.com
sudo certbot renew --dry-run
```

## Blue-Green Deployment

1. Deploy new version to alternate port (8081 → 8082)
2. Health check: `curl http://localhost:8082/actuator/health/liveness`
3. Update Nginx upstream to new port
4. Reload Nginx: `sudo nginx -s reload`
5. Stop old container

Automated via `.github/workflows/deploy.yml` on push to `main`.

## RDS PostgreSQL

```bash
# Create RDS instance
aws rds create-db-instance \
  --db-instance-identifier scalink-db \
  --db-instance-class db.t3.medium \
  --engine postgres \
  --engine-version 16 \
  --master-username scalink \
  --master-user-password <password> \
  --allocated-storage 50 \
  --vpc-security-group-ids sg-xxx \
  --multi-az
```

## ElastiCache Redis

```bash
aws elasticache create-cache-cluster \
  --cache-cluster-id scalink-redis \
  --engine redis \
  --cache-node-type cache.t3.micro \
  --num-cache-nodes 1 \
  --security-group-ids sg-xxx
```

## Health Checks

| Endpoint | Purpose |
|----------|---------|
| `/actuator/health/liveness` | Process is alive |
| `/actuator/health/readiness` | Ready to serve (DB + Redis connected) |
| `/actuator/health` | Full health details |

## Rollback

```bash
docker stop scalink-green
docker run -d --name scalink-blue \
  --env-file /opt/scalink/.env \
  -p 8081:8080 scalink:previous-tag
sudo nginx -s reload
```
