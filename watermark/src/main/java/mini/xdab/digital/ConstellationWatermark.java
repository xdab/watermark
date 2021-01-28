package mini.xdab.digital;

import lombok.NonNull;

import java.awt.image.BufferedImage;

public class ConstellationWatermark extends DigitalWatermark {

    @Override
    public byte[] read(@NonNull BufferedImage img) {
        return new byte[0]; // todo: hard: implement constellation reading algorithm
    }

    @Override
    public void write(@NonNull BufferedImage img, @NonNull byte[] data) {
        // todo: medium: implement constellation writing algorithm
    }

}
