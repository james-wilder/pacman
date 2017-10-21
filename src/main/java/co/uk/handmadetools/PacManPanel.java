package co.uk.handmadetools;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class PacManPanel extends JPanel {

    private MapState state;
    private Map<String, BufferedImage> spriteMap;


    public PacManPanel() {
        super.setBackground(Color.BLACK);
    }

    public void setState(MapState state) {
        this.state = state;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        for (int y = 0; y < Constants.Y_SIZE; y++) {
            for (int x = 0; x < Constants.X_SIZE; x++) {
                StringBuffer walls = new StringBuffer();
                walls.append(state.isWall(x - 1, y - 1) ? "W" : " ");
                walls.append(state.isWall(x, y - 1) ? "W" : " ");
                walls.append(state.isWall(x + 1, y - 1) ? "W" : " ");
                walls.append(state.isWall(x - 1, y) ? "W" : " ");
                walls.append(state.isWall(x, y) ? "W" : " ");
                walls.append(state.isWall(x + 1, y) ? "W" : " ");
                walls.append(state.isWall(x - 1, y + 1) ? "W" : " ");
                walls.append(state.isWall(x, y + 1) ? "W" : " ");
                walls.append(state.isWall(x + 1, y + 1) ? "W" : " ");

                String spriteName = null;
                String wallLookup = walls.toString();
                if ("    WW W ".equals(wallLookup)) {
                    spriteName = "wall_thin_nw";
                }
                if ("   WW  W ".equals(wallLookup)) {
                    spriteName = "wall_thin_ne";
                }
                if (" W  WW   ".equals(wallLookup)) {
                    spriteName = "wall_thin_sw";
                }
                if (" W WW    ".equals(wallLookup)) {
                    spriteName = "wall_thin_se";
                }
                if (spriteName != null) {
                    BufferedImage sprite = spriteMap.get(spriteName);
                    g.drawImage(sprite, x * 24, y * 24, null);
                }
            }
        }
    }

    public void setSpriteMap(Map<String, BufferedImage> spriteMap) {
        this.spriteMap = spriteMap;
    }
}
