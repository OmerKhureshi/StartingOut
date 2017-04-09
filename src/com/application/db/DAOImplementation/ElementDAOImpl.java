package com.application.db.DAOImplementation;

import com.application.db.DatabaseUtil;
import com.application.db.TableNames;
import com.application.fxgraph.ElementHelpers.Element;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.application.db.TableNames.ELEMENT_TABLE;

public class ElementDAOImpl {
    private static boolean isTableCreated = false;

    public static boolean isTableCreated() {
//        System.out.println("starting isTableCreated");
        if (!isTableCreated) {// No need to call DatabaseUtil method every time. Save time this way.
//            System.out.println("ElementDAOImpl:isTableCreated: " + isTableCreated);
            isTableCreated = DatabaseUtil.isTableCreated(ELEMENT_TABLE);
//            System.out.println("ElementDAOImpl:isTableCreated: " + isTableCreated);
        }
//        System.out.println("ending isTableCreated");
        return isTableCreated;
    }

    public static void createTable() {
//        System.out.println("starting createTable");
//        System.out.println("ElementDAOImpl:createTable: " + isTableCreated());
        if (!isTableCreated()) {
            try (Connection c = DatabaseUtil.getConnection(); Statement ps = c.createStatement()) {
                String sql = "CREATE TABLE " + ELEMENT_TABLE + " (" +
                        "id INTEGER NOT NULL, " +
                        "parent_id INTEGER, " +  // todo define foreign key
                        "bound_box_x_top_left FLOAT, " +
                        "bound_box_y_top_left FLOAT, " +
                        "bound_box_x_top_right FLOAT, " +
                        "bound_box_y_top_right FLOAT, " +
                        "bound_box_x_bottom_right FLOAT, " +
                        "bound_box_y_bottom_right FLOAT, " +
                        "bound_box_x_bottom_left FLOAT, " +
                        "bound_box_y_bottom_left FLOAT, " +
                        "bound_box_x_coordinate FLOAT, " +
                        "bound_box_y_coordinate FLOAT, " +
                        "index_in_parent INTEGER, " +
                        "leaf_count INTEGER, " +
                        "level_count INTEGER" +
                        ")";
                ps.execute(sql);
//                System.out.println("Creating table " + TableNames.ELEMENT_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("ending createTable");
    }

    public static void insert(Element element) {
//        System.out.println("starting insert");
//        System.out.println("ElementDAOImpl:insert: " + isTableCreated());
        if (!isTableCreated())
            createTable();
        String sql = null;
        try (Connection c = DatabaseUtil.getConnection(); Statement ps = c.createStatement()) {
            sql = "INSERT INTO " + TableNames.ELEMENT_TABLE + " VALUES (" +
                    element.getElementId() + ", " +
                    (element.getParent() == null? -1 : element.getParent().getElementId()) + ", " +
                    element.getBoundBox().xTopLeft + ", " +
                    element.getBoundBox().yTopLeft + ", " +
                    element.getBoundBox().xTopRight + ", " +
                    element.getBoundBox().yTopRight + ", " +
                    element.getBoundBox().xBottomRight + ", " +
                    element.getBoundBox().yBottomRight + ", " +
                    element.getBoundBox().xBottomLeft + ", " +
                    element.getBoundBox().yBottomLeft + ", " +
                    element.getBoundBox().xCoordinate + ", " +
                    element.getBoundBox().yCoordinate + ", " +
                    element.getIndexInParent() + ", " +
                    element.getLeafCount() + ", " +
                    element.getLevelCount() + ")";

            ps.execute(sql);
//            System.out.println(TableNames.ELEMENT_TABLE + ": Inserted: " + sql);
        } catch (SQLException e) {
            System.out.println(" Exception caused by: " + sql);
            e.printStackTrace();
        }
//        System.out.println("ending insert");
    }

    public static void dropTable() {
//        System.out.println("starting dropTable");
        if (isTableCreated()) {
            try (Connection c = DatabaseUtil.getConnection(); Statement ps = c.createStatement()) {
                String sql= "Drop table " + TableNames.ELEMENT_TABLE;
//                System.out.println("ELEMENT_TABLE dropped");
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
    public static ResultSet selectWhere(String where) {
        if (isTableCreated()) {
            try  {
                conn = DatabaseUtil.getConnection();
                ps = conn.createStatement();
                String sql = "SELECT * FROM " + ELEMENT_TABLE + " WHERE " + where;
//                System.out.println(">>> we got " + sql);
                ResultSet resultSet = ps.executeQuery(sql);
//                resultSet.next();
//                System.out.println(resultSet.getInt("id"));
                return resultSet;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalStateException("Table does not exist. Hence cannot fetch any rows from it.");
    }


}
