package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep {

    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String rootPath;
    private String outFile;

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        BasicConfigurator.configure();

        JavaGrepImp javaGrep = new JavaGrepImp();
        javaGrep.setRegex(args[0]);
        javaGrep.setRootPath(args[1]);
        javaGrep.setOutFile(args[2]);

        try {
            javaGrep.process();
        } catch (Exception ex) {
            javaGrep.logger.error(
                    String.format("Failure processing files with given arguments: %s %s %s"
                            , javaGrep.getRegex(), javaGrep.getRootPath(), javaGrep.getOutFile())
                    , ex);
        }
    }

    @Override
    public void process() throws IOException {
        List<String> matchedLines = new LinkedList<>();
        for (File file : listFiles(this.getRootPath())) {
            for (String line : readLines(file)) {
                if (containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }
        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) throws IOException {
        List<File> files = new LinkedList<>();
        Deque<File> remainingFiles = new LinkedList<>();
        remainingFiles.addFirst(new File(rootDir));
        do {
            File currentFile = remainingFiles.removeFirst();
            if (currentFile.isFile()) {
                files.add(currentFile);
            } else if (currentFile.isDirectory()) {
                remainingFiles.addAll(Arrays.asList(currentFile.listFiles()));
            } else {
                throw new IOException(String.format("File %s cannot be located", currentFile.getPath()));
            }
        } while (!remainingFiles.isEmpty());
        return files;
    }

    @Override
    public List<String> readLines(File inputFile) throws IllegalArgumentException {
        List<String> lines = new LinkedList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    String.format("File %s cannot be read", inputFile.getPath()), e);
        }
        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        return line.matches(String.format("%s", this.getRegex()));
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.getOutFile()))) {
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new IOException(String.format("File %s cannot be written to.", this.getOutFile()), e);
        }
    }

    @Override
    public String getRootPath() {
        return this.rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return this.regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return this.outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }
}