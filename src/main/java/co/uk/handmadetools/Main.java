package co.uk.handmadetools;

import co.uk.handmadetools.graphics.SpriteLoader;
import co.uk.handmadetools.model.Constants;
import co.uk.handmadetools.ui.KeyState;
import co.uk.handmadetools.ui.PacManFrame;
import co.uk.handmadetools.ui.PacManPanel;

import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public void run() {
        System.out.println("Load sprites");

        GameEngine gameEngine = new GameEngine();

        PacManFrame frame = new PacManFrame();
        frame.setLocation(200, 200);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        PacManPanel panel = new PacManPanel(gameEngine);
        panel.setPreferredSize(new Dimension(24 * Constants.X_SIZE, 24 * Constants.Y_SIZE));
        panel.setSpriteLoader(new SpriteLoader());
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(new KeyState());

        Runnable runnable = () -> {
            while (true) {
                panel.invalidate();
                panel.repaint();
                panel.revalidate();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

    }

}
