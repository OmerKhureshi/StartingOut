package com.application.threads;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class LogWriterUtil {
    File file = null;
    PrintWriter printWriter = null;

    public void initialize() {
        file = new File("ObjectWrapperCallTrace.txt");
        try {
            new PrintWriter(file).close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
