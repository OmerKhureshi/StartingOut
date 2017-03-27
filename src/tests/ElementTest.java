package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.application.fxgraph.graph.Element;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ElementTest {
    Element root = null;

    @BeforeEach
    void setUp() {
        root = new Element(null);
        Element c11= new Element(root);
        Element c12= new Element(root);
        Element c13= new Element(root);
        root.setChildren(Arrays.asList(new Element[] {c11, c12, c13}));

        Element c21= new Element(c11);
        Element c22= new Element(c11);
        Element c23= new Element(c11);
        c11.setChildren(Arrays.asList(new Element[] {c21, c22, c23}));

        Element c24= new Element(c13);
        c13.setChildren(Arrays.asList(new Element[] {c24}));

        Element c31= new Element(c24);
        c24.setChildren(Arrays.asList(new Element[] {c31}));

        Element c41= new Element(c31);
        c31.setChildren(Arrays.asList(new Element[] {c41}));


    }

    @AfterEach
    void tearDown() {
        root = null;  // hoping GC will find root soon.
    }

    @Test
    void testCalculateLeafCount() {
        // Element.calculateLeafCount is a calls itself recursively internally to calculate the leaf count.
        // It does not return the leaf count.

        root.calculateLeafCount();
        Assertions.assertEquals(5,root.getLeafCount(),"Leaf count of root not calculated correctly.");

        root.getChildren().get(0).calculateLeafCount();
        Assertions.assertEquals(3, root.getChildren().get(0).getLeafCount(),"Leaf count of element c11 not calculated correctly.");

        root.getChildren().get(1).calculateLeafCount();
        Assertions.assertEquals(0, root.getChildren().get(1).getLeafCount(),"Leaf count of element c12 not calculated correctly.");

        root.getChildren().get(2).calculateLeafCount();
        Assertions.assertEquals(1, root.getChildren().get(2).getLeafCount(),"Leaf count of element 13 not calculated correctly.");
    }

    @Test
    void testCalculateLevelCount() {
        root.calculateLevelCount(0);
        Assertions.assertEquals(0,root.getLevelCount(),"Level count of root not calculated correctly.");

        Element C41 = root.getChildren().get(2)
                .getChildren().get(0)
                .getChildren().get(0)
                .getChildren().get(0);
        Assertions.assertEquals(4,C41.getLevelCount(), "Level count of C41 not calculated correctly.");

        Element C22 = root.getChildren().get(0)
                .getChildren().get(1);
        Assertions.assertEquals(2,C22.getLevelCount(), "Level count of C22 not calculated correctly.");



    }

    @Test
    void testGetRoot() {
        Assertions.assertEquals((Element)root, root.getChildren().get(0).getRoot(), "c11 root not matching.");
        Assertions.assertEquals((Element)root, root.getChildren().get(0).getChildren().get(0).getRoot(), "c21 root not matching.");
        Assertions.assertNull(root.getRoot(), "root's parents should be null.");

        // If you need an explicit fail.
        //Assertions.assertNull(root.getChildren().get(0).getChildren().get(0).getRoot(), "root's parents should be null.");
    }
}