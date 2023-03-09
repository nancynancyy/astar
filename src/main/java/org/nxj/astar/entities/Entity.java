package org.nxj.astar.entities;

import org.nxj.astar.Application;

import java.awt.*;
import java.util.Map;
import java.util.Objects;

/**
 * @author nxj
 */
public class Entity {

    protected int x;
    protected int y;

	protected char glyph;
    protected String type;
    protected Color color;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getGlyph() {
        return this.glyph;
    }

    public String getType() {
        return this.type;
    }

    public Color getColor() {
        return this.color;
    }

    public Entity(Map<String, String> entityData, int xPos, int yPos) {
        x = xPos;
        y = yPos;
        type = entityData.get("name");
        glyph = entityData.get("glyph").charAt(0);
        color = Application.stringToColor(entityData.get("color"));
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
        Entity other = (Entity) otherObject;
        return getX() == other.getX() &&
                getY() == other.getY() &&
				getGlyph() == other.getGlyph() &&
                Objects.equals(getType(), other.getType()) &&
				Objects.equals(getColor(),other.getColor())
                ;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + getX();
		result = 31 * result + getY();
		result = 31 * result + getGlyph();
		result = 31 * result + (getType() == null ? 0 : getType().hashCode());
        result = 31 * result + (getColor() == null ? 0 : getColor().hashCode());
        return result;
    }

}
