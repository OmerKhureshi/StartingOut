package com.application.fxgraph.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Element class represent each method invocation on the UI. To avoid confusion, it has not been named as Node, which is
 * used by JavaFX or Cell, which is used for a different purpose here.
 */
public class Element {
    private Element parent;
    private List<Element> children;
    private int indexInParent;

    private int leafCount = 0;
    boolean isLeafCountSet = false;

    private int levelCount = 0;
    boolean isLevelCountSet = false;

    BoundBox boundBox = new BoundBox();

    // Assign this child as the parents child.
    // Set index in parent
    // calculate bound box
    // calculate leaf count.
    // calculate level count.

    public Element(Element parent) {
        this.parent = parent;
        setBoundBox();
        if (parent != null ) {
            // If this element has a parent.
            // Todo Performance: Can improve. Use guava?
            parent.setChildren(new ArrayList<Element>(Arrays.asList(this)));
            setIndexInParent(parent.getChildren().size()-1);
            calculateLevelCount(parent.getLevelCount()+1);
        } else {
            // If this element is the root.
            setIndexInParent(0);
            calculateLevelCount(0);
        }
        calculateLeafCount();
        setBoundBox();

    }
    public Element(Element parent, ArrayList<Element> children) {
        this(parent);
        setChildren(children);
        setIndexInParent(parent.getChildren().size()-1);
    }

    public int getIndexInParent() {
        return indexInParent;
    }

    public void setIndexInParent(int indexInParent) {
        this.indexInParent = indexInParent;
    }

    public int getLevelCount() {
        return levelCount;
    }

    public void setLevelCount(int levelCount) {
        this.levelCount = levelCount;
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public List<Element> getChildren() {
        return children;
    }

    public void setChildren(List<Element> children) {
        if (this.children != null)
            (this.children).addAll(children);
//            this.children.addAll(children);
        else
            this.children = children;
    }

    public int getLeafCount() {
        // ToDo add exception if calculateleafCount was not invoked.
        if (isLeafCountSet == false)
            return -10;
//            throw new IllegalAccessException("Leaf count is accessed before calulating it.");
        return leafCount;
    }

    public void setLeafCount(int leafCount) {
        isLeafCountSet = true;
        this.leafCount = leafCount;
    }

    /**
     * Calculates the leaf count of the current element. Leaf count is the count of the number of leaves or element in
     * this tree, that have no children. A leaf will have a leaf count of 0.
     * Note: this method does not return the leaf count. Use Element.getLeafCount() to get the leaf count of current element.
     * @return leaf count
     */
    public int calculateLeafCount() {
        int count=0;

//                 root
//         3           1          1
//        c11         c12        c13
//      1     1   1                1
//     c21  c22  c23              c24
//
//                                 c31
//
//                                 c41

        // If current element is a leaf. Sets its leaf count as 0 as this is the default value for a leaf. Return 1 so
        // its parent can set its leaf count as 1 plus leaf count of its other children.
        if (children == null) {
            setLeafCount(1);
            return 1;
        }

        for (Element ele: children) {
            count += ele.calculateLeafCount();
        }
        setLeafCount(count);
        return count;
    }

    /**
     * Calculates the max height of the tree and updates the value of levelCount for all the elements that are direct or
     * indirect children of the current tree.
     * @return the height of the tree rooted at current element.
     */
    public int calculateLevelCount(int yourLevel) {
        setLevelCount(yourLevel);

        if (getChildren() == null)
            return yourLevel;

        for (Element ele : getChildren()) {
            ele.calculateLevelCount(yourLevel + 1);
        }

        return yourLevel;
    }

    /**
     * Returns the root element of the tree of elements.
     * @return root element or null if current element is root.
     */
    public Element getRoot(){
        if(getParent() == null)
            return null;

        Element element = this;
        while (element.getParent() != null)
            element = element.getParent();

        return element;
    }

    /**
     * BoundBox defines the space occupied by each element on the UI. No other element can occupy this space.
     * The width of a BoundBox of all the element is a constant, unitWidthFactor. The height of a BoundBox is
     * determined by the number of leaves it has; represented by leafCount.
     */
    public void setBoundBox() {

        //Todo handle this case;
        // the leaf element has a leaf count of 0. Hence while calculating the yBottomLeft, which depends on the the leafcount,
        // we get 0 product. Infact, in such cases, that is for a leaf itself, its leaf count should have been 1. But the
        // class defines it as 0 to differentiate between a parent having single leaf or the leaf itself.
        if (getParent() != null && getIndexInParent() != 0) {
            // If this element has another sibling element before it, get few of its bounds.
            Element sib = getParent().getChildren().get(getIndexInParent() - 1);
            BoundBox sibBB = sib.boundBox;

            boundBox.xTopLeft = sibBB.xBottomLeft;
            boundBox.yTopLeft = sibBB.yBottomLeft;
        } else if (getParent() == null) {
            // If this element is the root of the tree.
            boundBox.xTopLeft= 0;
            boundBox.yTopLeft = 0;

        } else {
            // If this element is the first child of its parent element.
            Element parent = getParent();
            BoundBox parentBB = parent.boundBox;

            boundBox.xTopLeft = parentBB.xTopRight;
            boundBox.yTopLeft = parentBB.yTopRight;
        }

        boundBox.xTopRight = boundBox.xTopLeft + boundBox.unitWidthFactor;
        boundBox.yTopRight = boundBox.yTopLeft;

        boundBox.xBottomLeft = boundBox.xTopLeft ;
        boundBox.yBottomLeft = boundBox.yTopLeft + (boundBox.unitHeightFactor * leafCount);

        boundBox.xBottomRight = boundBox.xTopRight;
        boundBox.yBottomRight = boundBox.yBottomLeft;

        boundBox.xCord = boundBox.xTopLeft + (boundBox.xTopRight - boundBox.xTopLeft) / 2;  // Use this instead of just adding and dividing by 2 to avoid overflow.
        boundBox.yCord = boundBox.yTopLeft + (boundBox.yTopRight - boundBox.yTopLeft) / 2;  // Use this instead of just adding and dividing by 2 to avoid overflow.
    }

    public BoundBox getBoundBox() {
        return boundBox;
    }

    //ToDo implement addition of indexInParent.
}