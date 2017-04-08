package com.application.fxgraph.ElementHelpers;

import com.application.db.DAOImplementation.ElementDAOImpl;
import com.application.db.DAOImplementation.ElementToChildDAOImpl;
import com.application.fxgraph.ElementHelpers.Element;
import com.sun.javafx.collections.ElementObservableListDecorator;

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
    Element cur = null;
    public ArrayList<Element> rootsList;

    public ConvertDBtoElementTree() {
        rootsList = new ArrayList<>();
    }

    public void StringToElementList(List<String> line) {
//        parent = stack.peek();
        String msg = line.get(3);  // ToDo replace hardcoded indices with universal indices.
        switch (msg.toUpperCase()) {
            // If EventType is ENTER, the current Element is the child of the parent dElement which was saved
            // in the previous run of this method.
            // If this method is invoked for the first time or if this is the first
            // Element with the thread id then this Element is the root of the Tree for the thread id
            // and its parent is null.
            case "ENTER":   // Todo Performance: Use int codes instead of String like "ENTER":
                System.out.println(">>>>>>>>>> ENTER");
                parent = cur;
                cur = new Element(parent);

                //                stack.push(cur);
                if (parent != null) {
                    ElementToChildDAOImpl.insert(parent.getElementId(), cur.getElementId());
                }

                break;

            case "EXIT":
                System.out.println(">>>>>>>>>> EXIT");
                cur = cur.getParent();
//                stack.pop();
                break;

            // The default case is when the EventType does not have the valid value. This is BAD! THROW UP IMMEDIATELY!
            default:
                IllegalStateException up = new IllegalStateException("EventType should be either ENTER OR EXIT. This line caused exception: " + line);
                throw up;  // Yuck! Not having any of that :(
        }
        // Save the root of each thread tree in the map along with its threadId.
        // The stack operation
        Integer threadId = Integer.valueOf(line.get(1));

        //            // todo figure out storing thread id.


        if (parent == null && !msg.equalsIgnoreCase("EXIT")) {
            // How is this not executed when event type is exit?
            if (!threadMapToRoot.containsKey(threadId)) {
                System.out.println(">>>>>>>> parent null loop 1");
                grandParent = new Element(null);
                grandParent.setChildren(new ArrayList<>(Arrays.asList(cur)));
                cur.setParent(grandParent);
                threadMapToRoot.put(threadId, grandParent);
                // insert grandParent
                defaultInitialize(grandParent);
                ElementDAOImpl.insert(grandParent);
                System.out.println(">>> New grandparent inserted and linked to child.");
            } else {
                System.out.println(">>>>>>>> parent null loop 2");
                Element grandparent = threadMapToRoot.get(threadId);   // Get grandParent root for the current threadId
                grandparent.setChildren(new ArrayList<>(Collections.singletonList(cur)));       // set the current element as the child of the grandParent element.
                cur.setParent(grandparent);
                System.out.println(">>> root added to grandparent.");
            }
            // Insert grandparent <-> cur element relation
            ElementToChildDAOImpl.insert(grandParent.getElementId(), cur.getElementId());
        }

        if ( msg.equalsIgnoreCase("ENTER")) {
            System.out.println(">>>>>>>>>>>>>  Loop ENTER");
            // Insert cur element.
            defaultInitialize(cur);
            ElementDAOImpl.insert(cur);
        }
        // Problem: How to keep track of root elements.
        // soln: Used thread id as a criteria to store roots of unique threads.
    }

    private void defaultInitialize(Element element) {
        cur.setLeafCount(-1);
        cur.setLevelCount(-1);
        cur.getBoundBox().xTopLeft = -1;
        cur.getBoundBox().yTopLeft = -1;
        cur.getBoundBox().xTopRight = -1;
        cur.getBoundBox().yTopRight = -1;
        cur.getBoundBox().xBottomRight = -1;
        cur.getBoundBox().yBottomRight = -1;
        cur.getBoundBox().xBottomLeft = -1;
        cur.getBoundBox().yBottomLeft = -1;
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

