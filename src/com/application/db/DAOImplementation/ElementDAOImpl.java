package com.application.db.DAOImplementation;

import com.application.db.DAOInterfaces.ElementDAOInterface;
import com.application.db.DatabaseUtil;
import com.application.db.TableNames;
import com.application.fxgraph.ElementHelpers.Element;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.application.db.TableNames.ELEMENT_TABLE;

public class ElementDAOImpl implements ElementDAOInterface {
    private boolean isTableCreated = false;

    @Override
    public boolean isTableCreated() {
        if (!isTableCreated)  // No need to call DatabaseUtil method every time. Save time this way.
            isTableCreated = DatabaseUtil.isTableCreated(ELEMENT_TABLE);
        return isTableCreated;
    }

    @Override
    public void createTable() {
        if (!isTableCreated()) {
            try (Connection c = DatabaseUtil.getConnection(); Statement ps = c.createStatement()) {
                String sql = "CREATE TABLE " + ELEMENT_TABLE + " (\n" +
                        "   \"id\" INTEGER NOT NULL, \n" +
                        "    \"parent_id\" INTEGER, \n" +  // todo define foreign key
                        // Bound Box properties.
                        "    \"bound_box_x_top_left\" FLOAT, \n" +
                        "    \"bound_box_y_top_left\" FLOAT, \n" +
                        "    \"bound_box_x_top_right\" FLOAT, \n" +
                        "    \"bound_box_y_top_right\" FLOAT, \n" +
                        "    \"bound_box_x_bottom_right\" FLOAT, \n" +
                        "    \"bound_box_y_bottom_right\" FLOAT, \n" +
                        "    \"bound_box_x_bottom_left\" FLOAT, \n" +
                        "    \"bound_box_y_bottom_left\" FLOAT, \n" +
                        "    \"bound_box_x_coordinate\" FLOAT, \n" +
                        "    \"bound_box_y_coordinate\" FLOAT, \n" +
                        // Other properties
                        "    \"index_in_parent\" INTEGER,\n" +
                        "    \"leaf_count\" INTEGER,\n" +
                        "    \"level_count\" INTEGER\n" +
                        /*"   FOREIGN KEY(\"methodID\") REFERENCES METHOD(\"methodID\")"+ */
                        ")";
                ps.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void insert(Element element) {
        if (!isTableCreated)
            createTable();
        String sql = "INSERT INTO " + TableNames.ELEMENT_TABLE + " VALUES " +
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
                element.getBoundBox().xCord + ", " +
                element.getBoundBox().yCord + ", " +
                element.getIndexInParent() + ", " +
                element.getLeafCount() + ", " +
                element.getLevelCount() + ")";

        try (Connection c = DatabaseUtil.getConnection(); Statement ps = c.createStatement()) {
            ps.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ElementDAOImpl().createTable();
    }
}
