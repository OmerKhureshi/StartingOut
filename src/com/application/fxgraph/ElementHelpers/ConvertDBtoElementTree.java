package com.application.fxgraph.ElementHelpers;

import com.application.fxgraph.ElementHelpers.Element;

import java.util.*;

public class ConvertDBtoElementTree {

    // Stores the root grandParent Elements for each tree having a unique thread id.
    // These grandParent Elements do not represent any method invocations.
    private Map<Integer, Element> threadMapToRoot = new LinkedHashMap<>();

    // Used internally for linking parent and child Elements.
    private Deque<Element> stack = new LinkedList<>();

    Element grandParent = null;
    // Used internally to save state of the Element in the previous run of this method.
    Element parent = null;
    public ArrayList<Element> rootsList;

    public ConvertDBtoElementTree() {
        rootsList = new ArrayList<>();
    }

    public void StringToElementList(List<String> line) {
        Element cur = null;
        parent = stack.peek();
        String msg = line.get(3);
        switch (msg.toUpperCase()) {
            // If EventType is ENTER, the current Element is the child of the parent Element which was saved
            // in the previous run of this method.
            // If this method is invoked for the first time or if this is the first
            // Element with the thread id then this Element is the root of the Tree for the thread id
            // and its parent is null.
            case "ENTER":   // Todo Use int codes instead of String like "ENTER":
                cur = new Element(parent);
                stack.push(cur);
                System.out.println("Line: " + line);
                break;

            // If EventType is EXIT, not much to do. Except maintain the stack.
            case "EXIT":
                System.out.println("Line: " + line);
                stack.pop();
                break;

            // The default case is when the EventType does not have the valid value. This is BAD! THROW UP IMMEDIATELY!
            default:
                System.out.println("This is the yucky part: " + line);
                IllegalStateException up = new IllegalStateException("EventType should be either ENTER OR EXIT");
                throw up;  // Yuck! Not having any of that :(
        }

        // Save the root of each thread tree in the map along with its threadId.
        // The stack operation
        Integer threadId = Integer.valueOf(line.get(1));

        //        if (parent == null) {
        //            // todo figure out storing thread id.
        //            rootsList.add(cur);
        //        }


        // Need a null root node that is the parent of all the grandparent nodes.
        // grandparent elements are parents of all the root elements for a specific thread id.
        if (parent == null) {
            if (!threadMapToRoot.containsKey(threadId)) {
                Element grandParent = new Element(null);
                grandParent.setChildren(new ArrayList<>(Arrays.asList(cur)));
                cur.setParent(grandParent);
                threadMapToRoot.put(threadId, grandParent);
                System.out.println(">>> New grandparent added. and root added to it. " + cur);
            } else {
                Element grandparent = threadMapToRoot.get(threadId);   // Get grandParent root for the current threadId
                grandparent.setChildren(new ArrayList<>(Collections.singletonList(cur)));       // set the current element as the child of the grandParent element.
                cur.setParent(grandparent);
                System.out.println(">>> root added to grandparent." + cur);
            }
        }

        // Problem: How to keep track of root elements.
        // soln: Used thread id as a criteria to store roots of unique threads.
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

        //        rootsList.stream()
        //                .forEachOrdered(root -> {
        //                    root.calculateLeafCount();
        //                    root.calculateLevelCount(0);
        //                    root.setBoundBoxOnAll(root);
        //                });
    }
}
