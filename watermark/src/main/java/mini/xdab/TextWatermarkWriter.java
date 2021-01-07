package mini.xdab;

import lombok.*;
import org.intellij.lang.annotations.JdkConstants;

import java.awt.*;
import java.awt.image.BufferedImage;


public class TextWatermarkWriter implements IWatermarkWriter {

    @Getter @Setter protected Font font;
    @Getter @Setter protected Color color;
    @Getter @Setter protected double alpha;
    @Getter @Setter protected int placement;


    @Override
    public void write(@NonNull BufferedImage img, @NonNull byte[] data) {
        write(img, TextUtils.bytesToString(data));
    }

    @Override
    public void write(@NonNull BufferedImage img, @NonNull String string) {
        //TODO: CREATE write(BufferedImage img, String string)
    }

    // Font related getters/setters

    public void setFontSize(int size) {
        font = font.deriveFont((float) size);
    }

    public int getFontSize(int size) {
        return font.getSize();
    }

    public void setFontName(@NonNull String fontName) {
        font = new Font(fontName, font.getStyle(), font.getSize());
    }

    public String getFontName() {
        return font.getFontName();
    }


}
