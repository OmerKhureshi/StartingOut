package com.application.fxgraph.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Element class represent each method invocation on the UI. To avoid confusion, it has not been named as Node, which is
 * used by JavaFX or Cell, which is used for a different purpose here.
 */
public class Element {
    private Element parent;
    private List<Element> children;

    public int getIndInParent() {
        return indInParent;
    }

    public void setIndInParent(int indInParent) {
        this.indInParent = indInParent;
    }

    private int indInParent;
    private int leafCount = 0;
    private int levelCount = 0;

    BoundBox boundBox = new BoundBox();

    public int getLevelCount() {
        return levelCount;
    }

    public void setLevelCount(int levelCount) {
        this.levelCount = levelCount;
    }

    public Element(Element parent) {
        this.parent = parent;
    }

    public Element(Element parent, ArrayList<Element> children) {
        this.parent = parent;
        this.children = children;
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
        this.children = children;
    }

    public int getLeafCount() {
        return leafCount;
    }

    public void setLeafCount(int leafCount) {
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

        // If current element is a leaf. Sets its leaf count as 0 as this is the default value for a leaf. Return 1 so
        // its parent can set its leaf count as 1 plus leaf count of its other children.
        if (children == null) {
            leafCount = 0;
            return 1;
        }

        for (Element ele: children) {
            count += ele.calculateLeafCount();
        }
        setLeafCount(count);

        return count == 0? 1 : count;
    }

    /**
     * Calculates the max height of the tree and updates the value of levelCount for all the elements that are direct or
     * indirect children of the current tree.
     * @return the height of the tree rooted at current element.
     */
    public int calculateLevelCount(int count) {
        setLevelCount(count);

        if (getChildren() == null)
            return count;

        for (Element ele : getChildren()) {
            ele.calculateLevelCount(count + 1);
        }

        return count;
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

    public Element getTopSibling() {
        return parent.getChildren().get(0);
    }

    public void setBoundBox() {
        if (getIndInParent() != 0) {
            Element sib = getParent().getChildren().get(getIndInParent() - 1);
            BoundBox sibBB = sib.boundBox;

            boundBox.xTopLeft = sibBB.xBottomLeft;
            boundBox.yTopLeft = sibBB.yBottomLeft;

            boundBox.xTopRight = sibBB.xTopLeft + boundBox.unitWidthFactor;
            boundBox.yTopRight = sibBB.yTopLeft;

            boundBox.xBottomLeft = boundBox.xTopLeft ;
            boundBox.yBottomLeft = boundBox.yTopLeft + (boundBox.unitHeightFactor * leafCount);

            boundBox.xBottomRight = boundBox.xTopRight;
            boundBox.yBottomRight = boundBox.yBottomLeft;
        }
    }

    //ToDo implement addition of indInParent.
}