package co.uk.handmadetools;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class MapState {

    private static final String[][] map = new String[Constants.X_SIZE][Constants.Y_SIZE];

    public MapState() throws IOException, URISyntaxException {
        loadMap();
    }

    private void loadMap() throws URISyntaxException, IOException {
        URL url = this.getClass().getClassLoader().getResource("map.txt");
        File file = new File(url.toURI());
        List<String> lines = Files.readAllLines(file.toPath());
        for (int y = 0; y < Constants.Y_SIZE; y++) {
            for (int x = 0; x < Constants.X_SIZE; x++) {
                map[x][y] = lines.get(y).substring(x, x + 1);
            }
        }
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
        return "W".equals(map[x][y]);
    }
}
