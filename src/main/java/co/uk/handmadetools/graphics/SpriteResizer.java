package co.uk.handmadetools.graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SpriteResizer {

    int animationFrame = 0;
    int totalFrames = 6;

    private SpriteLoader spriteLoader = new SpriteLoader();

    public static void main(String[] args) {
        SpriteResizer spriteResizer = new SpriteResizer();
        spriteResizer.resize();
    }

    private void resize() {
        resize("bunny_right_1", "bunny_right_64_1");
        resize("bunny_right_2", "bunny_right_64_2");
        resize("bunny_right_3", "bunny_right_64_3");
        resize("bunny_right_4", "bunny_right_64_4");
        resize("bunny_right_5", "bunny_right_64_5");
        resize("bunny_right_6", "bunny_right_64_6");
    }

    private void resize(String in, String out) {
        URL url = this.getClass().getClassLoader().getResource("sprites" + File.separator + in + ".txt");
        try {
            File file = new File(url.toURI());
            List<String> lines = Files.readAllLines(file.toPath());
            StringBuilder sb = new StringBuilder();
            for (int y = 0; y < 32; y++) {
                StringBuilder line = new StringBuilder();
                for (int x = 0; x < 32; x++) {
                    if ((y < lines.size()) && (x < lines.get(y).length())) {
                        String rgbString = lines.get(y).substring(x, x + 1);
                        line.append(rgbString);
                        line.append(rgbString);
                    } else {
                        line.append("  ");
                    }
                }
                sb.append(line);
                sb.append("\n");
                sb.append(line);
                sb.append("\n");
            }
            Path path = Paths.get(
                    "src" + File.separator +
                            "main" + File.separator +
                            "resources" + File.separator +
                            "sprites" + File.separator
                            + out + ".txt");
            System.out.println(System.getProperty("user.dir"));
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(sb.toString());
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        SpriteTestFrame frame = new SpriteTestFrame();
        frame.setLocation(200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpriteTestPanel panel = new SpriteTestPanel();
        panel.setPreferredSize(new Dimension(100, 100));
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    animationFrame = animationFrame - 1;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    animationFrame = animationFrame + 1;
                }
                if (animationFrame < 0) {
                    animationFrame = totalFrames - 1;
                }
                if (animationFrame >= totalFrames) {
                    animationFrame = 0;
                }
                panel.invalidate();
                panel.repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        frame.setVisible(true);
    }

    class SpriteTestFrame extends JFrame {
    }

    class SpriteTestPanel extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponents(g);

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            int x = (getWidth() / 2);
            int y = (getHeight() / 2);
            BufferedImage image = spriteLoader.get("bunny_right_64_" + (animationFrame + 1));
            g.drawImage(image, x - 8 * 3, y - 8 * 3, 16 * 3, 16 * 3, null);
        }
    }
}
