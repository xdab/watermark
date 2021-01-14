package mini.xdab;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class ImageUtils {

    @SneakyThrows
    public static BufferedImage loadFromFile(String fileName) {
        return ImageIO.read(new File(fileName));
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

        ImageIO.write(img, format, file);
    }

}
