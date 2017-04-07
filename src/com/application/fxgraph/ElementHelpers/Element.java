package com.application.fxgraph.ElementHelpers;

import com.application.fxgraph.cells.CircleCell;
import com.application.fxgraph.graph.BoundBox;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Element class represent each method invocation on the UI. To avoid confusion, it has not been named as Node, which is
 * used by JavaFX or Cell, which is used for a different purpose here.
 */
public class Element {
    private static final AtomicInteger count = new AtomicInteger(0);
    private int elementId;
    private Element parent;
    private List<Element> children;
    private int indexInParent;

    private int leafCount = 0;
    private boolean isLeafCountSet = false;
    private int levelCount = 0;
    private BoundBox boundBox = new BoundBox();

    private int coordMultiplier = 1;

    public Element(Element parent) {
        elementId = count.incrementAndGet();
        this.parent = parent;
        if (parent != null ) {
            // If this element has a parent.
            // Todo Performance: Can improve. Use guava?
            parent.setChildren(new ArrayList<>(Arrays.asList(this)));
            setIndexInParent(parent.getChildren().size()-1);
        } else {
            // If this element is the root.
            setIndexInParent(0);
        }
    }

    public int getElementId() {
        return elementId;
    }

    public CircleCell getCircleCell() {
        return circleCell;
    }

    public void setCircleCell(CircleCell circleCell) {
        this.circleCell = circleCell;
    }

    public CircleCell circleCell = null;

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

    public void setParent(Element parent) {
        this.parent = parent;
        if (parent != null ) {
            // If this element has a parent.
            // Todo Performance: Can improve. Use guava?
//            parent.setChildren(new ArrayList<>(Collections.singletonList(this)));
            setIndexInParent(parent.getChildren().size()-1);
        } else {
            // If this element is the root.
            setIndexInParent(0);
        }
    }

    public Element getParent() {
        return parent;
    }

    public List<Element> getChildren() {
        return children;
    }

    /**
     * Appends or assigns the passed argument list to the current list of child elements depending on if the children
     * list of the current element already has elements or is null.
     * @param children the list of child elements to append or assign to list of children.
     */
    public void setChildren(List<Element> children) {
        int ind = 0;
        if (this.children != null) {
            ind = this.children.size()-1;
            this.children.addAll(children);
        } else {
            this.children = children;
        }

        for (Element element :
                children) {
            element.setIndexInParent(++ind);
        }
    }

    public int getLeafCount() {
        // ToDo add exception if calculateleafCount was not invoked.
        if (!isLeafCountSet)
            return -10;
//            throw new IllegalAccessException("Leaf count is accessed before calulating it.");
        return leafCount;
    }

    private void setLeafCount(int leafCount) {
        isLeafCountSet = true;
        this.leafCount = leafCount;
    }

    /**
     * Calculates and sets the leaf count of the current element and all the elements in this tree.
     * Leaf count is the count of the number of leaves or element in this tree that have no children.
     * The only exception is a leaf which will have a leaf count of 1.
     * Every time a the element tree is manipulated, this method has to be called on the root of the tree to recalculate
     * leaf count of all the children.
     *
     * @return leaf count
     */
    public int calculateLeafCount() {
        int count=0;

        // If current element is a leaf.
        if (children == null) {
            setLeafCount(1);
            return 1;
        }

        // If current element is not a leaf.
        for (Element ele: children) {
            count += ele.calculateLeafCount();
        }
        setLeafCount(count);

        return count;
    }

    /**
     * Calculates the max height of the tree and updates the value of levelCount for all the elements that are direct or
     * indirect children of the current tree.
     * This method has to be invoked everytime the tree has been manipulated.
     *
     * @param yourLevel the level of the root of the tree.
     * @return the value of the argument passed. Used to support recurrence internally.
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
    private void setBoundBox() {
        if (getParent() != null && getIndexInParent() != 0) {
            // If this element has another sibling element before it, get few of its bounds.
            Element sib = getParent().getChildren().get(getIndexInParent() - 1);
            BoundBox sibBB = sib.boundBox;

            boundBox.xTopLeft = sibBB.xBottomLeft;
            boundBox.yTopLeft = sibBB.yBottomLeft;
            System.out.println("first loop: getParent(): " + getParent() + " getIndexInParent(): " + getIndexInParent());
        } else if (getParent() == null) {

            // If this element is the root of the tree.
            boundBox.xTopLeft= 0;
            boundBox.yTopLeft = 0;
            System.out.println("second loop: getParent(): " + getParent() + " getIndexInParent(): " + getIndexInParent());
        } else {
            // If this element is the first child of its parent element.
            BoundBox parentBB = getParent().boundBox;

            boundBox.xTopLeft = parentBB.xTopRight;
            boundBox.yTopLeft = parentBB.yTopRight;
            System.out.println("third loop: getParent(): " + getParent() + " getIndexInParent(): " + getIndexInParent());
        }

        boundBox.xTopRight = boundBox.xTopLeft + boundBox.unitWidthFactor;
        boundBox.yTopRight = boundBox.yTopLeft;

        boundBox.xBottomLeft = boundBox.xTopLeft ;
        boundBox.yBottomLeft = boundBox.yTopLeft + (boundBox.unitHeightFactor * leafCount);

        boundBox.xBottomRight = boundBox.xTopRight;
        boundBox.yBottomRight = boundBox.yBottomLeft;

        boundBox.xCord = boundBox.xTopLeft + (boundBox.xTopRight - boundBox.xTopLeft) / 2;  // Use this instead of just adding and dividing by 2 to avoid overflow.
        boundBox.yCord = boundBox.yTopLeft + (boundBox.yBottomLeft - boundBox.yTopLeft) / 2;  // Use this instead of just adding and dividing by 2 to avoid overflow.

        setCoordMultiplier(coordMultiplier);
    }

    /**
     * This method is used to set bound box properties on all the elements in the tree.
     * This method has to be called every time the tree is manipulated.
     *
     * @param root root element of the tree
     */
    public void setBoundBoxOnAll(Element root) {
        if (root == null) return;

        root.setBoundBox();

        Optional.ofNullable(root.getChildren()).ifPresent(l -> l.forEach(ele -> {
            setBoundBoxOnAll(ele);
        }));
    }

    public BoundBox getBoundBox() {
        return boundBox;
    }

    public void calculateElementProperties(Element root, int levelCount) {
        this.calculateLevelCount(levelCount);
        this.calculateLeafCount();
        this.setBoundBoxOnAll(root);
    }

    @Override
    public String toString() {
        return "Element{" +
//                "parent levelCount=" + parent.getLevelCount() +
//                ", children size=" + Optional.ofNullable(children).; +
                ", indexInParent=" + indexInParent +
                ", leafCount=" + leafCount +
                ", isLeafCountSet=" + isLeafCountSet +
                ", levelCount=" + levelCount +
                ", boundBox=" + boundBox +
                ", coordMultiplier=" + coordMultiplier +
                '}';
    }

    public int getCoordMultiplier() {
        return coordMultiplier;
    }

    public void setCoordMultiplier(int coordMultiplier) {
        this.coordMultiplier = coordMultiplier;

//        boundBox.xCord *= coordMultiplier;
//        boundBox.yCord *= coordMultiplier;
    }
}