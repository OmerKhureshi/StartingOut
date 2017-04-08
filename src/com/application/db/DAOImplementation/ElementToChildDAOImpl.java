package com.application.db.DAOImplementation;

import com.application.db.DatabaseUtil;
import com.application.db.TableNames;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.application.db.TableNames.ELEMENT_TO_CHILD_TABLE;

public class ElementToChildDAOImpl {
    private static boolean isTableCreated = false;

    public static void createTable() {
        if (!isTableCreated()) {
            try (Connection c = DatabaseUtil.getConnection(); Statement ps = c.createStatement()) {
                String sql = "CREATE TABLE " + ELEMENT_TO_CHILD_TABLE + " (" +
                        "    \"parent_id\" INTEGER, " +  // todo define foreign key
                        "    \"child_id\" INTEGER" +  // todo define foreign key
                        ")";
                ps.execute(sql);
                System.out.println("Creating table " + TableNames.ELEMENT_TO_CHILD_TABLE);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isTableCreated() {
        if (!isTableCreated)  // No need to call DatabaseUtil method every time. Save time this way.
            isTableCreated = DatabaseUtil.isTableCreated(ELEMENT_TO_CHILD_TABLE);
        return isTableCreated;
    }

    public static void insert(int elementId, int childId) {
        if (!isTableCreated)
            createTable();

        try (Connection c = DatabaseUtil.getConnection(); Statement ps = c.createStatement()) {
            String sql = "INSERT INTO " + ELEMENT_TO_CHILD_TABLE + " VALUES( " +
                    elementId + ", " +
                    childId +
                    ")";
            ps.execute(sql);
            System.out.println(TableNames.ELEMENT_TO_CHILD_TABLE + ": Inserted: " + sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ElementToChildDAOImpl().createTable();
    }

    public static void dropTable() {
        if (isTableCreated()) {
            try (Connection c = DatabaseUtil.getConnection(); Statement ps = c.createStatement()) {
                String sql= "Drop table " + TableNames.ELEMENT_TO_CHILD_TABLE;
                System.out.println("ELEMENT_TO_CHILD_TABLE dropped");
                ps.execute(sql);
                isTableCreated = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
