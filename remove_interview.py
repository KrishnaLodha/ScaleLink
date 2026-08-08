import os
import re

# 1. Rename INTERVIEW_GUIDE.md to FAQ.md
if os.path.exists('INTERVIEW_GUIDE.md'):
    os.rename('INTERVIEW_GUIDE.md', 'FAQ.md')

# 2. Update README.md
with open('README.md', 'r') as f:
    readme = f.read()

readme = readme.replace(' — interview-ready system design project.', '')
readme = readme.replace('[INTERVIEW_GUIDE.md](INTERVIEW_GUIDE.md)', '[FAQ.md](FAQ.md)')
readme = readme.replace('Google SWE interview Q&A', 'System Design Q&A')
readme = readme.replace('## Interview Topics Covered', '## Topics Covered')

with open('README.md', 'w') as f:
    f.write(readme)

# 3. Update FAQ.md
with open('FAQ.md', 'r') as f:
    faq = f.read()

faq = faq.replace('ScaleLink Interview Guide', 'ScaleLink System FAQ')
faq = faq.replace('Google SWE Interview Discussion Questions', 'System Design Discussion Questions')
faq = faq.replace('## Additional Interview Topics', '## Additional Topics')

with open('FAQ.md', 'w') as f:
    f.write(faq)

print("Done")
