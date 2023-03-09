package org.nxj.astar.world;

import org.nxj.astar.algorithm.AStar;
import org.nxj.astar.algorithm.ColorController;
import org.nxj.astar.algorithm.Node;
import org.nxj.astar.entities.Creature;
import org.nxj.astar.entities.Entity;
import org.nxj.astar.entities.Tile;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;

/**
 * @author nxj
 */
public class World {
    private final Tile[][] tiles;
    private final int width;
    private final int height;
    public Creature player;
    public Set<Creature> creatures;

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    private static Random rand;

    static {
        try {
            rand = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
    }

    public World(Tile[][] tiles, Set<Creature> creatures) {
        this.creatures = new HashSet<>();
        this.creatures.addAll(creatures);
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
    }

    public void addEntity(Creature creature) {
        this.creatures.add(creature);
    }

    public Tile tile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        } else {
            return tiles[x][y];
        }
    }

    public Tile getRandomWalkableTile() {
        while (true) {
            int x = rand.nextInt(0, width);
            int y = rand.nextInt(0, height);
            if (!tiles[x][y].isBlocked()) {
                return tiles[x][y];
            }
        }
    }

    public void setTileBackgroundColor(Color color, int x, int y) {
        if (x >= 0 && x <= width && y >= 0 && y <= height) {
            tiles[x][y].setBackgroundColor(color);
        }
    }

    public char glyph(int x, int y) {
        return tile(x, y).getGlyph();
    }

    public Color glyphColor(int x, int y) {
        return tile(x, y).getColor();
    }

    public Color backgroundColor(int x, int y) {
        return tile(x, y).getBackgroundColor();
    }

    public Entity getEntityAt(int x, int y) {
        return creatures.stream()
                .filter(entity -> entity.getX() == x && entity.getY() == y)
                .findFirst()
                .orElse(null);
    }

    public boolean isBlocked(int x, int y) {
        return (tiles[x][y].isBlocked() || getEntityAt(x, y) != null);
    }

    public void update() {
        //
    }

    public void startAStar(Node src, Node dst) {
        AStar aStar = new AStar(width, height, src, dst, (x, y) -> setTileBackgroundColor(Color.BLUE, x, y));
        List<Node> blocks = blocks();
        aStar.setBlocks(blocks);
        List<Node> path = aStar.findPath();
    }

    public List<Node> blocks() {
        List<Node> results = new ArrayList<>();
        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                if (tile.isBlocked()) {
                    results.add(new Node(tile.getX(), tile.getY()));
                }
            }
        }
        return results;
    }

    public Set<String> getTileTypesInArea(Rectangle rectangle) {
        Set<String> tileTypes = new HashSet<>();
        Tile tile;

        for (int y = (int) rectangle.getY(); y < rectangle.getMaxY(); y += 1) {
            for (int x = (int) rectangle.getX(); x < rectangle.getMaxX(); x += 1) {
                tile = this.tiles[x][y];
                if (tile != null) {
                    tileTypes.add(tile.getType());
                }
            }
        }
        return tileTypes;
    }

    public Set<String> getCreatureTypesInArea(Rectangle rectangle) {
        Set<String> creatureTypes = new HashSet<>();

        creatureTypes.add(player.getType());

        for (Creature creature : this.creatures) {
            if (creature.getX() > rectangle.getX() && creature.getX() < rectangle.getMaxX() &&
                    creature.getY() > rectangle.getY() && creature.getY() < rectangle.getMaxY()) {
                creatureTypes.add(creature.getType());
            }
        }

        return creatureTypes;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
