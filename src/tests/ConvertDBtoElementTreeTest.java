package tests;

import com.application.fxgraph.graph.Element;
import com.application.logs.renameUnwrapper.ConvertDBtoElementTree;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omg.CORBA.INTERNAL;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ConvertDBtoElementTreeTest {
    List<List<String>> logIn = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // All setup done in each test.
    }

    @AfterEach
    void tearDown() {
        logIn = null;  // GC bait.
    }

    @Test
    void getThreadMapToRoot() {

        /*
          Test 1 begins
         */
        logIn.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
            logIn.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
            logIn.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
        logIn.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));

        ConvertDBtoElementTree convertDBtoElementTree = new ConvertDBtoElementTree();

        for (List<String> line : logIn) {
            convertDBtoElementTree.StringToElementList(line);
        }

        // Test number of trees with unique thread ids
        Assertions.assertEquals(1,
                convertDBtoElementTree.getThreadMapToRoot().size()
                , "threadMapToRoot calculated incorrectly.");

        // Test child element count of root element
        Assertions.assertEquals(1,
                convertDBtoElementTree.getThreadMapToRoot().get(1).getChildren().size());


        logIn.clear();

        // thread id = 1
        logIn.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
            logIn.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
            logIn.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
        logIn.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));

        // thread id = 2
        logIn.add(Arrays.asList("0", "2", "0","ENTER", "params", "21:34:55"));
        logIn.add(Arrays.asList("0", "2", "0","ENTER", "params", "21:34:55"));
        logIn.add(Arrays.asList("0", "2", "0", "EXIT", "params", "21:34:55"));
        logIn.add(Arrays.asList("0", "2", "0", "EXIT", "params", "21:34:55"));

        convertDBtoElementTree = new ConvertDBtoElementTree();

        for (List<String> line : logIn) {
            convertDBtoElementTree.StringToElementList(line);
        }

        // Test number of trees with unique thread ids
        Assertions.assertEquals(2,
                convertDBtoElementTree.getThreadMapToRoot().size()
                , "threadMapToRoot calculated incorrectly.");

        // Test child element count of elements
        Assertions.assertEquals(1,
                convertDBtoElementTree.getThreadMapToRoot().get(1).getChildren().size());

        Assertions.assertEquals(2,
                convertDBtoElementTree.getThreadMapToRoot().get(1).getChildren().get(0).getChildren().size());

        Assertions.assertEquals(1,
                convertDBtoElementTree.getThreadMapToRoot().get(2).getChildren().size());

        /*
          Test 2 begins
         */
        logIn.clear();

        // thread id = 1
        logIn.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
            logIn.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
            logIn.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
        logIn.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));

        // thread id = 2
        logIn.add(Arrays.asList("0", "2", "0","ENTER", "params", "21:34:55"));
            logIn.add(Arrays.asList("0", "2", "0","ENTER", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "2", "0","ENTER", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "2", "0", "EXIT", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "2", "0","ENTER", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "2", "0", "EXIT", "params", "21:34:55"));
            logIn.add(Arrays.asList("0", "2", "0", "EXIT", "params", "21:34:55"));
        logIn.add(Arrays.asList("0", "2", "0", "EXIT", "params", "21:34:55"));

        // thread id = 3
        logIn.add(Arrays.asList("0", "3", "0","ENTER", "params", "21:34:55"));
            logIn.add(Arrays.asList("0", "3", "0","ENTER", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "3", "0","ENTER", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "3", "0", "EXIT", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "3", "0","ENTER", "params", "21:34:55"));
                logIn.add(Arrays.asList("0", "3", "0", "EXIT", "params", "21:34:55"));
            logIn.add(Arrays.asList("0", "3", "0", "EXIT", "params", "21:34:55"));
        logIn.add(Arrays.asList("0", "3", "0", "EXIT", "params", "21:34:55"));

        convertDBtoElementTree = new ConvertDBtoElementTree();

        for (List<String> line : logIn) {
            convertDBtoElementTree.StringToElementList(line);
        }

        // Test count of unique thread ids. Also count of roots.
        Assertions.assertEquals(3,
                convertDBtoElementTree.getThreadMapToRoot().size()
                , "threadMapToRoot calculated incorrectly.");

        // Test child element count of elements
        Assertions.assertEquals(1,
                convertDBtoElementTree.getThreadMapToRoot().get(1).getChildren().size());

        Assertions.assertEquals(2,
                convertDBtoElementTree.getThreadMapToRoot().get(1).getChildren().get(0).getChildren().size());

        Assertions.assertEquals(1,
                convertDBtoElementTree.getThreadMapToRoot().get(2).getChildren().size());

        Assertions.assertEquals(2,
                convertDBtoElementTree.getThreadMapToRoot().get(2).getChildren().get(0).getChildren().size());

        Assertions.assertEquals(1,
                convertDBtoElementTree.getThreadMapToRoot().get(3).getChildren().size());

        Assertions.assertEquals(2,
                convertDBtoElementTree.getThreadMapToRoot().get(3).getChildren().get(0).getChildren().size());
    }

}