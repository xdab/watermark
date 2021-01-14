package mini.xdab.Experimental;

import mini.xdab.RGBUtils;

import java.awt.image.BufferedImage;


public class LSBVisualizer {

    public static BufferedImage process(BufferedImage img) {
        var vis = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < img.getWidth(); ++x) {
            for (int y = 0; y < img.getHeight(); ++y) {
                int rgb = img.getRGB(x, y);
                int threeBits = RGBUtils.getChannelLSBs(rgb);
                rgb = 0;
                if ((threeBits & 0b100) > 0) rgb |= 0xff0000; // Channel Red
                if ((threeBits & 0b010) > 0) rgb |= 0x00ff00; // Channel Green
                if ((threeBits & 0b001) > 0) rgb |= 0x0000ff; // Channel Blue
                vis.setRGB(x, y, rgb);
            }
        }

        return vis;
    }

}
