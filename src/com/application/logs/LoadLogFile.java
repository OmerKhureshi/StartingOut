package com.application.logs;

import com.application.db.DatabaseUtil;

import javax.xml.crypto.Data;
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class LoadLogFile {
    FileParser fileParser = null;
    File logFile = null;

    public enum LogType {
        MethodDefn, CallTrace
    }

    public void load(LogType logType) {
        if (logType == LogType.MethodDefn) {
            new ParseMethodDefinition().readFile(new MethodDefinitionLogFile().getFile());
        } else if (logType == LogType.CallTrace) {
            fileParser = new ParseCallTrace();
            fileParser.readFile(CallTraceLogFile.getFile());
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting out");
        new LoadLogFile().load(LoadLogFile.LogType.MethodDefn);


        try {
            System.out.println(DatabaseUtil.select(10, "Method_Defn"));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
