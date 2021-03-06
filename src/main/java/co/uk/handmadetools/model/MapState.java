package co.uk.handmadetools.model;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class MapState {

    private static final String[][] map = new String[Constants.X_SIZE][Constants.Y_SIZE];

    private static final String WALL = "Ww";

    public MapState() {
        loadMap();
    }

    private void loadMap() {
        try {
            URL url = this.getClass().getClassLoader().getResource("map.txt");
            File file = new File(url.toURI());
            List<String> lines = Files.readAllLines(file.toPath());
            for (int y = 0; y < Constants.Y_SIZE; y++) {
                for (int x = 0; x < Constants.X_SIZE; x++) {
                    if (x < lines.get(y).length()) {
                        map[x][y] = lines.get(y).substring(x, x + 1);
                    } else {
                        map[x][y] = " ";
                    }
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getWallType(int x, int y) {
        if (isWall(x, y)) {
            return map[x][y];
        }
        return " ";
    }

    public boolean isGhostDoor(int x, int y) {
        if (x < 0) {
            return false;
        }
        if (y < 0) {
            return false;
        }
        if (x >= Constants.X_SIZE) {
            return false;
        }
        if (y >= Constants.Y_SIZE) {
            return false;
        }
        return "7".equals(map[x][y]);
    }

    public boolean isWall(int x, int y) {
        if (x < 0) {
            return false;
        }
        if (y < 0) {
            return false;
        }
        if (x >= Constants.X_SIZE) {
            return false;
        }
        if (y >= Constants.Y_SIZE) {
            return false;
        }
        return WALL.contains(map[x][y]);
    }

    public boolean isPill(int x, int y) {
        if (x < 0) {
            return false;
        }
        if (y < 0) {
            return false;
        }
        if (x >= Constants.X_SIZE) {
            return false;
        }
        if (y >= Constants.Y_SIZE) {
            return false;
        }
        return ".".equals(map[x][y]);
    }

    public boolean isPowerPill(int x, int y) {
        if (x < 0) {
            return false;
        }
        if (y < 0) {
            return false;
        }
        if (x >= Constants.X_SIZE) {
            return false;
        }
        if (y >= Constants.Y_SIZE) {
            return false;
        }
        return "O".equals(map[x][y]);
    }
}
