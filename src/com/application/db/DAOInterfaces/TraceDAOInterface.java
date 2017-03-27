package com.application.db.DOAInterfaces;

import java.sql.ResultSet;

/**
 * Data Access Object Interface for each log statement in the Call Trace Log file
 */
public interface TraceDAOInterface {
    public static final String TRACE_TABLE_NAME = "CALL_TRACE";

    public boolean insert();
    public boolean createTable();
    public ResultSet select(int numOfRows);

}
