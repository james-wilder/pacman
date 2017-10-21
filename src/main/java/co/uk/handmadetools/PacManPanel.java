package co.uk.handmadetools;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Instant;

public class PacManPanel extends JPanel {

    private MapState state;
    private SpriteLoader spriteMap;
    private static final int SCALE = 3;
    private GameState gameState;

    public PacManPanel() {
        super.setBackground(Color.BLACK);

        Runnable runnable = () -> {
            while (true) {
                invalidate();
                repaint();
                revalidate();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void setState(MapState state) {
        this.state = state;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        GameState drawGameState = gameState;
        Instant now = Instant.now();

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

        drawGameState.getMoveables().forEach(moveable -> {
            BufferedImage pill = spriteMap.get(moveable.getName());
            g.drawImage(pill, (int)(moveable.getX(now) * 8 * SCALE), (int)(moveable.getY(now) * 8 * SCALE), 2 * 8 * SCALE, 2 * 8 * SCALE, null);
            if (moveable.getName().startsWith("ghost")) {
                if (moveable.getXs() < 0) {
                    BufferedImage eyes = spriteMap.get("ghost_eyes_left");
                    g.drawImage(eyes, (int) (moveable.getX(now) * 8 * SCALE), (int) (moveable.getY(now) * 8 * SCALE), 2 * 8 * SCALE, 2 * 8 * SCALE, null);
                }
                if (moveable.getXs() > 0) {
                    BufferedImage eyes = spriteMap.get("ghost_eyes_right");
                    g.drawImage(eyes, (int) (moveable.getX(now) * 8 * SCALE), (int) (moveable.getY(now) * 8 * SCALE), 2 * 8 * SCALE, 2 * 8 * SCALE, null);
                }
                if (moveable.getYs() < 0) {
                    BufferedImage eyes = spriteMap.get("ghost_eyes_up");
                    g.drawImage(eyes, (int) (moveable.getX(now) * 8 * SCALE), (int) (moveable.getY(now) * 8 * SCALE), 2 * 8 * SCALE, 2 * 8 * SCALE, null);
                }
                if (moveable.getYs() > 0) {
                    BufferedImage eyes = spriteMap.get("ghost_eyes_down");
                    g.drawImage(eyes, (int) (moveable.getX(now) * 8 * SCALE), (int) (moveable.getY(now) * 8 * SCALE), 2 * 8 * SCALE, 2 * 8 * SCALE, null);
                }
            }
        });
    }

    public void setSpriteMap( SpriteLoader spriteMap) {
        this.spriteMap = spriteMap;
    }

    public void update(GameState gameState) {
        this.gameState = gameState;
    }
}
