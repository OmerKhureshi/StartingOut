package com.application.fxgraph.graph;

import java.util.ArrayList;

public class CellTree {

}


class Element {
    Element parent;
    ArrayList<Element> children;
    int leafCount;

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public ArrayList<Element> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Element> children) {
        this.children = children;
    }

    public int getLeafCount() {
        return leafCount;
    }

    public void setLeafCount(int leafCount) {
        this.leafCount = leafCount;
    }

    public int claculateLeafCount() {

        return 0;
    }
}