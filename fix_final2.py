import os

with open('src/main/java/com/scalink/util/UserAgentParser.java', 'r') as f:
    uap = f.read()
uap = uap.replace("record ParsedUserAgent(String browser, String device, String operatingSystem) {}", "")
with open('src/main/java/com/scalink/util/UserAgentParser.java', 'w') as f:
    f.write(uap)

with open('src/main/java/com/scalink/util/ParsedUserAgent.java', 'w') as f:
    f.write("package com.scalink.util;\n\npublic record ParsedUserAgent(String browser, String device, String operatingSystem) {}\n")

with open('src/main/java/com/scalink/exception/ErrorResponse.java', 'r') as f:
    er = f.read()
er = er.replace("public ErrorResponse() {}", "public java.util.Map<String, String> getValidationErrors() { return this.validationErrors; }\n    public void setValidationErrors(java.util.Map<String, String> validationErrors) { this.validationErrors = validationErrors; }\n\n    public ErrorResponse() {}")
with open('src/main/java/com/scalink/exception/ErrorResponse.java', 'w') as f:
    f.write(er)
print("Done")
