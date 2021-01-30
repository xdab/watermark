package mini.xdab.digital;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import mini.xdab.utils.BitUtils;
import mini.xdab.utils.ImageUtils;
import mini.xdab.utils.RGBUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static mini.xdab.constants.LSBConstants.*;


public class LSBWatermark extends DigitalWatermark {

    public static final byte SYNC_WORD = 0x5b; // '['
    public static final byte END_WORD  = 0x24; // '$' (almost ~SYNC_WORD)

    @Getter @Setter
    protected Boolean verticalMode = false;


    @SneakyThrows
    @Override
    public byte[] read(@NonNull BufferedImage img) {
        int imgSizePx = img.getHeight() * img.getHeight();
        var probableMessages = new ArrayList<Integer>();

        // Probable message starts (valid SYNC_WORD occurences)
        // are found and recorded

        int lastRead = 0x000;

        for (int pxIndex = 0; pxIndex < imgSizePx; ++pxIndex) {
            var xy = ImageUtils.getPositionFromPxIndex(img, pxIndex, verticalMode);

            // Interpret current pixels as three bits
            // updating the int "buffer" with them
            int rgb = img.getRGB(xy.getValue0(), xy.getValue1());
            int threeBits = RGBUtils.getChannelLSBs(rgb);
            lastRead = (lastRead << 3) | threeBits;

            if (isGoodSyncWord(lastRead)) {
                // Minus 2 because since the SYNC_WORD is completely in lastRead
                // we must have gone 2 pixels ahead from its start
                probableMessages.add(pxIndex - 2);
            }
        }

        // Probable messages are individually checked
        // for valid body followed by a valid END_WORD

        var messageBuffer = new ByteArrayOutputStream();
        var message = new ByteArrayOutputStream();

        for (int msgStartPx : probableMessages) {
            messageBuffer.reset();

            for (int pos = msgStartPx; pos < imgSizePx-BYTE_SIZE_PIXELS; pos+=BYTE_SIZE_PIXELS) {
                int b = readByte(img, pos);

                if (b < 0) {
                    // Invalid byte invalidates the whole message
                    messageBuffer.reset();
                    break;
                } else {
                    // Valid byte is appended to message candidate
                    messageBuffer.write(b);
                    // END_WORD submits the candidate
                    if (b == END_WORD)
                        break;
                }
            }

            // Got a message
            if (messageBuffer.size() > 0) {
                try {
                    message.write(messageBuffer.toByteArray());
                } catch (IOException ioe) { }
            }
        }

        return message.toByteArray();
    }

    @Override
    public void write(@NonNull BufferedImage img, @NonNull byte[] data) {
        // Fit watermark onto image
        int imgSizePx = img.getHeight() * img.getWidth();
        int markSizePx = (NUM_SYNC_WORDS + NUM_END_WORDS + data.length) * BYTE_SIZE_PIXELS;

        if (markSizePx > imgSizePx) // Image too small
            throw new RuntimeException("Watermark data does not fit into the specified image");

        // Start from random position
        int positionPx = (new Random()).nextInt(imgSizePx - markSizePx - 1);

        writeByte(img, SYNC_WORD, positionPx);
        positionPx += BYTE_SIZE_PIXELS;

        for (byte b : data) {
            writeByte(img, b, positionPx);
            positionPx += BYTE_SIZE_PIXELS;
        }

        writeByte(img, END_WORD, positionPx);
    }

    protected int readByte(@NonNull BufferedImage img, int byteIdx) {
        int byteCandidate = 0x000;

        for (int offset = 0; offset < BYTE_SIZE_PIXELS; ++offset) {
            var xy = ImageUtils.getPositionFromPxIndex(img, byteIdx + offset, verticalMode);

            int rgb = img.getRGB(xy.getValue0(), xy.getValue1());
            int threeBits = RGBUtils.getChannelLSBs(rgb);
            byteCandidate = (byteCandidate << 3) | threeBits;
        }

        if (BitUtils.goodParity(byteCandidate))
            return byteCandidate >>> 1;

        return -1;
    }

    protected void writeByte(@NonNull BufferedImage img, byte data, int startPx) {
        // Append byte with 9th bit = parity
        int dataInt = (data << 1) | BitUtils.parityBit(data);

        for (int i = 0; i < BYTE_SIZE_PIXELS; ++i, ++startPx) {
            var xy = ImageUtils.getPositionFromPxIndex(img, startPx, verticalMode);

            // Bit magic for left to right bit reading
            int threeBits = (dataInt & THIRD_BIT_TRIPLET_MASK) >> 6;
            dataInt <<= 3;

            int rgb = img.getRGB(xy.getValue0(), xy.getValue1());
            img.setRGB(xy.getValue0(), xy.getValue1(), RGBUtils.setChannelLSBs(rgb, threeBits));
        }
    }

    // Semantic function to remove bit magic from message reading
    protected static boolean isGoodSyncWord(int candidate) {
        return BitUtils.isGoodByte(candidate, SYNC_WORD);
    }

}
