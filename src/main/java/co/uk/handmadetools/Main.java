package co.uk.handmadetools;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, BufferedImage> spriteMap = createSprites();

        PacManFrame frame = new PacManFrame();
        frame.setLocation(200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PacManPanel panel = new PacManPanel();
        panel.setPreferredSize(new Dimension(24 * Constants.X_SIZE, 24 * Constants.Y_SIZE));
        panel.setSpriteMap(new SpriteLoader());
        panel.setState(map);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public Map<String, BufferedImage> createSprites() {
        Map<String, BufferedImage> rtn = new HashMap<>();

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("spritemap-384.png");
        try {
            BufferedImage image = ImageIO.read(is);
            System.out.println("Got it");

            saveSubImage(image, 12, 16, 0, "pill", rtn);
            saveSubImage(image, 12, 18, 0, "power_pill", rtn);
            saveSubImage(image, 24, 0, 5, "cherry", rtn);

            BufferedImage wallOuterNwSmall = getSubImage(image, 12, 17, 4);
            BufferedImage wallOuterNeSmall = getSubImage(image, 12, 16, 4);
            BufferedImage wallOuterSwSmall = getSubImage(image, 12, 21, 4);
            BufferedImage wallOuterSeSmall = getSubImage(image, 12, 20, 4);

            BufferedImage wallOuterNSmall = getSubImage(image, 12, 26, 4);
            BufferedImage wallOuterSSmall = getSubImage(image, 12, 28, 4);
            BufferedImage wallOuterWSmall = getSubImage(image, 12, 19, 4);
            BufferedImage wallOuterESmall = getSubImage(image, 12, 18, 4);

//            saveCompositeImage(wallOuterNwSmall, wallOuterNSmall, wallOuterWSmall, null, "wall_thin_nw", rtn);
//            saveCompositeImage(wallOuterNSmall, wallOuterNeSmall, null, wallOuterESmall, "wall_thin_ne", rtn);
//            saveCompositeImage(wallOuterWSmall, null, wallOuterSwSmall, wallOuterSSmall, "wall_thin_sw", rtn);
//            saveCompositeImage(null, wallOuterESmall, wallOuterSSmall, wallOuterSeSmall, "wall_thin_se", rtn);
//            saveCompositeImage(wallOuterNSmall, wallOuterNSmall, null, null, "wall_thin_n", rtn);
//            saveCompositeImage(null, null, wallOuterSSmall, wallOuterSSmall, "wall_thin_s", rtn);
//            saveCompositeImage(wallOuterWSmall, null, wallOuterWSmall, null, "wall_thin_w", rtn);
//            saveCompositeImage(null, wallOuterESmall, null, wallOuterESmall, "wall_thin_e", rtn);

//            saveSubImage(image, 24, 0, 5, "wall_ns", rtn);
//            saveSubImage(image, 24, 0, 4, "wall_we", rtn);

            BufferedImage wallInnerNwSmall = getSubImage(image, 12, 8, 8);
            BufferedImage wallInnerNeSmall = getSubImage(image, 12, 11, 8);
            BufferedImage wallInnerSwSmall = getSubImage(image, 12, 4, 9);
            BufferedImage wallInnerSeSmall = getSubImage(image, 12, 7, 9);

            BufferedImage wallInnerNSmall = getSubImage(image, 12, 9, 8);
            BufferedImage wallInnerSSmall = getSubImage(image, 12, 5, 9);
            BufferedImage wallInnerWSmall = getSubImage(image, 12, 8, 9);
            BufferedImage wallInnerESmall = getSubImage(image, 12, 11, 9);

            saveCompositeImage(wallInnerNwSmall, wallInnerNSmall, wallInnerWSmall, null, "wall_nw", rtn);
            saveCompositeImage(wallInnerNSmall, wallInnerNeSmall, null, wallInnerESmall, "wall_ne", rtn);
            saveCompositeImage(wallInnerWSmall, null, wallInnerSwSmall, wallInnerSSmall, "wall_sw", rtn);
            saveCompositeImage(null, wallInnerESmall, wallInnerSSmall, wallInnerSeSmall, "wall_se", rtn);

            saveCompositeImage(null, null, wallInnerNSmall, wallInnerNSmall, "wall_n", rtn);
            saveCompositeImage(wallInnerSSmall, wallInnerSSmall, null, null, "wall_s", rtn);
            saveCompositeImage(null, wallInnerWSmall, null, wallInnerWSmall, "wall_w", rtn);
            saveCompositeImage(wallInnerESmall, null, wallInnerESmall, null, "wall_e", rtn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rtn;
    }

    private void saveCompositeImage(BufferedImage nw, BufferedImage ne, BufferedImage sw, BufferedImage se, String filename, Map<String, BufferedImage> rtn) {
        BufferedImage image = new BufferedImage(24, 24, IMAGE_TYPE);
        Graphics g = image.getGraphics();
        if (nw != null) {
            g.drawImage(nw, 0, 0, null);
        }
        if (ne != null) {
            g.drawImage(ne, 12, 0, null);
        }
        if (sw != null) {
            g.drawImage(sw, 0, 12, null);
        }
        if (se != null) {
            g.drawImage(se, 12, 12, null);
        }
        saveImage(image, filename);
        rtn.put(filename, image);
    }

    private BufferedImage getSubImage(BufferedImage source, int gridSize, int x, int y) {
        return source.getSubimage(x * gridSize, y * gridSize, gridSize, gridSize);
    }

    private void saveSubImage(BufferedImage source, int gridSize, int x, int y, String filename, Map<String, BufferedImage> rtn) {
        BufferedImage subImage = getSubImage(source, gridSize, x, y);
        saveImage(subImage, filename);
        rtn.put(filename, subImage);
    }

    private void saveImage(BufferedImage image, String filename) {
        try {
            ImageIO.write(image, "png", new File(
                    "src" + File.separator +
                            "main" + File.separator +
                            "resources" + File.separator +
                            filename
                            + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
