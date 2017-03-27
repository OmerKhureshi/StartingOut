package com.application.logs;

import com.application.db.DatabaseUtil;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ParseMethodDefinition implements FileParser {
    private BufferedReader br;
    String line;
    List<String> vals;
    @Override
    public boolean readFile(File logFile) {
        try {
            br = new BufferedReader(new FileReader(logFile));
            // ToDo Look into streams to perform buffered read and insert.
            while((line = br.readLine()) != null) {
                DatabaseUtil.insertMDStmt(parse(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public List<String> parse(String line) {
//        System.out.println(Arrays.asList(line.split("\\|")));
        return Arrays.asList(line.split("\\|"));
    }
}
