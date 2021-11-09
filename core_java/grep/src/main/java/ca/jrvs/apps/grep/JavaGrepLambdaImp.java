package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JavaGrepLambdaImp extends JavaGrepImp {

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try {
            javaGrepLambdaImp.process();
        } catch (Exception ex) {
            javaGrepLambdaImp.logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> file_lines = new ArrayList<>();
        try {
            file_lines = Files
                    .lines(Paths.get(inputFile.getPath()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("File cannot be read", e);
        }
        return file_lines;
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> recursive_files = new ArrayList<>();
        try {
            recursive_files = Files
                    .walk(Paths.get(rootDir))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("File not accessible", e);
        }
        return recursive_files;
    }
}