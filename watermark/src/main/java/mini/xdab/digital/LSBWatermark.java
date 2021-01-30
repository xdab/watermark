package mini.xdab.digital;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import mini.xdab.exception.ImageTooSmallException;
import mini.xdab.singleton.Log;
import mini.xdab.singleton.Random;
import mini.xdab.utils.BitUtils;
import mini.xdab.utils.ImageUtils;
import mini.xdab.utils.RGBUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static mini.xdab.constants.LSBConstants.*;


public class LSBWatermark extends DigitalWatermark {

    @Getter @Setter
    protected Boolean verticalMode = false;


    @SneakyThrows
    @Override
    public byte[] read(@NonNull BufferedImage img) {
        var probableMessages = findProbableMessages(img);
        return processProbableMessages(img, probableMessages);
    }

    @SneakyThrows
    @Override
    public void write(@NonNull BufferedImage img, @NonNull byte[] data) {
        // Fit watermark onto image
        int imgSizePx = ImageUtils.getImageSize(img);
        int wmSizePx = data.length * BYTE_SIZE_PIXELS + (NUM_END_WORDS + NUM_SYNC_WORDS) * WORD_SIZE_PIXELS;

        if (imgSizePx < wmSizePx)
            throw new ImageTooSmallException(wmSizePx, imgSizePx);

        // Start from random position
        int pxIndex = Random.getInt(imgSizePx - wmSizePx - 1);
        Log.debug("LSBWatermark.write Writing at pxIndex=%d", pxIndex);

        writeGoodWord(img, SYNC_WORD, pxIndex);
        pxIndex += WORD_SIZE_PIXELS;

        for (byte b : data) {
            writeGoodByte(img, b, pxIndex);
            pxIndex += BYTE_SIZE_PIXELS;
        }

        writeGoodWord(img, END_WORD, pxIndex);
    }


    protected ArrayList<Integer> findProbableMessages(@NonNull BufferedImage img) {
        int imgSizePx = ImageUtils.getImageSize(img);
        var probableMessages = new ArrayList<Integer>();
        Log.info("LSBWatermark.findProbableMessages Starting for image '%s' of size %d pixels", img.toString(), imgSizePx);

        int lastRead = 0x000000;

        for (int pxIndex = 0; pxIndex < imgSizePx; ++pxIndex) {
            var xy = ImageUtils.getPositionFromPxIndex(img, pxIndex, verticalMode);
            Log.ultra("LSBWatermark.findProbableMessages at pxIndex=%d of %d (x=%d, y=%d)", pxIndex, imgSizePx, xy.getValue0(), xy.getValue1());

            int pxRGB = img.getRGB(xy.getValue0(), xy.getValue1());
            int channelLSBs = RGBUtils.getChannelLSBs(pxRGB);
            lastRead = (lastRead << 3) | channelLSBs;

            if (isGoodSyncWord(lastRead)) {
                int probableMessageStart = pxIndex - WORD_SIZE_PIXELS + 1;
                probableMessages.add(probableMessageStart);
                Log.debug("LSBWatermark.findProbableMessages Probable message", probableMessageStart, imgSizePx);
            }
        }

        return probableMessages;
    }



    protected byte[] processProbableMessages(@NonNull BufferedImage img, @NonNull ArrayList<Integer> probableMessages) {
        int imgSizePx = ImageUtils.getImageSize(img);

        var messageBuffer = new ByteArrayOutputStream();
        var message = new ByteArrayOutputStream();

        int lastBytes = 0x0000;

        for (int msgStartPx : probableMessages) {
            messageBuffer.reset();

            for (int pos = msgStartPx; pos < imgSizePx-BYTE_SIZE_PIXELS; pos+=BYTE_SIZE_PIXELS) {
                Byte b = readGoodByte(img, pos);
                if (b == null) {
                    messageBuffer.reset();
                    break;
                }

                lastBytes = (lastBytes << 8) | b;
                messageBuffer.write(b);
                if (isEndWord(lastBytes)) {
                    Log.info("LSBWatermark.processProbableMessages Message (startPx=%d, endPx=%d) has ended correctly", msgStartPx, pos);
                    break;
                }
            }

            if (messageBuffer.size() > 0) {
                Log.debug("LSBWatermark.processProbableMessages Writing message (startPx=%d) to main buffer", msgStartPx);
                try { message.write(messageBuffer.toByteArray()); }
                catch (IOException ioe) {
                    Log.error("LSBWatermark.processProbableMessages Unable to write message (startPx=%d) to main buffer: %s", msgStartPx, ioe.getMessage());
                }
            }
        }

        Log.info("LSBWatermark.processProbableMessages Returning %d bytes of data", message.size());
        return message.toByteArray();
    }


    protected Byte readGoodByte(@NonNull BufferedImage img, int byteIdx) {
        int byteCandidate = 0x000;

        for (int offset = 0; offset < BYTE_SIZE_PIXELS; ++offset) {
            var xy = ImageUtils.getPositionFromPxIndex(img, byteIdx + offset, verticalMode);

            int rgb = img.getRGB(xy.getValue0(), xy.getValue1());
            int threeBits = RGBUtils.getChannelLSBs(rgb);
            byteCandidate = (byteCandidate << 3) | threeBits;
        }

        if (BitUtils.goodParity(byteCandidate))
            return (byte) (byteCandidate >>> 1);

        return null;
    }

    protected void writeGoodByte(@NonNull BufferedImage img, Byte data, int startPx) {
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

    protected void writeGoodWord(@NonNull BufferedImage img, Integer word, int startPx) {
        writeGoodByte(img, (byte) ((word & 0xff00) >> 8), startPx);
        writeGoodByte(img, (byte) ((word & 0xff)), startPx + BYTE_SIZE_PIXELS);
    }

    // Semantic function to remove bit magic from message reading
    protected static boolean isGoodSyncWord(Integer candidateSyncWord) {
        return BitUtils.isGoodWord(candidateSyncWord, SYNC_WORD);
    }

    protected static boolean isEndWord(Integer candidateEndWord) {
        return (candidateEndWord & 0xffff) == END_WORD;
    }

}
