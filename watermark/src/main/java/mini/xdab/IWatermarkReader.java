package mini.xdab;

import lombok.NonNull;

import java.awt.image.BufferedImage;


public interface IWatermarkReader {

    byte[] read(@NonNull BufferedImage img);

    String readString(@NonNull BufferedImage img);

}
