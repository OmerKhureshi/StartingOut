package tests;

import com.application.fxgraph.graph.BoundBox;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.application.fxgraph.graph.Element;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ElementTest {
    Element root = null;

    Element C11;
    Element C12;
    Element C13;
    Element C41;
    Element C21;
    Element C22;
    Element C23;
    Element C31;

    @BeforeEach
    void setUp() {
        root = new Element(null);
        C11= new Element(root);
        C12= new Element(root);
        C13= new Element(root);

        C21 = new Element(C11);
        C22 = new Element(C11);
        C23 = new Element(C11);
//        C11.setChildren(Arrays.asList(new Element[] {C21, C22, C23}));
//
//        Element c24= new Element(C13);
//        C13.setChildren(Arrays.asList(new Element[] {c24}));
//
//        C31= new Element(c24);
//        c24.setChildren(Arrays.asList(new Element[] {C31}));
//
//        C41 = new Element(C31);
//        C31.setChildren(Arrays.asList(new Element[] {C41}));

        // Todo dont have to call setChildren everytime. Use this.getParent().getChildren().add(this); and also set the index in parent.
    }

    @AfterEach
    void tearDown() {
        root = null;  // hoping GC will find root soon.
    }

    @Test
    void testCalculateLeafCount() {
        // Element.calculateLeafCount is a calls itself recursively internally to calculate the leaf count.
        // It does not return the leaf count.
        Assertions.assertEquals(5, root.getLeafCount(),"Leaf count of root not calculated correctly.");
        Assertions.assertEquals(3, C11.getLeafCount(),"Leaf count of element C11 not calculated correctly.");
        Assertions.assertEquals(1, C12.getLeafCount(),"Leaf count of element C12 not calculated correctly.");
        Assertions.assertEquals(1, C13.getLeafCount(),"Leaf count of element C13 not calculated correctly.");
    }

    @Test
    void testCalculateLevelCount() {
        root.calculateLevelCount(0);
        Assertions.assertEquals(0,root.getLevelCount(),"Level count of root not calculated correctly.");
        Assertions.assertEquals(4,C41.getLevelCount(), "Level count of C41 not calculated correctly.");
        Assertions.assertEquals(2,C22.getLevelCount(), "Level count of C22 not calculated correctly.");
    }

    @Test
    void testGetRoot() {
        Assertions.assertEquals((Element)root, C11.getRoot(), "c11 root not matching.");
        Assertions.assertEquals((Element)root, C21.getRoot(), "c21 root not matching.");
        Assertions.assertNull(root.getRoot(), "root's parents should be null.");

        // If you need an explicit fail.
        //Assertions.assertNull(root.getChildren().get(0).getChildren().get(0).getRoot(), "root's parents should be null.");
    }

    @Test
    void testGetBoundBox() {
        // Test roots bounds and coordinates.
        // Element needs to be set up.
//        root.setIndexInParent(0);
//        root.calculateLeafCount();
//        root.setBoundBox();

        Assertions.assertEquals(0, root.getBoundBox().xTopLeft, "root bound box's x top left should be 0.");
        Assertions.assertEquals(0, root.getBoundBox().yTopLeft, "root bound box's y top left should be 0.");

        Assertions.assertEquals(root.getBoundBox().unitWidthFactor, root.getBoundBox().xTopRight, "root bound box's x top right should be 3.");
        Assertions.assertEquals(0, root.getBoundBox().yTopRight, "root bound box's y top right should be 0.");

        Assertions.assertEquals(0, root.getBoundBox().xBottomLeft, "root bound box's x bottom left should be 0.");
        Assertions.assertEquals(root.getBoundBox().unitHeightFactor * root.getLeafCount(), root.getBoundBox().yBottomLeft, "root bound box's y bottom left should be " + root.getBoundBox().unitHeightFactor * root.getLeafCount());

        Assertions.assertEquals(root.getBoundBox().unitWidthFactor, root.getBoundBox().xBottomRight, "root bound box's x bottom right should be 3.");
        Assertions.assertEquals(root.getBoundBox().unitHeightFactor * root.getLeafCount(), root.getBoundBox().yBottomRight, "root bound box's y bottom right should be " + root.getBoundBox().unitHeightFactor * root.getLeafCount());

        // Test C11 bounds and coordinates.
//        C11.setIndexInParent(0);
//        C11.calculateLeafCount();
//        C11.setBoundBox();

        Assertions.assertEquals(3, C11.getBoundBox().xTopLeft, "C11 bound box's x top left should be 3.");
        Assertions.assertEquals(0, C11.getBoundBox().yTopLeft, "C11 bound box's y top left should be 0.");

        Assertions.assertEquals(C11.getBoundBox().unitWidthFactor * 2, C11.getBoundBox().xTopRight, "C11 bound box's x top right should be 6.");
        Assertions.assertEquals(0, C11.getBoundBox().yTopRight, "C11 bound box's y top right should be 0.");

        Assertions.assertEquals(3, C11.getBoundBox().xBottomLeft, "C11 bound box's x bottom left should be 3.");
        Assertions.assertEquals(C11.getBoundBox().unitHeightFactor * C11.getLeafCount(), C11.getBoundBox().yBottomLeft, "C11 bound box's y bottom left should be " + C11.getBoundBox().unitHeightFactor * C11.getLeafCount());

        Assertions.assertEquals(C11.getBoundBox().unitWidthFactor * 2, C11.getBoundBox().xBottomRight, "C11 bound box's x bottom right should be 6.");
        Assertions.assertEquals(C11.getBoundBox().unitHeightFactor * C11.getLeafCount(), C11.getBoundBox().yBottomRight, "C11 bound box's y bottom right should be " + C11.getBoundBox().unitHeightFactor * C11.getLeafCount());

        // Test C13 bounds and coordinates.
//        C12.setIndexInParent(1);
//        C12.calculateLeafCount();
//        C12.setBoundBox();

        Assertions.assertEquals(C12.getBoundBox().unitWidthFactor, C12.getBoundBox().xTopLeft, "C12 bound box's x top left should be 3.");
        Assertions.assertEquals(9, C12.getBoundBox().yTopLeft, "C12 bound box's y top left should be 9.");

        Assertions.assertEquals(C12.getBoundBox().unitWidthFactor * 2, C12.getBoundBox().xTopRight, "C12 bound box's x top right should be 6.");
        Assertions.assertEquals(9, C12.getBoundBox().yTopRight, "C12 bound box's y top right should be 9.");

        Assertions.assertEquals(C12.getBoundBox().unitWidthFactor, C12.getBoundBox().xBottomLeft, "C12 bound box's x bottom left should be 3.");
        Assertions.assertEquals(12, C12.getBoundBox().yBottomLeft, "C12 bound box's y bottom left should be 12.");

        Assertions.assertEquals(C12.getBoundBox().unitWidthFactor * 2, C12.getBoundBox().xBottomRight, "C12 bound box's x bottom right should be 6.");
        Assertions.assertEquals(12, C12.getBoundBox().yBottomRight, "C12 bound box's y bottom right should be 12.");


        // Test C31 bounds and coordinates.


//        C31.setIndexInParent(0);
//        C31.calculateLeafCount();
//        C31.setBoundBox();
//
//        Assertions.assertEquals(C31.getBoundBox().unitWidthFactor * 3, C31.getBoundBox().xTopLeft, "C31 bound box's x top left should be 9.");
//        Assertions.assertEquals(12, C31.getBoundBox().yTopLeft, "C31 bound box's y top left should be 12.");
//
//        Assertions.assertEquals(C31.getBoundBox().unitWidthFactor * 4, C31.getBoundBox().xTopRight, "C31 bound box's x top right should be 12.");
//        Assertions.assertEquals(12, C31.getBoundBox().yTopRight, "C31 bound box's y top right should be 12.");
//
//        Assertions.assertEquals(C31.getBoundBox().unitWidthFactor * 3, C31.getBoundBox().xBottomLeft, "C31 bound box's x bottom left should be 9.");
//        Assertions.assertEquals(15, C31.getBoundBox().yBottomLeft, "C31 bound box's y bottom left should be 15.");
//
//        Assertions.assertEquals(C31.getBoundBox().unitWidthFactor * 4, C31.getBoundBox().xBottomRight, "C31 bound box's x bottom right should be 12.");
//        Assertions.assertEquals(15, C31.getBoundBox().yBottomRight, "C31 bound box's y bottom right should be 15.");

    }
}