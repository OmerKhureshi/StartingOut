package com.application.logs;

import java.io.*;
import java.util.List;

public interface FileParser {
    BufferedReader bf = null;
//  method defn
//    3 | java/util/HashMap<TK;TV;> | putAll |  (Ljava/util/Map<+TK;+TV;>;)V

//    call trace
//    350|012345|1|Exit|[Hello world!,0,12,5,]|1962-09-23 03:23:34.234
    public boolean readFile (File logFile);

    public List<String> parse(String line);
}
