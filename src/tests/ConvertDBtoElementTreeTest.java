package tests;

import com.application.fxgraph.ElementHelpers.Element;
import com.application.fxgraph.ElementHelpers.ConvertDBtoElementTree;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class ConvertDBtoElementTreeTest {
    List<List<String>> logInTest1 = new ArrayList<>();
    ConvertDBtoElementTree convertDBtoElementTreeTest1 = new ConvertDBtoElementTree();

    List<List<String>> logInTest2 = new ArrayList<>();
    ConvertDBtoElementTree convertDBtoElementTreeTest2 = new ConvertDBtoElementTree();

    List<List<String>> logInTest3 = new ArrayList<>();
    ConvertDBtoElementTree convertDBtoElementTreeTest3 = new ConvertDBtoElementTree();

    @BeforeEach
    void setUp() {
        /*
        Test 1 initializations.
         */
        logInTest1.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
        logInTest1.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
        logInTest1.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
        logInTest1.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
        // Construct tree.
        for (List<String> line : logInTest1) {
            convertDBtoElementTreeTest1.StringToElementList(line, 0);
        }

        /*
        Test 2 initializations.
         */
        // thread id = 1
        logInTest2.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
            logInTest2.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
                logInTest2.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
                logInTest2.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
                logInTest2.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
                logInTest2.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
            logInTest2.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
        logInTest2.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
        // thread id = 2
        logInTest2.add(Arrays.asList("0", "2", "0","ENTER", "params", "21:34:55"));
            logInTest2.add(Arrays.asList("0", "2", "0","ENTER", "params", "21:34:55"));
            logInTest2.add(Arrays.asList("0", "2", "0", "EXIT", "params", "21:34:55"));
        logInTest2.add(Arrays.asList("0", "2", "0", "EXIT", "params", "21:34:55"));
        // Construct tree.
        for (List<String> line : logInTest2) {
            convertDBtoElementTreeTest2.StringToElementList(line, 0);
        }

        /*
        Test 3 initializations
         */
        // thread id = 1
        logInTest3.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
            logInTest3.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
                logInTest3.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
                logInTest3.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
                logInTest3.add(Arrays.asList("0", "1", "0","ENTER", "params", "21:34:55"));
                logInTest3.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
            logInTest3.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
        logInTest3.add(Arrays.asList("0", "1", "0", "EXIT", "params", "21:34:55"));
        // thread id = 2
        logInTest3.add(Arrays.asList("0", "2", "0","ENTER", "params", "21:34:55"));
            logInTest3.add(Arrays.asList("0", "2", "0","ENTER", "params", "21:34:55"));
                logInTest3.add(Arrays.asList("0", "2", "0","ENTER", "params", "21:34:55"));
                logInTest3.add(Arrays.asList("0", "2", "0", "EXIT", "params", "21:34:55"));
                logInTest3.add(Arrays.asList("0", "2", "0","ENTER", "params", "21:34:55"));
                logInTest3.add(Arrays.asList("0", "2", "0", "EXIT", "params", "21:34:55"));
            logInTest3.add(Arrays.asList("0", "2", "0", "EXIT", "params", "21:34:55"));
        logInTest3.add(Arrays.asList("0", "2", "0", "EXIT", "params", "21:34:55"));
        // thread id = 3
        logInTest3.add(Arrays.asList("0", "3", "0","ENTER", "params", "21:34:55"));
            logInTest3.add(Arrays.asList("0", "3", "0","ENTER", "params", "21:34:55"));
                logInTest3.add(Arrays.asList("0", "3", "0","ENTER", "params", "21:34:55"));
                logInTest3.add(Arrays.asList("0", "3", "0", "EXIT", "params", "21:34:55"));
                logInTest3.add(Arrays.asList("0", "3", "0","ENTER", "params", "21:34:55"));
                logInTest3.add(Arrays.asList("0", "3", "0", "EXIT", "params", "21:34:55"));
            logInTest3.add(Arrays.asList("0", "3", "0", "EXIT", "params", "21:34:55"));
        logInTest3.add(Arrays.asList("0", "3", "0", "EXIT", "params", "21:34:55"));
        // Construct tree
        for (List<String> line : logInTest3) {
            convertDBtoElementTreeTest3.StringToElementList(line, 0);
        }
    }

    @AfterEach
    void tearDown() {
        // GC bait.
        logInTest1 = null;
        logInTest2 = null;
        logInTest3 = null;
    }

    @Test
    void testGetThreadMapToRoot() {
        /*
          Test 1 begins
         */
        // Test number of trees with unique thread ids
        Assertions.assertEquals(1,
                convertDBtoElementTreeTest1.getThreadMapToRoot().size()
                , "threadMapToRoot calculated incorrectly.");
        // Test child element count of root element
        Assertions.assertEquals(1,
                convertDBtoElementTreeTest1.getThreadMapToRoot().get(1).getChildren().size());

        /*
        Test 2 begins
         */
        // Test number of trees with unique thread ids
        Assertions.assertEquals(2,
                convertDBtoElementTreeTest2.getThreadMapToRoot().size()
                , "threadMapToRoot calculated incorrectly.");
        // Test child element count of elements
        Assertions.assertEquals(1,
                convertDBtoElementTreeTest2.getThreadMapToRoot().get(1).getChildren().size());
        Assertions.assertEquals(2,
                convertDBtoElementTreeTest2.getThreadMapToRoot().get(1).getChildren().get(0).getChildren().size());
        Assertions.assertEquals(1,
                convertDBtoElementTreeTest2.getThreadMapToRoot().get(2).getChildren().size());

        /*
          Test 3 begins
         */
        // Test count of unique thread ids. Also count of roots.
        Assertions.assertEquals(3,
                convertDBtoElementTreeTest3.getThreadMapToRoot().size()
                , "threadMapToRoot calculated incorrectly.");
        // Test child element count of elements
        Assertions.assertEquals(1,
                convertDBtoElementTreeTest3.getThreadMapToRoot().get(1).getChildren().size());
        Assertions.assertEquals(2,
                convertDBtoElementTreeTest3.getThreadMapToRoot().get(1).getChildren().get(0).getChildren().size());
        Assertions.assertEquals(1,
                convertDBtoElementTreeTest3.getThreadMapToRoot().get(2).getChildren().size());
        Assertions.assertEquals(2,
                convertDBtoElementTreeTest3.getThreadMapToRoot().get(2).getChildren().get(0).getChildren().size());
        Assertions.assertEquals(1,
                convertDBtoElementTreeTest3.getThreadMapToRoot().get(3).getChildren().size());
        Assertions.assertEquals(2,
                convertDBtoElementTreeTest3.getThreadMapToRoot().get(3).getChildren().get(0).getChildren().size());
    }

    @Test
    void testCalculateElementProperties() {
        /*
        Test 1 begins
         */
        convertDBtoElementTreeTest1.calculateElementProperties();
        Element rootForThread1 = convertDBtoElementTreeTest1.getThreadMapToRoot().get(1);  // get root for thread id = 1
        Assertions.assertEquals(0, rootForThread1.getLevelCount());
        Assertions.assertEquals(1, rootForThread1.getLeafCount());
        // No need to test properties for child node as these are handled in tests for Element class.

        /*
        Test 2 begins
         */
        convertDBtoElementTreeTest2.calculateElementProperties();
        rootForThread1 = convertDBtoElementTreeTest2.getThreadMapToRoot().get(1);  // get root for thread id = 1
        Assertions.assertEquals(0, rootForThread1.getLevelCount());
        Assertions.assertEquals(2, rootForThread1.getLeafCount());
        Element rootForThread2 = convertDBtoElementTreeTest2.getThreadMapToRoot().get(2);  // get root for thread id = 2
        Assertions.assertEquals(0, rootForThread2.getLevelCount());
        Assertions.assertEquals(1, rootForThread2.getLeafCount());

        /*
        Test 3 begins
         */
        convertDBtoElementTreeTest3.calculateElementProperties();
        rootForThread1 = convertDBtoElementTreeTest3.getThreadMapToRoot().get(1);  // get root for thread id = 1
        Assertions.assertEquals(0, rootForThread1.getLevelCount());
        Assertions.assertEquals(2, rootForThread1.getLeafCount());
        rootForThread2 = convertDBtoElementTreeTest3.getThreadMapToRoot().get(2);  // get root for thread id = 2
        Assertions.assertEquals(0, rootForThread2.getLevelCount());
        Assertions.assertEquals(2, rootForThread2.getLeafCount());
        Element rootForThread3 = convertDBtoElementTreeTest3.getThreadMapToRoot().get(3);  // get root for thread id = 3
        Assertions.assertEquals(0, rootForThread3.getLevelCount());
        Assertions.assertEquals(2, rootForThread3.getLeafCount());

    }
}