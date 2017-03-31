package com.application.threads;

import java.io.File;
import java.util.Random;

public class ThreadExampleThree {
    public static void main(String[] args) {
        ObjWrapper.setLogFileName(new File("ObjectWrapperCallTrace.txt"));

        new ThreadExampleTwo().startAll("Thread A", "Thread B", "Thread C");
        new ThreadExampleTwo().startAll("Thread 1", "Thread 2", "Thread 3");
    }
}