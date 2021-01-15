package mini.xdab;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;


public class LSBWatermark extends DigitalWatermark {

    public static final byte SYNC_WORD = 0x5b; // '['
    public static final byte END_WORD  = 0x24; // '$' (almost ~SYNCWORD)

    // Semantic function to remove bit magic from message reading
    protected static boolean isGoodSyncWord(int candidate) {
        return BitUtils.isGoodByte(candidate, SYNC_WORD);
    }


    protected int readByte(@NonNull BufferedImage img, int position) {
        int byteCandidate = 0x000;

        for (int offset = 0; offset < 3; ++offset) {
            int x = (position + offset) % img.getWidth();
            int y = (position + offset) / img.getHeight();

            int rgb = img.getRGB(x, y);
            int threeBits = RGBUtils.getChannelLSBs(rgb);
            byteCandidate = (byteCandidate << 3) | threeBits;
        }

        if (BitUtils.goodParity(byteCandidate))
            return byteCandidate >>> 1;

        return -1;
    }


    @SneakyThrows
    @Override
    public byte[] read(@NonNull BufferedImage img) {
        int imgSizePx = img.getHeight() * img.getHeight();
        var probableMessages = new ArrayList<Integer>();

        int lastRead = 0x000;

        for (int position = 0; position < imgSizePx; ++position) {
            int x = position % img.getWidth();
            int y = position / img.getHeight();

            // Interpret current pixels as three bits
            // updating the int "buffer" with them
            int rgb = img.getRGB(x, y);
            int threeBits = RGBUtils.getChannelLSBs(rgb);
            lastRead = (lastRead << 3) | threeBits;

            if (isGoodSyncWord(lastRead)) {
                // Minus 2 because since the SYNC_WORD is completely in lastRead
                // we must have gone 2 pixels ahead from its start
                probableMessages.add(position - 2);
            }
        }

        var messageBuffer = new ByteArrayOutputStream();
        var message = new ByteArrayOutputStream();

        for (int msgStartPx : probableMessages) {
            messageBuffer.reset();

            for (int pos = msgStartPx; pos < imgSizePx-3; pos+=3) {
                int b = readByte(img, pos);

                if (b >= 0) {
                    messageBuffer.write(b);
                } else {
                    messageBuffer.reset();
                    break;
                }

                if (b == END_WORD)
                    break;
            }

            // Got a message
            if (messageBuffer.size() > 0) {
                message.write(messageBuffer.toByteArray());
            }
        }

        return message.toByteArray();
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
