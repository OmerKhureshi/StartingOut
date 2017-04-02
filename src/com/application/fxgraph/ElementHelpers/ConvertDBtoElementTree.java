package com.application.fxgraph.ElementHelpers;

import com.application.fxgraph.ElementHelpers.Element;

import java.util.*;

public class ConvertDBtoElementTree {

    // Stores the root Elements for each tree having a unique thread id.
    private Map<Integer, Element> threadMapToRoot = new LinkedHashMap<>();

    // Used internally for linking parent and child Elements.
    private Deque<Element> stack = new LinkedList<>();

    // Used internally to save state of the Element in the previous run of this method.
    Element parent = null;

    public void StringToElementList(List<String> line) {
        Element cur = null;
        parent = stack.peek();
        String msg = line.get(3);

        switch (msg) {
            // If EventType is ENTER, the current Element is the child of the parent Element which was saved
            // in the previous run of this method.
            // If this method is invoked for the first time or if this is the first
            // Element with the thread id then this Element is the root of the Tree for the thread id
            // and its parent is null.
            case "ENTER":   // Todo no need to have "ENTER", Use int codes instead:
                cur = new Element(parent);
                stack.push(cur);
                break;

            // If EventType is EXIT, not much to do. Except maintain the stack.
            case "EXIT":
                stack.pop();
                break;

            // The default case is when the EventType does not have the valid value. This is BAD! THROW UP IMMEDIATELY!
            default:
                IllegalStateException up = new IllegalStateException("EventType should be either ENTER OR EXIT");
                throw up;  // Yuck! Not having any of that :(
        }

        // Save the root of each thread tree in the map along with its threadId.
        // The stack operation
        Integer threadId = Integer.valueOf(line.get(1));
        if (!threadMapToRoot.containsKey(threadId)) {
            threadMapToRoot.put(threadId, cur);
        }
    }

    public Map<Integer, Element> getThreadMapToRoot() {
        return threadMapToRoot;
    }

    /**
     * Calculates the Element properties on all direct and indirect children of current element.
     * Ensure that the sub tree is fully constructed before invoking this method.
     */
    public void calculateElementProperties() {
        /*
        Iterate through the threadMapToRoot and calculate Element properties for all the roots.
         */
        threadMapToRoot.entrySet().stream()
                .map(Map.Entry::getValue)
                .forEachOrdered(root -> {
                    root.calculateLeafCount();
                    root.calculateLevelCount(0);
                    root.setBoundBoxOnAll(root);
                });
    }
}

