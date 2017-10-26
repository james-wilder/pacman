package co.uk.handmadetools.ui;

import co.uk.handmadetools.GameEngine;
import co.uk.handmadetools.graphics.SpriteLoader;
import co.uk.handmadetools.model.Constants;
import co.uk.handmadetools.model.Drawable;
import co.uk.handmadetools.model.MapState;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.List;

public class PacManPanel extends JPanel {

    private SpriteLoader spriteMap;
    private static final int SCALE = 3;
    private GameEngine gameEngine;

    public PacManPanel(GameEngine gameEngine) {
        super.setBackground(Color.BLACK);
        this.gameEngine = gameEngine;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        Instant now = Instant.now();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        gameEngine = gameEngine.update(now);
        MapState state = gameEngine.getMapState();
        List<Drawable> drawables = gameEngine.getDrawables(now);

        for (int y = 0; y < Constants.Y_SIZE; y++) {
            for (int x = 0; x < Constants.X_SIZE; x++) {
                g.setColor(Color.GRAY);
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

        drawables.stream()
                .sorted((m1, m2) -> m2.getName().compareTo(m1.getName()))
                .forEach(moveable -> {
                    int gx = (int) (moveable.getX() * 8 * SCALE - 4 * SCALE);
                    int gy = (int) (moveable.getY() * 8 * SCALE - 4 * SCALE);
                    BufferedImage image = spriteMap.get(moveable.getName());
                    g.drawImage(image, gx, gy, 2 * 8 * SCALE, 2 * 8 * SCALE, null);
                    if (moveable.getName().startsWith("ghost")) {
                        BufferedImage eyes = null;
                        if (moveable.getXFacing() < 0) {
                            eyes = spriteMap.get("ghost_eyes_left");
                        }
                        if (moveable.getXFacing() > 0) {
                            eyes = spriteMap.get("ghost_eyes_right");
                        }
                        if (moveable.getYFacing() < 0) {
                            eyes = spriteMap.get("ghost_eyes_up");
                        }
                        if (moveable.getYFacing() > 0) {
                            eyes = spriteMap.get("ghost_eyes_down");
                        }
                        if (eyes != null) {
                            g.drawImage(eyes, gx, gy, 2 * 8 * SCALE, 2 * 8 * SCALE, null);
                        }
                    }
                });
    }

    public void setSpriteMap(SpriteLoader spriteMap) {
        this.spriteMap = spriteMap;
    }

}
