package mini.xdab;

import lombok.NonNull;

import java.awt.image.BufferedImage;


public interface IWatermarkWriter {

    void write(@NonNull BufferedImage img, @NonNull byte[] data);

    void write(@NonNull BufferedImage img, @NonNull String string);

}
