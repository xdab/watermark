package mini.xdab.utils;

import lombok.SneakyThrows;
import org.javatuples.Pair;
import org.javatuples.Tuple;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageUtils {

    @SneakyThrows
    public static BufferedImage loadFromFile(String fileName) {
        try { return ImageIO.read(new File(fileName)); }
        catch (IOException ioe) { }
        return null;
    }


    @SneakyThrows
    public static void saveToFile(BufferedImage img, String fileName) {
        File file;
        String format;

        if (fileName.contains(".")) {
            format = fileName.substring(1 + fileName.lastIndexOf("."));
            file = new File(fileName);
        } else {
            format = "png";
            file = new File(fileName + ".png");
        }

        try {
            ImageIO.write(img, format, file);
        } catch (IOException ioe) { }
    }

    public static Pair<Integer, Integer> getPositionFromHIndex(Image img, Integer hIndex) {
        int width = img.getWidth(null);
        return Pair.with(hIndex % width, hIndex / width);
    }

    public static Pair<Integer, Integer> getPositionFromVIndex(Image img, Integer vIndex) {
        int height = img.getHeight(null);
        return Pair.with(vIndex % height, vIndex / height);
    }

}
