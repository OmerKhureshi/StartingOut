package com.application.db.DAOInterfaces;

import java.sql.ResultSet;

/**
 * Data Access Object Interface for each log statement in the Call Trace Log file
 */
public interface DefnDAOInterface {
    public static final String DEFINITION_TABLE_NAME = "METHOD_DEFN";

    public boolean insert();
    public boolean createTable();
    public ResultSet select(int numOfRows);

}
