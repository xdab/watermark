package mini.xdab;

import lombok.NonNull;

import java.awt.image.BufferedImage;
import java.util.Random;

public class LSBWatermark extends DigitalWatermark {

    public static final byte SYNC_WORD = 0x3a; // ':'
    public static final byte END_WORD  = 0x45; // 'E' (almost ~SYNCWORD)


    @Override
    public byte[] read(@NonNull BufferedImage img) {
        int position = 0;
        int imgSizePx = img.getHeight() * img.getHeight();

        int lastRead = 0x000;
        while (position < imgSizePx) {
            int x = position % img.getWidth();
            int y = position / img.getHeight();

            // Interpret current pixels as three bits
            // updating the int "buffer" with them
            int rgb = img.getRGB(x, y);
            int threeBits = RGBUtils.getChannelLSBs(rgb);
            lastRead = (lastRead << 3) | threeBits;

            // (SYNC_WORD's parity bit is 0)
            if ((lastRead & 0x1ff) == (SYNC_WORD << 1)) {
                // Probable message detected
                System.out.println("Probable msg start at position" + position);
                // TODO: start reading the message into some buffer
                // TODO:    if parity breaks before a correct END_WORD: message is invalid
                // TODO:    if a correct END_WORD is detected: save message
            }

            position++;
        }

        // TODO: concatenate messages and return
        return new byte[0];
    }


    protected void writeByte(@NonNull BufferedImage img, byte data, int startPx) {
        // Append byte with 9th bit = parity
        int dataInt = (data << 1) | BitUtils.parityBit(data);

        for (int i = 0; i < 3; ++i, ++startPx) {
            int x = startPx % img.getWidth();
            int y = startPx / img.getHeight();

            // Bit magic for left to right bit reading
            int threeBits = (dataInt & 0b111000000) >> 6;
            dataInt <<= 3;

            int rgb = img.getRGB(x, y);
            img.setRGB(x, y, RGBUtils.setChannelLSBs(rgb, threeBits));
        }
    }


    @Override
    public void write(@NonNull BufferedImage img, @NonNull byte[] data) {
        // Fit watermark onto image
        int imgSizePx = img.getHeight() * img.getWidth();
        int markSizePx = (2 + data.length) * 3;

        if (markSizePx > imgSizePx) // Image too small
            throw new RuntimeException("Watermark data does not fit into the specified image");

        // Start from random position
        int positionPx = (new Random()).nextInt(imgSizePx - markSizePx - 1);

        writeByte(img, SYNC_WORD, positionPx);
        positionPx += 3;

        for (int i = 0; i < data.length; ++i) {
            writeByte(img, data[i], positionPx);
            positionPx += 3;
        }

        writeByte(img, END_WORD, positionPx);
        //positionPx += 3; // Never used again
    }

}
