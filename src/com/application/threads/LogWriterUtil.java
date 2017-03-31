package com.application.threads;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;

public class LogWriterUtil {
    static Path file;

    public static void main(String[] args) {
        initialize();
        write("Line 1");
        write("Line 2");
    }

    private static void initialize() {
        file = Paths.get("ObjectWrapperCallTrace.txt");
    }

    public static void write(String line) {
        if (file == null) {
            initialize();
//            throw new IllegalStateException("LogWriterUtil should be initialized first.");
        }

        try {
            Files.write(file, Collections.singletonList(line), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
