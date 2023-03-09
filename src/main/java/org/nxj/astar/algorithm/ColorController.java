package org.nxj.astar.algorithm;

/**
 * @author nxj
 */
@FunctionalInterface
public
interface ColorController
{
    /**
     * 在A*算法中调用，用于控制砖块背景颜色，以标识寻路过程
     * @param x position x
     * @param y position y
     */
    void switchColor(int x, int y);
}
