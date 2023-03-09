package org.nxj.astar.algorithm;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author nxj
 */
public class AStar {
    private static final int DEFAULT_HV_COST = 10;
    private static final int DEFAULT_DIAGONAL_COST = 14;
    private int hvCost;
    private int diagonalCost;
    private Node[][] searchArea;
    private PriorityQueue<Node> openList;
    private Set<Node> closedSet;
    private Node initialNode;
    private Node finalNode;
    private ColorController colorController;

    public AStar(int x, int y, Node initialNode, Node finalNode, int hvCost, int diagonalCost, ColorController colorController) {
        this.hvCost = hvCost;
        this.diagonalCost = diagonalCost;
        setInitialNode(initialNode);
        setFinalNode(finalNode);
        this.searchArea = new Node[x][y];
        this.openList = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        setNodes();
        this.closedSet = new HashSet<>();
        this.colorController = colorController;
    }

    public AStar(int x, int y, Node initialNode, Node finalNode, ColorController colorController) {
        this(x, y, initialNode, finalNode, DEFAULT_HV_COST, DEFAULT_DIAGONAL_COST, colorController);
    }

    private void setNodes() {
        for (int x = 0; x < searchArea.length; x++) {
            for (int y = 0; y < searchArea[0].length; y++) {
                Node node = new Node(x, y);
                node.calculateHeuristic(getFinalNode());
                this.searchArea[x][y] = node;
            }
        }
    }

    public void setBlocks(List<Node> blocks) {
        for (Node block : blocks) {
            int x = block.getX();
            int y = block.getY();
            setBlock(x, y);
        }
    }

    public List<Node> findPath() {
        openList.add(initialNode);
        while (!isEmpty(openList)) {
            Node currentNode = openList.poll();
            assert currentNode != null;
            closedSet.add(currentNode);
            if (isFinalNode(currentNode)) {
                List<Node> path = getPath(currentNode);
                for (Node node : path) {
                    colorController.switchColor(node.getX(), node.getY());
                }
                return getPath(currentNode);
            } else {
                addAdjacentNodes(currentNode);
            }
        }
        return new ArrayList<>();
    }

    private List<Node> getPath(Node currentNode) {
        List<Node> path = new ArrayList<>();
        path.add(currentNode);
        Node parent;
        while ((parent = currentNode.getParent()) != null) {
            path.add(0, parent);
            currentNode = parent;
        }
        return path;
    }

    private void addAdjacentNodes(Node currentNode) {
        addAdjacentUpperRow(currentNode);
        addAdjacentMiddleRow(currentNode);
        addAdjacentLowerRow(currentNode);
    }

    private void addAdjacentLowerRow(Node currentNode) {
        int x = currentNode.getX();
        int y = currentNode.getY();
        int lowerRow = x + 1;
        if (lowerRow < getSearchArea().length) {
            if (y - 1 >= 0) {
                checkNode(currentNode, y - 1, lowerRow, getDiagonalCost());
            }
            if (y + 1 < getSearchArea()[0].length) {
                checkNode(currentNode, y + 1, lowerRow, getDiagonalCost());
            }
            checkNode(currentNode, y, lowerRow, getHvCost());
        }
    }

    private void addAdjacentMiddleRow(Node currentNode) {
        int x = currentNode.getX();
        int y = currentNode.getY();
        if (y - 1 >= 0) {
            checkNode(currentNode, y - 1, x, getHvCost());
        }
        if (y + 1 < getSearchArea()[0].length) {
            checkNode(currentNode, y + 1, x, getHvCost());
        }
    }

    private void addAdjacentUpperRow(Node currentNode) {
        int x = currentNode.getX();
        int y = currentNode.getY();
        int upperRow = x - 1;
        if (upperRow >= 0) {
            if (y - 1 >= 0) {
                checkNode(currentNode, y - 1, upperRow, getDiagonalCost());
            }
            if (y + 1 < getSearchArea()[0].length) {
                checkNode(currentNode, y + 1, upperRow, getDiagonalCost());
            }
            checkNode(currentNode, y, upperRow, getHvCost());
        }
    }

    private void checkNode(Node currentNode, int x, int y, int cost) {
        Node adjacentNode = getSearchArea()[y][x];
        if (!adjacentNode.isBlock() && !getClosedSet().contains(adjacentNode)) {
            if (!getOpenList().contains(adjacentNode)) {
                adjacentNode.setNodeData(currentNode, cost);
                getOpenList().add(adjacentNode);
            } else {
                boolean changed = adjacentNode.checkBetterPath(currentNode, cost);
                if (changed) {
                    getOpenList().remove(adjacentNode);
                    getOpenList().add(adjacentNode);
                }
            }
        }
    }

    private boolean isFinalNode(Node currentNode) {
        return currentNode.equals(finalNode);
    }

    private boolean isEmpty(PriorityQueue<Node> openList) {
        return openList.isEmpty();
    }

    private void setBlock(int x, int y) {
        this.searchArea[x][y].setBlock(true);
    }

    public Node getInitialNode() {
        return initialNode;
    }

    public void setInitialNode(Node initialNode) {
        this.initialNode = initialNode;
    }

    public Node getFinalNode() {
        return finalNode;
    }

    public void setFinalNode(Node finalNode) {
        this.finalNode = finalNode;
    }

    public Node[][] getSearchArea() {
        return searchArea;
    }

    public void setSearchArea(Node[][] searchArea) {
        this.searchArea = searchArea;
    }

    public PriorityQueue<Node> getOpenList() {
        return openList;
    }

    public void setOpenList(PriorityQueue<Node> openList) {
        this.openList = openList;
    }

    public Set<Node> getClosedSet() {
        return closedSet;
    }

    public void setClosedSet(Set<Node> closedSet) {
        this.closedSet = closedSet;
    }

    public int getHvCost() {
        return hvCost;
    }

    public void setHvCost(int hvCost) {
        this.hvCost = hvCost;
    }

    private int getDiagonalCost() {
        return diagonalCost;
    }

    private void setDiagonalCost(int diagonalCost) {
        this.diagonalCost = diagonalCost;
    }
}
