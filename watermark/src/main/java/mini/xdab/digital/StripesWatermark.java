package mini.xdab.digital;

import lombok.NonNull;

import java.awt.image.BufferedImage;

public class StripesWatermark extends DigitalWatermark {

    @Override
    public byte[] read(@NonNull BufferedImage img) {
        return new byte[0]; // todo: medium: implement stripes algorithm reader
    }

    @Override
    public void write(@NonNull BufferedImage img, @NonNull byte[] data) {
        // todo: medium: implement stripes algorithm writer
    }
}
