package com.application.logs.fileHandler;

import java.io.File;

public class CallTraceLogFile {
    // ToDO next version.
    // Change time stamp to milliseconds.
    // get logs from a real app
    // UI - thread info in left pane. show single thread on canvas
    // UI - label for each node for classname.methodname.

    // private static String fileName = "L-Instrumentation_call_trace_1_wait.txt";
    private static String fileName = "L-Instrumentation_call_trace_works_basic_complexity_4.txt";
    private static File file = new File(Thread.currentThread().getContextClassLoader().getResource(fileName).getFile());

    CallTraceLogFile() {
        // Get the resources
        // http://stackoverflow.com/a/21722773/3690248
        file = new File(Thread.currentThread().getContextClassLoader().getResource(fileName).getFile());
    }

    public static File getFile() {
        return file;
    }

    public static void setFile(File newFile) {
        file = newFile;
    }

    public static String getFileName() {
        return fileName;
    }

    public static void setFileName(String newFileName) {
        fileName = newFileName;
    }
}
