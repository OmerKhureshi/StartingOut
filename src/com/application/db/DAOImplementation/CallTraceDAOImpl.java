package com.application.db.DAOImplementation;

import com.application.db.DatabaseUtil;
import com.application.db.TableNames;

import java.sql.*;
import java.util.List;

import static com.application.db.TableNames.CALL_TRACE_TABLE;

public class CallTraceDAOImpl {
    // TODO: Create abstract classes for all these Impl classes. All common funtionality is implement by the default class. Rest of the methods are abstract.
    private static boolean isTableCreated = false;

    public static boolean isTableCreated() {
        //        System.out.println("starting isTableCreated");
        if (!isTableCreated) {// No need to call DatabaseUtil method every time. Save time this way.
            //            System.out.println("CallTraceDAOImpl:isTableCreated: " + isTableCreated);
            isTableCreated = DatabaseUtil.isTableCreated(CALL_TRACE_TABLE);
            //            System.out.println("CallTraceDAOImpl:isTableCreated: " + isTableCreated);
        }
        //        System.out.println("ending isTableCreated");
        return isTableCreated;
    }

    public static void createTable() {
        //        System.out.println("starting createTable");
        //        System.out.println("CallTraceDAOImpl:createTable: " + isTableCreated());
        if (!isTableCreated()) {
            try (Connection c = DatabaseUtil.getConnection(); Statement ps = c.createStatement()) {

                sql = "CREATE TABLE " + CALL_TRACE_TABLE + " (" +
                        "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                        "process_id INTEGER not null, " +
                        "thread_id INTEGER, " +
                        "method_id INTEGER, " +
                        "message VARCHAR(20), " +
                        "parameters VARCHAR(200), " +
                        "time_instant VARCHAR(24)" +
                        ")";

                ps.execute(sql);
                isTableCreated = true;
                //                System.out.println("Creating table " + TableNames.ELEMENT_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //        System.out.println("ending createTable");
    }

    public static int insert(List<String> vals)
            throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{


        //       394 | 0 | 1 | Enter|[2131427413]|2017-03-01 21:34:55.529
        int autoIncrementedId = -1;
        int processID = Integer.parseInt(vals.get(0));
        int threadID = Integer.parseInt(vals.get(1));
        int methodID = Integer.parseInt(vals.get(2));
        String eventType = vals.get(3);
        String parameters = vals.get(4);
        String timeStamp = vals.get(5);

        //        System.out.println("starting insert");
        //        System.out.println("CallTraceDAOImpl:insert: " + isTableCreated());

        if (!isTableCreated())
            createTable();
        String sql = null;
        try (Connection c = DatabaseUtil.getConnection(); Statement ps = c.createStatement()) {

            //            System.out.println("value of callTraceTableCreated: " + callTraceTableCreated);
            sql = "INSERT INTO " + CALL_TRACE_TABLE +
                    "(" +
                    "process_id, " +
                    "thread_id, " +
                    "method_id, " +
                    "message, " +
                    "parameters, " +
                    "time_instant" +
                    ")" +
                    " VALUES("+
                    processID  + ", " +
                    threadID   + ", " +
                    methodID   + ", " +
                    "'" + eventType  + "', " +
                    "'" + parameters + "', " +
                    "'" + timeStamp  + "'" +
                    ")";

            //            System.out.println("Inserting into call trace the statement: " + sql);
            ps.execute(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                autoIncrementedId = rs.getInt(1);
            }

            // autoIncrementedId = rs.getInt("ID");
            //            System.out.println(TableNames.ELEMENT_TABLE + ": Inserted: " + sql);
        } catch (SQLException e) {
            System.out.println(" Exception caused by: " + sql);
            e.printStackTrace();
        }
        //        System.out.println("ending insert");
        return autoIncrementedId;
    }


    public static void dropTable() {
        //        System.out.println("starting dropTable");
        if (isTableCreated()) {
            try (Connection c = DatabaseUtil.getConnection(); Statement ps = c.createStatement()) {
                String sql= "Drop table " + TableNames.CALL_TRACE_TABLE;
                  // System.out.println("ELEMENT_TABLE dropped");
                ps.execute(sql);
                isTableCreated = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //        System.out.println("ending dropTable");
    }

    static Connection conn;
    static Statement ps;
    static String sql;
    public static ResultSet selectWhere(String where) {
        if (isTableCreated()) try {
            conn = DatabaseUtil.getConnection();
            ps = conn.createStatement();
            sql = "SELECT * FROM " + CALL_TRACE_TABLE + " WHERE " + where;
            //                System.out.println(">>> we got " + sql);
            ResultSet resultSet = ps.executeQuery(sql);
            //                resultSet.next();
            //                System.out.println(resultSet.getInt("id"));
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Line that threw error: " + sql);
            e.printStackTrace();
        }
        throw new IllegalStateException("Table does not exist. Hence cannot fetch any rows from it.");
    }
}
