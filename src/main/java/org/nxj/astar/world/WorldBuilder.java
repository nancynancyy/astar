package org.nxj.astar.world;

import org.nxj.astar.entities.Creature;
import org.nxj.astar.entities.Tile;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author nxj
 */
public class WorldBuilder {
	private final int width;
	private final int height;
	private final Tile[][] tiles;
	private final Map<String, Map<String, String>> tileData;
	private final Map<String, Map<String, String>> creatureData;
	private final Set<Creature> creatures;

	private static Random rand;

	static {
		try {
			rand = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public WorldBuilder(Map<String, Map<String, String>> tileData, Map<String, Map<String, String>> creatureData, int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new Tile[width][height];
		this.tileData = tileData;
		this.creatureData = creatureData;
		this.creatures = new HashSet<>();
	}
	
	public Tile createTile(String type, int x, int y) {
		return new Tile(tileData.get(type), x, y);
	}
	
	public Creature createCreature(String type, int x, int y) {
		return new Creature(creatureData.get(type), x, y);
	}
	
	public WorldBuilder fill(String tileType) {
		for (int x=0; x < width; x++) {
			for (int y=0; y < height; y++) {
				tiles[x][y] = new Tile(tileData.get(tileType), x, y);
			}
		}
		return this;
	}
	
	public WorldBuilder addBorders() {
		for (int x=0; x<width; x++) {
			tiles[x][0] = createTile("wall", x, 0);
			tiles[x][height-1] = createTile("wall", x, height-1);
		}
		
		for (int y=0; y<height; y++) {
			tiles[0][y] = createTile("wall", 0, y);
			tiles[width-1][y] = createTile("wall", width-1, y);
		}
		return this;
	}
	
	public WorldBuilder carveOutRoom(int topX, int topY, int width, int height) {
		for (int x=topX; x < topX+width; x++) {
			for (int y=topY; y < topY+height; y++) {
				tiles[x][y] = createTile("ground", x, y);
			}
		}
		return this;
	}
	
	public WorldBuilder populateWorld(int nrOfCreatures) {
		int rndX;
		int rndY;
		
		for (int i=0; i < nrOfCreatures; i++) {
			
			do {
				rndX = rand.nextInt(width);
				rndY = rand.nextInt(height);
			} while (tiles[rndX][rndY].isBlocked());
			
			List<String> creatureTypes = new ArrayList<>(creatureData.keySet());
			creatureTypes.remove("player");
			if (!creatureTypes.isEmpty()) {
				String creatureType = creatureTypes.get(rand.nextInt(creatureTypes.size()));
				creatures.add(createCreature(creatureType, rndX, rndY));
			}
		}
		
		return this;
	}
	
	public WorldBuilder createRandomWalkCave(int startX, int startY, int length) {
		int direction;
		int x = startX;
		int y = startY;
		
		for (int i=0; i<length; i++) {
			direction = rand.nextInt(4);
			if (direction == 0 && (x+1) < (width-1)) {
				x += 1;
			} else if (direction == 1 && (x-1) > 0) {
				x -= 1;
			} else if (direction == 2 && (y+1) < (height-1)) {
				y += 1;
			} else if (direction == 3 && (y-1) > 0) {
				y -= 1;
			}
			
			tiles[x][y] = createTile("ground", x, y);
		}

		return this;
	}
	
	public World build() {
		return new World(tiles, creatures);
	}

}