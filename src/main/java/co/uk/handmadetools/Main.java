package co.uk.handmadetools;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    private static final int IMAGE_TYPE = BufferedImage.TYPE_INT_ARGB;

    MapState map = new MapState();

    public Main() throws IOException, URISyntaxException {
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        Main main = new Main();
        main.run();
    }

    public void run() {
        System.out.println("Load sprites");

        GameEngine gameEngine = new GameEngine();

        PacManFrame frame = new PacManFrame();
        frame.setLocation(200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PacManPanel panel = new PacManPanel();
        panel.setPreferredSize(new Dimension(24 * Constants.X_SIZE, 24 * Constants.Y_SIZE));
        panel.setSpriteMap(new SpriteLoader());
        panel.setState(map);
        panel.update(gameEngine.getGameState());
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

}
