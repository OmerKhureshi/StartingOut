package com.application.logs.fileIntegrity;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class CheckFileIntegrity {
    static String line;
    static Deque<Integer> stack;
    static int linesRead = 0;

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
                    ++linesRead;
            }
            if (!(stack.isEmpty())) {
                IllegalStateException up = new IllegalStateException("Stack should have been empty, it is not. Error at line " + linesRead + 1);
                throw up;  // Yuck! Not having any of that :(
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Error occurred in line due to mismatch in count of enters and exits. Error at line: " + linesRead + "; Line is: " + line);
        } finally {
            System.out.println("File integrity check completed. If no exceptions were thrown, then file is good.");
        }

    }
}
