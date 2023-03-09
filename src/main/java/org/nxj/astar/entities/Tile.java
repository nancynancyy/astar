package org.nxj.astar.entities;

import org.nxj.astar.Application;

import java.awt.*;
import java.util.Map;

/**
 * @author nxj
 */
public class Tile extends Entity {
	
    private Color backgroundColor;
    private final boolean blocked;
    
    public boolean isBlocked() {return this.blocked;}
    public Color getBackgroundColor() {return this.backgroundColor;}
    public void setBackgroundColor(Color value) {this.backgroundColor = value;}

    public Tile(Map<String, String> tileData, int xPos, int yPos)
    {
    	super(tileData, xPos, yPos);
        backgroundColor = Application.stringToColor(tileData.get("backgroundColor"));
        blocked = Boolean.parseBoolean(tileData.get("blocked"));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}