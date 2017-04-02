package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.application.fxgraph.ElementHelpers.Element;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class ElementTest {
    private Element root, C1, C2, C3, C11, C12, C13, C31, C311, C3111;

    @BeforeEach
    void setUp() {
        root = new Element(null);

        // Children of root element
        C1 = new Element(root);
        C2 = new Element(root);
        C3 = new Element(root);

        // Children of element C1
        C11 = new Element(C1);
        C12 = new Element(C1);
        C13 = new Element(C1);

        // Child of element C3
        C31 = new Element(C3);

        // Child of element C31
        C311 = new Element(C31);

        // Child of element C311
        C3111 = new Element(C311);

        // calculateLeafCount() has to be called after adding all the child elements.
        root.calculateLeafCount();

        // calculateLevelCount() has to be called after adding all the child elements.
        root.calculateLevelCount(0);

        root.setBoundBoxOnAll(root);
    }


    @AfterEach
    void tearDown() {
          root = null;  // hoping GC will find root soon.
    }

    @Test
    void testSetChildren() {
        // Test root children
        List<Element> expected = Arrays.asList(C1, C2, C3);
        Assertions.assertEquals(expected, root.getChildren(), "Children of root element are incorrect.");

        // Test C1 children
        expected = Arrays.asList(C11, C12, C13);
        Assertions.assertEquals(expected, C1.getChildren(), "Children of element C1 are incorrect.");

        // Test C11 children
        Assertions.assertEquals(null, C11.getChildren(), "Children of element C11 are incorrect.");

        // Test C311 children
        expected = Collections.singletonList(C3111);
        Assertions.assertEquals(expected, C311.getChildren(), "Children of element C311 are incorrect.");

        Assertions.assertEquals(null,
                C11.getChildren(),
                "C11 should have no children");

        Assertions.assertEquals(null,
                C3111.getChildren(),
                "C3111 should have no children");
    }

    @Test
    void testSetIndexInParent() {
        Assertions.assertEquals(0, root.getIndexInParent(), "root index in parent incorrect");
        Assertions.assertEquals(0, C1.getIndexInParent(), "C1 index in parent incorrect");
        Assertions.assertEquals(2, C3.getIndexInParent(), "C3 index in parent incorrect");
        Assertions.assertEquals(0, C11.getIndexInParent(), "C11 index in parent incorrect");
        Assertions.assertEquals(2, C13.getIndexInParent(), "C13 index in parent incorrect");
        Assertions.assertEquals(0, C31.getIndexInParent(), "C31 index in parent incorrect");
        Assertions.assertEquals(0, C311.getIndexInParent(), "C311 index in parent incorrect");
        Assertions.assertEquals(0, C3111.getIndexInParent(), "C3111 index in parent incorrect");
    }

    @Test
    void testCalculateLeafCount() {
        Assertions.assertEquals(5, root.getLeafCount(),"Leaf count of root not calculated correctly.");

        Assertions.assertEquals(3, C1.getLeafCount(),"Leaf count of element C1 not calculated correctly.");
        Assertions.assertEquals(1, C2.getLeafCount(),"Leaf count of element C2 not calculated correctly.");
        Assertions.assertEquals(1, C3.getLeafCount(),"Leaf count of element C3 not calculated correctly.");

        Assertions.assertEquals(1, C11.getLeafCount(),"Leaf count of element C11 not calculated correctly.");
        Assertions.assertEquals(1, C13.getLeafCount(),"Leaf count of element C13 not calculated correctly.");

        Assertions.assertEquals(1, C31.getLeafCount(),"Leaf count of element C31 not calculated correctly.");
        Assertions.assertEquals(1, C311.getLeafCount(),"Leaf count of element C311 not calculated correctly.");
    }

    @Test
    void testCalculateLevelCount() {
        Assertions.assertEquals(0,root.getLevelCount(),"Level count of root not calculated correctly.");
        Assertions.assertEquals(1, C1.getLevelCount(), "Level count of C1 not calculated correctly.");
        Assertions.assertEquals(1, C3.getLevelCount(), "Level count of C3 not calculated correctly.");
        Assertions.assertEquals(2, C12.getLevelCount(), "Level count of C12 not calculated correctly.");
        Assertions.assertEquals(4, C3111.getLevelCount(), "Level count of C3111 not calculated correctly.");
    }

    @Test
    void testGetRoot() {
        Assertions.assertNull(root.getRoot(), "root's parents should be null.");
        Assertions.assertEquals(root, C1.getRoot(), "c11 root not matching.");
        Assertions.assertEquals(root, C11.getRoot(), "c21 root not matching.");
        Assertions.assertEquals(root, C3111.getRoot(), "c3111 root not matching.");

        // If you need an explicit fail.
        //Assertions.assertNull(root.getChildren().get(0).getChildren().get(0).getRoot(), "root's parents should be null.");
    }

    @Test
    void testGetBoundBox() {
        float level1XLeft = root.getBoundBox().unitWidthFactor;
        float level1XRight = root.getBoundBox().unitWidthFactor * 2;

        // test root element's bound box properties.
        Assertions.assertEquals(0, root.getBoundBox().xTopLeft, "root bound box's x top left should be 0.");
        Assertions.assertEquals(0, root.getBoundBox().yTopLeft, "root bound box's y top left should be 0.");

        Assertions.assertEquals(root.getBoundBox().unitWidthFactor, root.getBoundBox().xTopRight, "root bound box's x top right should be 3.");
        Assertions.assertEquals(0, root.getBoundBox().yTopRight, "root bound box's y top right should be 0.");

        Assertions.assertEquals(0, root.getBoundBox().xBottomLeft, "root bound box's x bottom left should be 0.");
        Assertions.assertEquals(root.getBoundBox().unitHeightFactor * root.getLeafCount(), root.getBoundBox().yBottomLeft, "root bound box's y bottom left should be " + root.getBoundBox().unitHeightFactor * root.getLeafCount());

        Assertions.assertEquals(root.getBoundBox().unitWidthFactor, root.getBoundBox().xBottomRight, "root bound box's x bottom right should be 3.");
        Assertions.assertEquals(root.getBoundBox().unitHeightFactor * root.getLeafCount(), root.getBoundBox().yBottomRight, "root bound box's y bottom right should be " + root.getBoundBox().unitHeightFactor * root.getLeafCount());

        // test element C1's bound box properties.
        Assertions.assertEquals(level1XLeft,
                C1.getBoundBox().xTopLeft,
                "C1 bound box's x top left calculated incorrectly.");

        Assertions.assertEquals(0,
                C1.getBoundBox().yTopLeft,
                "C1 bound box's y top left should be 0.");

        Assertions.assertEquals(level1XRight,
                C1.getBoundBox().xTopRight,
                "C1 bound box's x top right should be 6.");

        Assertions.assertEquals(0,
                C1.getBoundBox().yTopRight,
                "C1 bound box's y top right should be 0.");

        Assertions.assertEquals(level1XLeft,
                C1.getBoundBox().xBottomLeft,
                "C1 bound box's x bottom left should be 3.");

        Assertions.assertEquals(C1.getBoundBox().unitHeightFactor * C1.getLeafCount(),
                C1.getBoundBox().yBottomLeft,
                "C1 bound box's y bottom left calculated incorrectly");

        Assertions.assertEquals(level1XRight,
                C1.getBoundBox().xBottomRight,
                "C1 bound box's x bottom right should be 6.");

        Assertions.assertEquals(C1.getBoundBox().unitHeightFactor * C1.getLeafCount(),
                C1.getBoundBox().yBottomRight,
                "C1 bound box's y bottom right should be " + C1.getBoundBox().unitHeightFactor * C1.getLeafCount());


        // test element C2's bound box properties.
        Assertions.assertEquals(level1XLeft,
                C2.getBoundBox().xTopLeft,
                "C2 bound box's x top left calculated incorrectly.");

        Assertions.assertEquals(9,
                C2.getBoundBox().yTopLeft,
                "C2 bound box's y top left should be 9.");

        Assertions.assertEquals(level1XRight,
                C2.getBoundBox().xTopRight,
                "C2 bound box's x top right should be 6.");

        Assertions.assertEquals(9,
                C2.getBoundBox().yTopRight,
                "C2 bound box's y top right should be 9.");

        Assertions.assertEquals(level1XLeft,
                C2.getBoundBox().xBottomLeft,
                "C2 bound box's x bottom left should be 3.");

        Assertions.assertEquals(12,
                C2.getBoundBox().yBottomLeft,
                "C2 bound box's y bottom left should be 12.");

        Assertions.assertEquals(level1XRight,
                C2.getBoundBox().xBottomRight,
                "C2 bound box's x bottom right should be 6.");

        Assertions.assertEquals(12,
                C2.getBoundBox().yBottomRight,
                "C2 bound box's y bottom right should be 12.");


        // test element C3's bound box properties.
        Assertions.assertEquals(level1XLeft,
                C3.getBoundBox().xTopLeft,
                "C3 bound box's x top left calculated incorrectly.");

        Assertions.assertEquals(12,
                C3.getBoundBox().yTopLeft,
                "C3 bound box's y top left calculated incorrectly..");

        Assertions.assertEquals(level1XRight,
                C3.getBoundBox().xTopRight,
                "C3 bound box's x top right calculated incorrectly..");

        Assertions.assertEquals(12,
                C3.getBoundBox().yTopRight,
                "C3 bound box's y top right calculated incorrectly.");

        Assertions.assertEquals(level1XLeft,
                C3.getBoundBox().xBottomLeft,
                "C3 bound box's x bottom left calculated incorrectly..");

        Assertions.assertEquals(15,
                C3.getBoundBox().yBottomLeft,
                "C3 bound box's y bottom left calculated incorrectly..");

        Assertions.assertEquals(level1XRight,
                C3.getBoundBox().xBottomRight,
                "C3 bound box's x bottom right calculated incorrectly..");

        Assertions.assertEquals(15,
                C3.getBoundBox().yBottomRight,
                "C3 bound box's y bottom right calculated incorrectly.");


        // test element C13's bound box properties.
        float level2XLeft = root.getBoundBox().unitWidthFactor * 2;
        float level2XRight = root.getBoundBox().unitWidthFactor * 3;

        Assertions.assertEquals(level2XLeft,
                C13.getBoundBox().xTopLeft,
                "C13 bound box's x top left calculated incorrectly.");

        Assertions.assertEquals(6,
                C13.getBoundBox().yTopLeft,
                "C13 bound box's y top left calculated incorrectly.");

        Assertions.assertEquals(level2XRight,
                C13.getBoundBox().xTopRight,
                "C13 bound box's x top right calculated incorrectly..");

        Assertions.assertEquals(6,
                C13.getBoundBox().yTopRight,
                "C13 bound box's y top right calculated incorrectly.");

        Assertions.assertEquals(level2XLeft,
                C13.getBoundBox().xBottomLeft,
                "C13 bound box's x bottom left calculated incorrectly..");

        Assertions.assertEquals(9,
                C13.getBoundBox().yBottomLeft,
                "C13 bound box's y bottom left calculated incorrectly.");

        Assertions.assertEquals(level2XRight,
                C13.getBoundBox().xBottomRight,
                "C13 bound box's x bottom right calculated incorrectly..");

        Assertions.assertEquals(9,
                C13.getBoundBox().yBottomRight,
                "C13 bound box's y bottom right calculated incorrectly.");


        // Test C311 bounds and coordinates.
        float level3XLeft = root.getBoundBox().unitWidthFactor * 3;
        float level3XRight = root.getBoundBox().unitWidthFactor * 4;

        Assertions.assertEquals(level3XLeft,
                C311.getBoundBox().xTopLeft,
                "C311 bound box's x top left calculated incorrectly.");

        Assertions.assertEquals(12,
                C311.getBoundBox().yTopLeft,
                "C311 bound box's y top left calculated incorrectly.");

        Assertions.assertEquals(level3XRight,
                C311.getBoundBox().xTopRight,
                "C311 bound box's x top right calculated incorrectly.");

        Assertions.assertEquals(12,
                C311.getBoundBox().yTopRight,
                "C311 bound box's y top right calculated incorrectly.");

        Assertions.assertEquals(level3XLeft,
                C311.getBoundBox().xBottomLeft,
                "C311 bound box's x bottom left calculated incorrectly.");

        Assertions.assertEquals(15,
                C311.getBoundBox().yBottomLeft,
                "C311 bound box's y bottom left calculated incorrectly.");

        Assertions.assertEquals(level3XRight,
                C311.getBoundBox().xBottomRight,
                "C311 bound box's x bottom right calculated incorrectly.");

        Assertions.assertEquals(15,
                C311.getBoundBox().yBottomRight,
                "C311 bound box's y bottom right calculated incorrectly.");

        // Test C3111 bounds and coordinates.
        float level4XLeft = root.getBoundBox().unitWidthFactor * 4;
        float level4XRight = root.getBoundBox().unitWidthFactor * 5;

        Assertions.assertEquals(level4XLeft,
                C3111.getBoundBox().xTopLeft,
                "C3111 bound box's x top left calculated incorrectly.");

        Assertions.assertEquals(12,
                C3111.getBoundBox().yTopLeft,
                "C3111 bound box's y top left calculated incorrectly.");

        Assertions.assertEquals(level4XRight,
                C3111.getBoundBox().xTopRight,
                "C3111 bound box's x top right calculated incorrectly.");

        Assertions.assertEquals(12,
                C3111.getBoundBox().yTopRight,
                "C3111 bound box's y top right calculated incorrectly.");

        Assertions.assertEquals(level4XLeft,
                C3111.getBoundBox().xBottomLeft,
                "C3111 bound box's x bottom left calculated incorrectly.");

        Assertions.assertEquals(15,
                C3111.getBoundBox().yBottomLeft,
                "C3111 bound box's y bottom left calculated incorrectly.");

        Assertions.assertEquals(level4XRight,
                C3111.getBoundBox().xBottomRight,
                "C3111 bound box's x bottom right calculated incorrectly.");

        Assertions.assertEquals(15,
                C3111.getBoundBox().yBottomRight,
                "C3111 bound box's y bottom right calculated incorrectly.");

    }
}