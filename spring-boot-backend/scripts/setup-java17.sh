#!/bin/bash

# Script to setup Java 17 for development

echo "=========================================="
echo "Setting up Java 17 for Spring Boot"
echo "=========================================="
echo ""

# Check if Java 17 is installed
JAVA_17_PATH=$(/usr/libexec/java_home -v 17 2>/dev/null)

if [ -z "$JAVA_17_PATH" ]; then
    echo "❌ Java 17 is not installed!"
    echo ""
    echo "Please install Java 17:"
    echo "  brew install openjdk@17"
    echo ""
    exit 1
fi

echo "✅ Java 17 found at: $JAVA_17_PATH"
echo ""

# Set JAVA_HOME
export JAVA_HOME=$JAVA_17_PATH
export PATH=$JAVA_HOME/bin:$PATH

echo "Current Java version:"
java -version

echo ""
echo "=========================================="
echo "Java 17 is now active!"
echo "=========================================="
echo ""
echo "To make this permanent, add to your ~/.zshrc or ~/.bash_profile:"
echo ""
echo "  export JAVA_HOME=\$(/usr/libexec/java_home -v 17)"
echo "  export PATH=\$JAVA_HOME/bin:\$PATH"
echo ""

