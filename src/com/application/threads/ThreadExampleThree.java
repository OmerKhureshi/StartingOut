package com.application.threads;

import java.util.Random;

public class ThreadExampleThree {
    public static void main(String[] args) {
        new ThreadExampleTwo().startAll("Thread A", "Thread B", "Thread C");
        new ThreadExampleTwo().startAll("Thread 1", "Thread 2", "Thread 3");
    }
}