package com.application.logs.parsers;

import com.application.db.DatabaseUtil;
import com.application.logs.parsers.FileParser;

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
    List<String> vals;

    // public boolean readFile(File logFile) {
    //     try {
    //         br = new BufferedReader(new FileReader(logFile));
    //         while((line = br.readLine()) != null) {
    //             DatabaseUtil.insertCTStmt(parse(line));
    //         }
    //     } catch (FileNotFoundException e) {
    //         e.printStackTrace();
    //     } catch (Exception e){
    //         e.printStackTrace();
    //     }
    //     return false;
    // }
    //
    @Override
    public void readFile(File logFile, Consumer<List<String>> cmd) {
        try {
            br = new BufferedReader(new FileReader(logFile));
            // ToDo Look into streams to perform buffered read and insert.
            while ((line = br.readLine()) != null) {
                List<String> brokenLineList = parse(line);
                cmd.accept(brokenLineList);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> parse(String line) {
        //        System.out.println(Arrays.asList(line.split("\\|")));
        return Arrays.asList(line.split("\\|"));
    }
}
