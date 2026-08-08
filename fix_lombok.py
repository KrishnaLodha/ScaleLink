import os
import re

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    if 'public class' not in content:
        return
        
    class_name_match = re.search(r'public class (\w+)', content)
    if not class_name_match:
        return
    class_name = class_name_match.group(1)
    
    # Very rudimentary field extraction
    # Matches: private String foo;
    fields = re.findall(r'private ([\w\.\<\>]+) (\w+)(?:\s*=\s*[^;]+)?;', content)
    
    additions = []
    
    if '@Getter' in content or '@Data' in content:
        for f_type, f_name in fields:
            cap_name = f_name[0].upper() + f_name[1:]
            additions.append(f"    public {f_type} get{cap_name}() {{ return this.{f_name}; }}")
            
    if '@Setter' in content or '@Data' in content:
        for f_type, f_name in fields:
            cap_name = f_name[0].upper() + f_name[1:]
            additions.append(f"    public void set{cap_name}({f_type} {f_name}) {{ this.{f_name} = {f_name}; }}")
            
    if '@NoArgsConstructor' in content or '@Data' in content:
        additions.append(f"    public {class_name}() {{}}")
        
    if '@AllArgsConstructor' in content or '@Builder' in content:
        args = ", ".join([f"{t} {n}" for t, n in fields])
        assigns = " ".join([f"this.{n} = {n};" for t, n in fields])
        additions.append(f"    public {class_name}({args}) {{ {assigns} }}")
        
    if '@Builder' in content:
        builder_name = f"{class_name}Builder"
        additions.append(f"    public static {builder_name} builder() {{ return new {builder_name}(); }}")
        additions.append(f"    public static class {builder_name} {{")
        for f_type, f_name in fields:
            additions.append(f"        private {f_type} {f_name};")
            additions.append(f"        public {builder_name} {f_name}({f_type} {f_name}) {{ this.{f_name} = {f_name}; return this; }}")
        args_pass = ", ".join([n for t, n in fields])
        additions.append(f"        public {class_name} build() {{ return new {class_name}({args_pass}); }}")
        additions.append("    }")
        
    if '@Slf4j' in content:
        additions.insert(0, f"    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger({class_name}.class);")
        
    if not additions:
        return
        
    # Replace the last brace with the additions
    last_brace_idx = content.rfind('}')
    if last_brace_idx != -1:
        new_content = content[:last_brace_idx] + "\n".join(additions) + "\n}\n"
        with open(filepath, 'w') as f:
            f.write(new_content)
        print(f"Patched {filepath}")

for root, _, files in os.walk('src/main/java'):
    for file in files:
        if file.endswith('.java'):
            process_file(os.path.join(root, file))

# Handle inner classes manually for DashboardSummaryResponse and AnalyticsSummaryResponse
# Not writing the regex for inner classes since it's hard, let's just append to the file directly

