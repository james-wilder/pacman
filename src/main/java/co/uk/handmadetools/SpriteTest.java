package co.uk.handmadetools;

import co.uk.handmadetools.graphics.SpriteLoader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class SpriteTest {

    int animationFrame = 0;
    int totalFrames = 6;

    private SpriteLoader spriteLoader = new SpriteLoader();

    public static void main(String[] args) {
        SpriteTest spriteTest = new SpriteTest();
        spriteTest.start();
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
            BufferedImage image = spriteLoader.get("bunny_right_" + (animationFrame + 1));
            g.drawImage(image, x - 8 * 3, y - 8 * 3, 16 * 3, 16 * 3, null);
        }
    }
}
