package com.application.fxgraph.graph;

public class BoundBox {
    public float xTopLeft;
    public float yTopLeft;

    public float xTopRight;
    public float yTopRight;

    public float xBottomRight;
    public float yBottomRight;

    public float xBottomLeft;
    public float yBottomLeft;

    public final int unitWidthFactor = 100; // The width of the rectangular space that contains a single element on UI
    public final int unitHeightFactor = 100;// The height of the rectangular space that contains a single element on UI

    public float xCord;
    public float yCord;
}
