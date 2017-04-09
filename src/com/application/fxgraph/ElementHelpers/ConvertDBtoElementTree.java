package com.application.fxgraph.ElementHelpers;

import com.application.db.DAOImplementation.ElementDAOImpl;
import com.application.db.DAOImplementation.ElementToChildDAOImpl;

import javax.swing.text.html.Option;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        // parent = stack.peek();
        String msg = line.get(3);  // ToDo replace hardcoded indices with universal indices.
        switch (msg.toUpperCase()) {
            case "ENTER":   // Todo Performance: Use int codes instead of String like "ENTER":
                parent = cur;
                cur = new Element(parent);
                // stack.push(cur);
                /*if (parent != null) {
                    ElementToChildDAOImpl.insert(parent.getElementId(), cur.getElementId());
                }*/
                break;

            case "EXIT":
                cur = cur.getParent();
                // stack.pop();
                break;

            default:
                IllegalStateException up = new IllegalStateException("EventType should be either ENTER OR EXIT. This line caused exception: " + line);
                throw up;  // Yuck! Not having any of that :(
        }

        Integer threadId = Integer.valueOf(line.get(1));

        if (parent == null && !msg.equalsIgnoreCase("EXIT")) {
            if (!threadMapToRoot.containsKey(threadId)) {
                grandParent = new Element(null);
                grandParent.setChildren(new ArrayList<>(Arrays.asList(cur)));
                cur.setParent(grandParent);
                threadMapToRoot.put(threadId, grandParent);
                /*defaultInitialize(grandParent);
                ElementDAOImpl.insert(grandParent);*/
            } else {
                Element grandparent = threadMapToRoot.get(threadId);   // Get grandParent root for the current threadId
                grandparent.setChildren(new ArrayList<>(Collections.singletonList(cur)));       // set the current element as the child of the grandParent element.
                cur.setParent(grandparent);
            }
            // There seems to be a problem here. Because
            /*ElementToChildDAOImpl.insert(grandParent.getElementId(), cur.getElementId());*/
        }

        /*if ( msg.equalsIgnoreCase("ENTER")) {
            defaultInitialize(cur);
            ElementDAOImpl.insert(cur);
        }*/
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
    }

    public void recursivelyInsertElements(Element root) {
        if (root == null)
            return;
        // Insert this element into the ELEMENT table.
        ElementDAOImpl.insert(root);

        // Insert this element and its parent relation into ELEMENT_TO_CHILD table.
        ElementToChildDAOImpl.insert(
                root.getParent() == null? -1 : root.getParent().getElementId(),
                root.getElementId());

        System.out.println(root.getElementId() + " : " + root.getLevelCount()
                + " : " + root.getLeafCount());

        // Recursively call this same method on all the children of current element.
//        Optional.ofNullable(root.getChildren()).ifPresent(list -> list.stream().forEachOrdered(this::recursivelyInsertElements));
        if (root.getChildren() != null) {
            root.getChildren().stream().forEachOrdered(this::recursivelyInsertElements);
        }
    }

    public void fromDBToUI() {
        // start with the level 0 and 1 elements and create circles for them.
        // get element id of the grand parent elements.
        // get element ids of all level 1 roots.
        // display them.
        try {
            ResultSet rs = ElementDAOImpl.selectWhere("parent_id = -1");
            rs.next();
            int grandParentId = rs.getInt("id");
            rs = ElementDAOImpl.selectWhere("parent_id = " + grandParentId);
            while (rs.next()) {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

