package com.application.logs.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ParseCallTrace implements FileParser {
    private BufferedReader br;
    String line;

    @Override
    public void readFile(File logFile, Consumer<List<String>> cmd) {
        try {
            br = new BufferedReader(new FileReader(logFile));
            // ToDo Use streams to perform buffered read and insert.
            while ((line = br.readLine()) != null) {
                List<String> brokenLineList = parse(line);
                cmd.accept(brokenLineList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // TimeStamp       | ProcessID | ThreadID |  EventType |LockObjectID
    // utc time format | 40948     |    9     | Wait-Enter |3986916

    // TimeStamp                | ProcessID | ThreadID | EventType | MethodID  | Arguments
    // 2017-03-31T17:00:19.305Z | 40948     |    9     |   Enter   |     1     |    []

    public List<String> parse(String line) {
        //        System.out.println(Arrays.asList(line.split("\\|")));
        return Arrays.asList(line.split("\\|"));
    }
}
