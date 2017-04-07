package com.application.db.DAOImplementation;

import com.application.db.DAOInterfaces.ElementToChildDAOInterface;
import com.application.db.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.application.db.TableNames.ELEMENT_TO_CHILD_TABLE;

public class ElementToChildDAOImpl implements ElementToChildDAOInterface {
    private boolean isTableCreated = false;

    @Override
    public void createTable() {
        if (!isTableCreated()) {
            try (Connection c = DatabaseUtil.getConnection(); Statement ps = c.createStatement()) {
                String sql = "CREATE TABLE " + ELEMENT_TO_CHILD_TABLE + " (" +
                        "    \"parent_id\" INTEGER, " +  // todo define foreign key
                        "    \"child_id\" INTEGER" +  // todo define foreign key
                        ")";
                ps.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isTableCreated() {
        if (!isTableCreated)  // No need to call DatabaseUtil method every time. Save time this way.
            isTableCreated = DatabaseUtil.isTableCreated(ELEMENT_TO_CHILD_TABLE);
        return isTableCreated;
    }

    @Override
    public void insert(int elementId, int childId) {
        if (!isTableCreated)
            createTable();

        try (Connection c = DatabaseUtil.getConnection(); Statement ps = c.createStatement()) {
            String sql = "INSERT INTO " + ELEMENT_TO_CHILD_TABLE + " VALUES( " +
                    elementId + ", " +
                    childId +
                    ")";
            ps.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ElementToChildDAOImpl().createTable();
    }
}
