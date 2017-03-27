package tests;

import com.application.db.DAOImplementation.TraceDAOImpl;
import com.application.db.DAOInterfaces.TraceDAOInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TraceDAOImplTest {
    TraceDAOInterface traceTable;
    List<String> values;
    @BeforeEach
    void setUp() {
        traceTable = new TraceDAOImpl();
        values = Arrays.asList(
                "394", "0", "4", "Enter",
                "[Hello from the other side]",
                "2017-03-01 21:34:55.762");
    }

    @AfterEach
    void tearDown() {
        //Todo implement tear down to drop tables or delete the entire database.
    }

    @Test
    void insert() {
        Assertions.assertTrue(traceTable.insert(values));
    }

    @Test
    void createTable() {
        Assertions.assertTrue(traceTable.createTable());
    }

    @Test
    void select() {
        ResultSet rs = traceTable.select(1);
        String[] actual = new String[6];
        try {
            while(rs.next()){
                int columns = rs.getMetaData().getColumnCount();
                for (int i = 1; i <=columns ; i++) {
                    System.out.println(columns);
                    actual[i-1] = rs.getString(i);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Assertions.assertArrayEquals(values.toArray(), actual);
    }

}