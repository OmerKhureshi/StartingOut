package com.application.logs.fileIntegrity;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class CheckFileIntegrity {
    static String line;
    static Deque<Integer> stack;

    public static void checkFile (File file) {
        stack = new LinkedList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while((line = br.readLine()) != null) {
                String msg = line.split("\\|")[3];
                    switch (msg.toUpperCase()) {
                        case "ENTER":
                            stack.push(1);
                            break;

                        case "EXIT":
                            stack.pop();
                            break;

                        default:
                            IllegalStateException up = new IllegalStateException("Error occurred in line: " + line);
                            throw up;  // Yuck! Not having any of that :(
                    }
            }
            if (!(stack.isEmpty())) {
                IllegalStateException up = new IllegalStateException("Stack should have been empty, it is not.");
                throw up;  // Yuck! Not having any of that :(
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Error occurred in line due to mismatch in count of enters and exits.: " + line);
        } finally {
            System.out.println("File integrity check completed. If no exceptions were thrown, then file is good.");
        }

    }
}
