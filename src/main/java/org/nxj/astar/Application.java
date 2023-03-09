package org.nxj.astar;

import org.nxj.astar.algorithm.Node;
import org.nxj.astar.entities.Creature;
import org.nxj.astar.entities.Tile;
import org.nxj.astar.ui.Interface;
import org.nxj.astar.world.World;
import org.nxj.astar.world.WorldBuilder;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Application {

    private boolean isRunning;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int TIME_PER_LOOP = 1000000000 / FRAMES_PER_SECOND;

    private World world;
    private Creature player;

    private final Map<String, Map<String, String>> creatureData;
    private final Map<String, Map<String, String>> tileData;

    private int screenWidth;
    private int screenHeight;

    private final Rectangle gameViewArea;

    private static final int MAP_WIDTH = 100;
    private static final int MAP_HEIGHT = 100;

    private final Interface ui;


    public Application(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        gameViewArea = new Rectangle(screenWidth, screenHeight - 5);

        ui = new Interface(screenWidth, screenHeight, new Rectangle(MAP_WIDTH, MAP_HEIGHT));

        creatureData = loadData("/creatures.txt");
        tileData = loadData("/tiles.txt");

        createWorld();
    }

    public Map<String, Map<String, String>> loadData(String filename) {
        BufferedReader br = null;
        URL res = getClass().getResource(filename);
        if ("jar".equals(res.getProtocol())) {
            InputStream input = getClass().getResourceAsStream(filename);
            InputStreamReader reader = new InputStreamReader(input);
            br = new BufferedReader(reader);
        } else {
            try {
                br = new BufferedReader(new FileReader(res.getFile()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return parseData(br);
    }

    private Map<String, Map<String, String>> parseData(BufferedReader br){
        try {
            Map<String, Map<String, String>> entityMap = new HashMap<>();
            String line;
            String[] attributeNames = new String[10];
            line = br.readLine();

            if (line != null) {
                attributeNames = line.split(", ");
            }

            while ((line = br.readLine()) != null) {
                String[] data = line.split(", ");
                Map<String, String> entityData = new HashMap<>();

                for (int i = 0; i < attributeNames.length; i++) {
                    entityData.put(attributeNames[i], data[i]);
                }

                String name = data[1];
                entityMap.put(name, entityData);
            }
            return entityMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    private void createWorld() {
        player = new Creature(creatureData.get("player"), 50, 50);
        world = new WorldBuilder(tileData, creatureData, MAP_WIDTH, MAP_HEIGHT)
                .fill("wall")
                .createRandomWalkCave(50, 50, 6000)
                .populateWorld(10)
                .build();
        world.player = player;
        world.addEntity(player);
    }

    public void processInput() {
        InputEvent event = ui.getNextInput();
        if (event instanceof KeyEvent keypress) {
            switch (keypress.getKeyCode()) {
                case KeyEvent.VK_LEFT, KeyEvent.VK_4 -> player.move(world, -1, 0);
                case KeyEvent.VK_RIGHT, KeyEvent.VK_6 -> player.move(world, 1, 0);
                case KeyEvent.VK_UP, KeyEvent.VK_8 -> player.move(world, 0, -1);
                case KeyEvent.VK_DOWN, KeyEvent.VK_2 -> player.move(world, 0, 1);
                case KeyEvent.VK_7 -> player.move(world, -1, -1);
                case KeyEvent.VK_9 -> player.move(world, 1, -1);
                case KeyEvent.VK_1 -> player.move(world, -1, 1);
                case KeyEvent.VK_3 -> player.move(world, 1, 1);
                case KeyEvent.VK_SPACE -> {
                    Tile dst = world.getRandomWalkableTile();
                    world.setTileBackgroundColor(Color.GREEN, dst.getX(), dst.getY());
                    world.startAStar(
                            new Node(player.getX(), player.getY()),
                            new Node(dst.getX(), dst.getY())
                            );
                }
                default -> {
                    //
                }
            }
//            for (Tile[] nodes: world.getTiles()){
//                for (Tile node: nodes){
//                    if (player.getX() == node.getX() && player.getY() == node.getY()){
//                        System.out.print("@");
//                    } else {
//                        System.out.print((node.isBlocked() ? "B" : "."));
//                    }
//                }
//                System.out.println();
//            }
        }
    }

    public void render() {
        ui.pointCameraAt(world, player.getX(), player.getY());
        ui.drawDynamicLegend(gameViewArea, world, tileData, creatureData);
        ui.refresh();
    }

    public void update() {
        world.update();
    }

    public void run() {
        isRunning = true;

        while (isRunning) {
            long startTime = System.nanoTime();

            processInput();
            update();
            render();

            long endTime = System.nanoTime();

            long sleepTime = TIME_PER_LOOP - (endTime - startTime);

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Application game = new Application(80, 50);
        game.run();
    }

    public static Color stringToColor(String colorString) {
        Color color;
        try {
            Field field = Color.class.getField(colorString);
            color = (Color) field.get(null);
        } catch (Exception e) {
            color = null; // Not defined
        }
        return color;
    }

}
