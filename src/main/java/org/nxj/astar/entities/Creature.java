package org.nxj.astar.entities;

import org.nxj.astar.world.World;

import java.util.Map;

/**
 * @author nxj
 */
public class Creature extends Entity {
    
    public Creature(Map<String, String> creatureData, int x, int y)
    {
        super(creatureData, x, y);
    }
 
    public void move(World world, int dx, int dy)
    {
        if (!world.isBlocked(x + dx, y + dy))
        {
            x += dx;
            y += dy;
        }
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
