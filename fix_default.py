import os
import re

def fix(path):
    with open(path, 'r') as f:
        content = f.read()
    
    if '.Default' in content:
        content = content.replace('.Default\n', '')
        with open(path, 'w') as f:
            f.write(content)
        print(f"Fixed {path}")

for root, _, files in os.walk('src/main/java'):
    for file in files:
        if file.endswith('.java'):
            fix(os.path.join(root, file))
print("Done")
