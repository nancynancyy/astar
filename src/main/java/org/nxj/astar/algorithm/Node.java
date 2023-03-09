package org.nxj.astar.algorithm;

/**
 * @author nxj
 */
public class Node {

    private int g;
    private int f;
    private int h;
    private int x;
    private int y;
    private boolean isBlock;
    private Node parent;

    public Node(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public void calculateHeuristic(Node finalNode) {
        this.h = Math.abs(finalNode.getY() - getY()) + Math.abs(finalNode.getX() - getX());
    }

    public void setNodeData(Node currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        setParent(currentNode);
        setG(gCost);
        calculateFinalCost();
    }

    public boolean checkBetterPath(Node currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        if (gCost < getG()) {
            setNodeData(currentNode, cost);
            return true;
        }
        return false;
    }

    private void calculateFinalCost() {
        int finalCost = getG() + getH();
        setF(finalCost);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (getClass() != otherObject.getClass()) {
            return false;
        }
        Node other = (Node) otherObject;
        return this.getX() == other.getX() && this.getY() == other.getY();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + getX();
        result = 31 * result + getY();
        return result;
    }


    @Override
    public String toString() {
        return "Node [x=" + x + ", y=" + y + "]";
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean isBlock) {
        this.isBlock = isBlock;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


}