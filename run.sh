#!/bin/bash

# Course Registration System Launcher Script

echo "======================================================="
echo "Course Registration System Launcher by David Porathur"
echo "======================================================="

echo "Java version:"
java -version
echo ""

# Compile the application with PostgreSQL driver
echo "Compiling Java files..."
if javac -cp ".:postgresql-42.6.0.jar" *.java; then
    echo "Compilation successful!"
    echo ""
    
    echo "Starting Course Registration System..."
    echo "Note: Database features require PostgreSQL to be running."
    echo "Use ./run-with-db.sh for full database functionality."
    echo "Close the application window to exit."
    echo ""
    
    # Run the application with PostgreSQL driver in classpath
    java -cp ".:postgresql-42.6.0.jar" CourseRegistrationSystem
    
    echo ""
    echo "Application closed. Thank you!"
else
    echo "Compilation failed. Please check the Java files for errors."
    exit 1
fi