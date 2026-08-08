import os
import re

def strip_annotations(path):
    with open(path, 'r') as f:
        content = f.read()
    
    original = content
    content = re.sub(r'@Data\s*', '', content)
    content = re.sub(r'@NoArgsConstructor\s*', '', content)
    content = re.sub(r'@AllArgsConstructor\s*', '', content)
    content = re.sub(r'@Builder\s*', '', content)
    content = re.sub(r'@Getter\s*', '', content)
    content = re.sub(r'@Setter\s*', '', content)
    
    if content != original:
        with open(path, 'w') as f:
            f.write(content)
        print(f"Stripped from {path}")

for root, _, files in os.walk('src/main/java'):
    for file in files:
        if file.endswith('.java'):
            strip_annotations(os.path.join(root, file))
print("Done")
