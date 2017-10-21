package co.uk.handmadetools;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PacManPanel extends JPanel {

    private MapState state;
    private SpriteLoader spriteMap;
    private static final int SCALE = 3;

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
//                g.drawRect(x * 24, y * 24, 24, 24);

                String wall = state.getWallType(x, y);

                BufferedImage sprite = spriteMap.get(wall);
                if (sprite != null) {
                    g.drawImage(sprite, x * 8 * SCALE, y * 8 * SCALE, 8 * SCALE, 8 * SCALE, null);
                }
                if (state.isPill(x, y)) {
                    BufferedImage pill = spriteMap.get("pill");
                    g.drawImage(pill, x * 8 * SCALE, y * 8 * SCALE, 8 * SCALE, 8 * SCALE, null);
                }
                if (state.isPowerPill(x, y)) {
                    BufferedImage pill = spriteMap.get("power_pill");
                    g.drawImage(pill, x * 8 * SCALE, y * 8 * SCALE, 8 * SCALE, 8 * SCALE, null);
                }
            }
        }

        float ghostX = 13;
        float ghostY = 10.5f;
        BufferedImage pill = spriteMap.get("ghost_1");
        g.drawImage(pill, (int)(ghostX * 8 * SCALE), (int)(ghostY * 8 * SCALE), 2 * 8 * SCALE, 2 * 8 * SCALE, null);
        BufferedImage eyes = spriteMap.get("ghost_eyes_left");
        g.drawImage(eyes, (int)(ghostX * 8 * SCALE), (int)(ghostY * 8 * SCALE), 2 * 8 * SCALE, 2 * 8 * SCALE, null);
    }

    public void setSpriteMap( SpriteLoader spriteMap) {
        this.spriteMap = spriteMap;
    }
}
