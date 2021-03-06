package co.uk.handmadetools.graphics;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpriteLoader {

    public static final int IMAGE_TYPE = BufferedImage.TYPE_INT_ARGB;
    private Map<String, BufferedImage> map = new HashMap<>();

    public SpriteLoader() {
        loadSprite("pill", 8, 8);
        loadSprite("power_pill", 8, 8);

        loadSprite("ghost_1", 16, 16);
        loadSprite("ghost_2", 16, 16);
        loadSprite("ghost_3", 16, 16);
        loadSprite("ghost_4", 16, 16);
        loadSprite("ghost_eyes_left", 16, 16);
        loadSprite("ghost_eyes_right", 16, 16);
        loadSprite("ghost_eyes_up", 16, 16);
        loadSprite("ghost_eyes_down", 16, 16);
        loadSprite("pacman", 16, 16);

        loadSprite("bunny_right_64_1", 64, 64);
        loadSprite("bunny_right_64_2", 64, 64);
        loadSprite("bunny_right_64_3", 64, 64);
        loadSprite("bunny_right_64_4", 64, 64);
        loadSprite("bunny_right_64_5", 64, 64);
        loadSprite("bunny_right_64_6", 64, 64);

        flipX("bunny_right_64_1", "bunny_left_64_1");
        flipX("bunny_right_64_2", "bunny_left_64_2");
        flipX("bunny_right_64_3", "bunny_left_64_3");
        flipX("bunny_right_64_4", "bunny_left_64_4");
        flipX("bunny_right_64_5", "bunny_left_64_5");
        flipX("bunny_right_64_6", "bunny_left_64_6");
    }

    private void flipX(String source, String dest) {
        BufferedImage image = get(source);
        AffineTransform affineTransform = AffineTransform.getScaleInstance(-1, 1);
        affineTransform.translate(-image.getWidth(null), 0);
        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = affineTransformOp.filter(image, null);
        map.put(dest, image);
    }

    public BufferedImage get(String filename) {
        return map.get(filename);
    }

    private void loadSprite(String filename, int xSize, int ySize) {
        URL url = this.getClass().getClassLoader().getResource("sprites" + File.separator + filename + ".txt");
        File file = null;
        BufferedImage image = new BufferedImage(xSize, ySize, IMAGE_TYPE);
        try {
            file = new File(url.toURI());
            List<String> lines = Files.readAllLines(file.toPath());
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    if ((y < lines.size()) && (x < lines.get(y).length())) {
                        String rgbString = lines.get(y).substring(x, x + 1);
                        if ("01234567".contains(rgbString)) {
                            if (!" ".equals(rgbString)) {
                                int rgb = Integer.valueOf(rgbString);
                                int r = (rgb & 1) != 0 ? 255 : 0;
                                int g = (rgb & 2) != 0 ? 255 : 0;
                                int b = (rgb & 4) != 0 ? 255 : 0;
                                Color c = new Color(r, g, b);
                                image.setRGB(x, y, c.getRGB());
                            }
                        } else {
                            if ("8".equals(rgbString)) {
                                Color c = new Color(255, 184, 222);
                                image.setRGB(x, y, c.getRGB());
                            }
                            if ("O".equals(rgbString)) {
                                Color c = new Color(255, 160, 0);
                                image.setRGB(x, y, c.getRGB());
                            }
                            if ("o".equals(rgbString)) {
                                Color c = new Color(192, 120, 0);
                                image.setRGB(x, y, c.getRGB());
                            }
                            if ("C".equals(rgbString)) {
                                Color c = new Color(0, 192, 255);
                                image.setRGB(x, y, c.getRGB());
                            }
                            if ("G".equals(rgbString)) {
                                Color c = new Color(192, 192, 192);
                                image.setRGB(x, y, c.getRGB());
                            }
                            if ("g".equals(rgbString)) {
                                Color c = new Color(128, 128, 128);
                                image.setRGB(x, y, c.getRGB());
                            }
                        }
                    }
                }
            }
            map.put(filename, image);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
