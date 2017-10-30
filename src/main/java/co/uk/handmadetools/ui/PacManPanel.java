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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PacManPanel extends JPanel {

    public static final List<String> MONTHS = Arrays.asList("spring"); // , "summer", "autumn", "winter");

    private SpriteLoader spriteLoader;
    private static final int SCALE = 3;
    private GameEngine gameEngine;
    public static final int BUNNY_FRAMES = 6;
    private Random random = new Random();

    private Map<String, BufferedImage> backgrounds = new HashMap<>();

    public PacManPanel() {
        super.setBackground(Color.BLACK);
        paintBackgrounds();
    }

    private synchronized void paintBackgrounds() {
        int xSize = Constants.X_SIZE * SCALE * 8;
        int ySize = Constants.Y_SIZE * SCALE * 8;
        Color bgColor = new Color(0, 200, 0);

        BufferedImage bg = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
        Graphics bgG = bg.getGraphics();
        bgG.setColor(bgColor);
        bgG.fillRect(0, 0, xSize, ySize);

        MapState state = new MapState();
        for (int y = 0; y < Constants.Y_SIZE; y++) {
            for (int x = 0; x < Constants.X_SIZE; x++) {
                bgG.setColor(new Color(0, 0, 0));
                int extra = 16;
                if (!state.isWall(x, y)) {
                    bgG.fillRect(x * 8 * SCALE - extra, y * 8 * SCALE - extra, 8 * SCALE + 2 * extra, 8 * SCALE + 2 * extra);
                }
            }
        }

        for (String month : MONTHS) {
            BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            g.drawImage(bg, 0, 0, null);
            backgrounds.put(month, image);
        }

        for (int y = 1; y < ySize; y = y + 2) {
            for (int x = 0; x < xSize; x++) {
                if (random.nextDouble() < 0.6) {
                    int rgb = bg.getRGB(x, y);
                    double x2 = (double) 5 * (random.nextDouble() - 0.5);
                    double y2 = 6 - random.nextInt(2) - x2 * x2 / 5;
                    Color c = new Color(0, 200, 0);
                    if (y % 12 < 6) {
                        c = new Color(0, 180, 0);
                    }
                    if (rgb == bgColor.getRGB()) {
                        for (String month : MONTHS) {
                            BufferedImage image = backgrounds.get(month);
                            Graphics g = image.getGraphics();
                            g.setColor(c);
                            g.drawLine(x - 1, y, (int) (x + x2), (int) (y - y2));
                            g.drawLine(x, y, (int) (x + x2), (int) (y - y2));
                            g.drawLine(x + 1, y, (int) (x + x2), (int) (y - y2));
                            backgrounds.put(month, image);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        Instant now = Instant.now();

        gameEngine = gameEngine.update(now);
        MapState state = gameEngine.getMapState();
        List<Drawable> drawables = gameEngine.getDrawables(now);

        BufferedImage bg = backgrounds.get("spring");
        g.drawImage(bg, 0, 0, Constants.X_SIZE * SCALE * 8, Constants.Y_SIZE * SCALE * 8, null);

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
                        int frame = (int) (BUNNY_FRAMES * (moveable.getPosition().getX() / 2 - Math.floor(moveable.getPosition().getX() / 2)));
                        String dir = "right";
                        if (moveable.getSpeed().getVx() < 0) {
                            dir = "left";
                            frame = BUNNY_FRAMES - frame - 1;
                        }
                        BufferedImage image = spriteLoader.get(moveable.getName() + "_" + dir + "_64_" + (frame + 1));
                        g.drawImage(image, gx - 8, gy - 8, 64, 64, null);
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

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }
}
