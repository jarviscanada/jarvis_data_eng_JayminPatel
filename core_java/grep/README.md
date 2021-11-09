Introduction

The Java Grep Application facilitates recursive text-searching based on regular expressions. Given a root directory, the application searches through all files and subdirectories for matches to a specific pattern. The application is deployed through a docker image to ensure consistent runtime environments as well as implements core Java file I/O Stream APIs and Lambda functions, managed with Apache Maven. The following include some of the key technologies used in this implementation:

Java 8
IntelliJ IDEA
Maven
Docker

Quick Start

1. Run the program with Jar file

# Clean build with Maven
mvn clean compile package

# Run application with jar file
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp [regex] [rootPath] [outFile]

2. Run the program with Docker

# Pull Docker image
docker pull jaymin/grep

# Run Docker container
docker run --rm -v `pwd`/data:/data -v `pwd`/out:/out jaymin/grep [regex] [rootPath] [outFile]

Implementaiton

Pseudocode

The following is pseudocode for the process method:

matchedLines = []
for file in listFilesRecursively(rootDir)
for line in readLines(file)
if containsPattern(line)
matchedLines.add(line)
writeToFile(matchedLines)

Performance Issue

The default implementation that is based on the Java Collections framework can have memory issues when working with large files and limited JVM heap size. This is due to the fact that Collections require the whole file to be loaded into the memory before being proccessed. The solution is to use the Stream interface of the BufferedReader class that allows lazy processing of lines in a memory-efficient way. This solution is implemented in the processStream() method of JavaGrepLambdaImp.java.

Test
Manual testing was done using sample data and IDE debugger. The content of the output files was examined and compared to the expected results.

Deployment
The application was deployed as a Docker image named jaymin/grep. The image is available in a public repository on Docker Hub. The following commands can be used to obtain and run the application:

# Pull Docker image
docker pull jaymin/grep

# Run Docker container
docker run --rm -v `pwd`/data:/data -v `pwd`/out:/out jaymin/grep [regex] [rootPath] [outFile]


Improvement
1.Display more detailed results, including file name and line number.
2.Further extend functionalities, such as ignoring case, selecting non-matched lines
3.More user feedback, displaying lines as they are matched