package com.application.logs;

import com.application.db.DatabaseUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

public class ParseCallTrace implements FileParser {
    private BufferedReader br;
    String line;
    List<String> vals;
    @Override
    public boolean readFile(File logFile) {
        try {
            br = new BufferedReader(new FileReader(logFile));
            while((line = br.readLine()) != null) {
                DatabaseUtil.insertCTStmt(parse(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public List<String> parse(String line) {
        System.out.println(Arrays.asList(line.split("|")));
        return Arrays.asList(line.split("|"));
    }
}
