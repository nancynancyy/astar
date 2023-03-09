package org.nxj.astar.graphics;

import asciiPanel.AsciiPanel;
import org.nxj.astar.entities.Creature;
import org.nxj.astar.entities.Tile;
import org.nxj.astar.world.World;

import java.awt.*;

/**
 * @author nxj
 */
public class AsciiCamera {

    int screenWidth;
    int screenHeight;

    int mapWidth;
    int mapHeight;

    public AsciiCamera(Rectangle bounds, Rectangle viewArea)
    {
        screenWidth = viewArea.width;
        screenHeight = viewArea.height;
    	
        mapWidth = bounds.width;
        mapHeight = bounds.height;
    }

    
    public Point getCameraOrigin(int xFocus, int yFocus)
    {
        int spx = Math.max(0, Math.min(xFocus - screenWidth / 2, mapWidth - screenWidth));
        int spy = Math.max(0, Math.min(yFocus - screenHeight / 2, mapHeight - screenHeight));
        return new Point(spx, spy);
    }

    public void lookAt(AsciiPanel terminal, World world, int xFocus, int yFocus)
    {
        Tile tile;
        Point origin;
        
        origin = getCameraOrigin(xFocus, yFocus);
        
		for (int x = 0; x < screenWidth; x++){
			for (int y = 0; y < screenHeight; y++){
                tile = world.tile(origin.x + x, origin.y + y);
                terminal.write(tile.getGlyph(), x, y, tile.getColor(), tile.getBackgroundColor());
            }
        }
        
		int spx;
		int spy;
        for(Creature entity : world.creatures)
        {
        	spx = entity.getX() - origin.x;
        	spy = entity.getY() - origin.y;
        	
        	if ((spx >= 0 && spx < screenWidth) && (spy >= 0 && spy < screenHeight)) {
        		terminal.write(entity.getGlyph(), spx, spy, entity.getColor(), world.tile(entity.getX(), entity.getY()).getBackgroundColor());
        	}
        }
    }
}
