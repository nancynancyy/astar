package org.nxj.astar.algorithm;

import java.util.ArrayList;
import java.util.List;

public class AStarTest {

    public static void main(String[] args) {
        Node initialNode = new Node(2, 1);
        Node finalNode = new Node(2, 5);
        int x = 15;
        int y = 6;
        ColorController yorController = (px,py) -> {};
        AStar aStar = new AStar(x, y, initialNode, finalNode, yorController);
        List<Node> blocksArray = new ArrayList<Node>();
        blocksArray.add(new Node(1,3));
        blocksArray.add(new Node(2,3));
        blocksArray.add(new Node(3,3));

        aStar.setBlocks(blocksArray);
        Node[][] area =  aStar.getSearchArea();
        for (Node[] nodes: area){
            for (Node node: nodes){
                if (node.equals(initialNode)){
                    System.out.print("S"+ "\t");
                } else if (node.equals(finalNode)){
                    System.out.print("D"+ "\t");
                } else {
                    System.out.print((node.isBlock() ? "B" : ".") + "\t");
                }
            }
            System.out.println();
        }

        List<Node> path = aStar.findPath();
        for (Node node : path) {
            System.out.println(node);
        }

        //Search Area
        //      0   1   2   3   4   5   6
        // 0    -   -   -   -   -   -   -
        // 1    -   -   -   B   -   -   -
        // 2    -   I   -   B   -   F   -
        // 3    -   -   -   B   -   -   -
        // 4    -   -   -   -   -   -   -
        // 5    -   -   -   -   -   -   -

        //Expected output with diagonals
        //Node [x=2, y=1]
        //Node [x=1, y=2]
        //Node [x=0, y=3]
        //Node [x=1, y=4]
        //Node [x=2, y=5]

        //Search Path with diagonals
        //      0   1   2   3   4   5   6
        // 0    -   -   -   *   -   -   -
        // 1    -   -   *   B   *   -   -
        // 2    -   I*  -   B   -  *F   -
        // 3    -   -   -   B   -   -   -
        // 4    -   -   -   -   -   -   -
        // 5    -   -   -   -   -   -   -

        //Expected output without diagonals
        //Node [x=2, y=1]
        //Node [x=2, y=2]
        //Node [x=1, y=2]
        //Node [x=0, y=2]
        //Node [x=0, y=3]
        //Node [x=0, y=4]
        //Node [x=1, y=4]
        //Node [x=2, y=4]
        //Node [x=2, y=5]

        //Search Path without diagonals
        //      0   1   2   3   4   5   6
        // 0    -   -   *   *   *   -   -
        // 1    -   -   *   B   *   -   -
        // 2    -   I*  *   B   *  *F   -
        // 3    -   -   -   B   -   -   -
        // 4    -   -   -   -   -   -   -
        // 5    -   -   -   -   -   -   -
    }
}