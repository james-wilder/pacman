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

    private SpriteLoader spriteLoader;
    private static final int SCALE = 3;
    private GameEngine gameEngine;
    public static final int BUNNY_FRAMES = 6;

    public PacManPanel(GameEngine gameEngine) {
        super.setBackground(Color.BLACK);
        this.gameEngine = gameEngine;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        Instant now = Instant.now();

        g.setColor(new Color(112, 79, 63));
        g.fillRect(0, 0, getWidth(), getHeight());

        gameEngine = gameEngine.update(now);
        MapState state = gameEngine.getMapState();
        List<Drawable> drawables = gameEngine.getDrawables(now);

        for (int y = 0; y < Constants.Y_SIZE; y++) {
            for (int x = 0; x < Constants.X_SIZE; x++) {
                g.setColor(Color.BLACK);
                int extra = 16;
                if (!state.isWall(x, y)) {
                    g.fillRect(x * 8 * SCALE - extra, y * 8 * SCALE - extra, 8 * SCALE + 2 * extra, 8 * SCALE + 2 * extra);
                }
            }
        }

        for (int y = 0; y < Constants.Y_SIZE; y++) {
            for (int x = 0; x < Constants.X_SIZE; x++) {
                if (state.isPill(x, y)) {
                    BufferedImage pill = spriteLoader.get("pill");
                    g.drawImage(pill, x * 8 * SCALE, y * 8 * SCALE, 8 * SCALE, 8 * SCALE, null);
                }
                if (state.isPowerPill(x, y)) {
                    BufferedImage pill = spriteLoader.get("power_pill");
                    g.drawImage(pill, x * 8 * SCALE, y * 8 * SCALE, 8 * SCALE, 8 * SCALE, null);
                }
            }
        }

        drawables.stream()
                .sorted((m1, m2) -> m2.getName().compareTo(m1.getName()))
                .forEach(moveable -> {
                    int gx = (int) (moveable.getPosition().getX() * 8 * SCALE - 4 * SCALE);
                    int gy = (int) (moveable.getPosition().getY() * 8 * SCALE - 4 * SCALE);
                    if ("bunny".equals(moveable.getName())) {
                        int frame = (int) (BUNNY_FRAMES * (moveable.getPosition().getX() - Math.floor(moveable.getPosition().getX())));
                        String dir = "right";
                        if (moveable.getSpeed().getVx() < 0) {
                            dir = "left";
                            frame = BUNNY_FRAMES - frame - 1;
                        }
                        BufferedImage image = spriteLoader.get(moveable.getName() + "_" + dir + "_" + (frame + 1));
                        g.drawImage(image, gx, gy, 2 * 8 * SCALE, 2 * 8 * SCALE, null);
                    } else {
                        BufferedImage image = spriteLoader.get(moveable.getName());
                        g.drawImage(image, gx, gy, 2 * 8 * SCALE, 2 * 8 * SCALE, null);
                        if (moveable.getName().startsWith("ghost")) {
                            BufferedImage eyes = null;
                            if (moveable.getSpeed().getVx() < 0) {
                                eyes = spriteLoader.get("ghost_eyes_left");
                            }
                            if (moveable.getSpeed().getVx() > 0) {
                                eyes = spriteLoader.get("ghost_eyes_right");
                            }
                            if (moveable.getSpeed().getVy() < 0) {
                                eyes = spriteLoader.get("ghost_eyes_up");
                            }
                            if (moveable.getSpeed().getVy() > 0) {
                                eyes = spriteLoader.get("ghost_eyes_down");
                            }
                            if (eyes != null) {
                                g.drawImage(eyes, gx, gy, 2 * 8 * SCALE, 2 * 8 * SCALE, null);
                            }
                        }
                    }
                });
    }

    public void setSpriteLoader(SpriteLoader spriteLoader) {
        this.spriteLoader = spriteLoader;
    }

}
