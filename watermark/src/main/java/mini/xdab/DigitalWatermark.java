package mini.xdab;

import lombok.NonNull;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;


public abstract class DigitalWatermark implements IWatermarkWriter, IWatermarkReader {

    @Override
    public void write(@NonNull BufferedImage img, @NonNull String string) {
        write(img, string.getBytes(StandardCharsets.UTF_8));
    }

}
