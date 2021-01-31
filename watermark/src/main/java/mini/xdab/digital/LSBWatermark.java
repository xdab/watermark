package mini.xdab.digital;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import mini.xdab.consts.BitConsts;
import mini.xdab.consts.Consts;
import mini.xdab.exception.ImageTooSmallException;
import mini.xdab.singleton.Log;
import mini.xdab.singleton.Random;
import mini.xdab.digital.util.MessagesBuffer;
import mini.xdab.utils.BitUtils;
import mini.xdab.utils.ImageUtils;
import mini.xdab.utils.RGBUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static mini.xdab.consts.LSBConsts.*;


public class LSBWatermark extends DigitalWatermark {

    @Getter @Setter
    protected Boolean verticalMode = false;


    @SneakyThrows
    @Override
    public byte[] read(@NonNull BufferedImage img) {
        return processProbableMessages(img, findProbableMessages(img));
    }

    @SneakyThrows
    @Override
    public void write(@NonNull BufferedImage img, @NonNull byte[] data) {
        // Fit watermark onto image
        int imgSizePx = ImageUtils.getImageSize(img);
        int wmSizePx = data.length * BYTE_SIZE_PIXELS + 2 * WORD_SIZE_PIXELS;

        if (imgSizePx < wmSizePx)
            throw new ImageTooSmallException(wmSizePx, imgSizePx);

        // Start from random position
        int pxIndex = Random.getInt(imgSizePx - wmSizePx - 1);
        Log.debug(this,".write Writing at pxIndex=%d", pxIndex);

        writeGoodWord(img, BitConsts.SYNC_WORD, pxIndex);
        pxIndex += WORD_SIZE_PIXELS;

        for (byte b : data) {
            writeGoodByte(img, b, pxIndex);
            pxIndex += BYTE_SIZE_PIXELS;
        }

        writeGoodWord(img, BitConsts.END_WORD, pxIndex);
    }


    private ArrayList<Integer> findProbableMessages(BufferedImage img) {
        int imgSizePx = ImageUtils.getImageSize(img);
        var probableMessages = new ArrayList<Integer>();
        Log.info("LSBWatermark.findProbableMessages Starting for image '%s' of size %d pixels", img.toString(), imgSizePx);

        int lastRead = 0x000000;

        for (int pxIndex = 0; pxIndex < imgSizePx; ++pxIndex) {
            var xy = ImageUtils.getPositionFromPxIndex(img, pxIndex, verticalMode);
            Log.ultra(this, ".findProbableMessages at pxIndex=%d of %d (x=%d, y=%d)", pxIndex, imgSizePx, xy.getValue0(), xy.getValue1());

            int pxRGB = img.getRGB(xy.getValue0(), xy.getValue1());
            int channelLSBs = RGBUtils.getChannelLSBs(pxRGB);
            lastRead = (lastRead << Consts.RGB_CHANNELS) | channelLSBs;

            if (BitUtils.isGoodSyncWord(lastRead)) {
                int probableMessageStart = pxIndex - WORD_SIZE_PIXELS + 1;
                probableMessages.add(probableMessageStart);
                Log.debug(this, ".findProbableMessages Probable message at pxIndex=%d of %d", probableMessageStart, imgSizePx);
            }
        }

        Log.debug(this, ".findProbableMessages Returning %d probable messages", probableMessages.size());
        return probableMessages;
    }


    private byte[] processProbableMessages(BufferedImage img, ArrayList<Integer> probableMessages) {
        int imgSizePx = ImageUtils.getImageSize(img);
        var msgsBuffer = new MessagesBuffer();

        int lastRead = 0x0000;
        for (int msgStartPx : probableMessages) {
            msgsBuffer.beginMessage();

            for (int pos = msgStartPx; pos < imgSizePx-BYTE_SIZE_PIXELS; pos += BYTE_SIZE_PIXELS) {
                Byte b = readGoodByte(img, pos);
                if (b == null) {
                    msgsBuffer.abortMessage();
                    break;
                }

                lastRead = (lastRead << 8) | b;
                msgsBuffer.addMessageByte(b);
                if (BitUtils.isEndWord(lastRead)) {
                    msgsBuffer.concludeMessage();
                    Log.info(this, ".processProbableMessages Message (startPx=%d, endPx=%d) concluded", msgStartPx, pos);
                    break;
                }
            }
        }

        Log.info(this,".processProbableMessages Returning %d bytes of data", msgsBuffer.size());
        return msgsBuffer.getMessages();
    }


    private Byte readGoodByte(BufferedImage img, int pxIndex) {
        int byteCandidate = 0x000;

        for (int offset = 0; offset < BYTE_SIZE_PIXELS; ++offset) {
            var xy = ImageUtils.getPositionFromPxIndex(img, pxIndex + offset, verticalMode);

            int rgb = img.getRGB(xy.getValue0(), xy.getValue1());
            int threeBits = RGBUtils.getChannelLSBs(rgb);
            byteCandidate = (byteCandidate << Consts.RGB_CHANNELS) | threeBits;
        }

        return BitUtils.goodByteOrNull(byteCandidate);
    }

    private void writeGoodByte(BufferedImage img, Byte data, int startPx) {
        var dataInt = BitUtils.goodByteOf(data);

        for (int i = 0; i < BYTE_SIZE_PIXELS; ++i, ++startPx) {
            var xy = ImageUtils.getPositionFromPxIndex(img, startPx, verticalMode);

            // Bit magic for left to right bit reading
            int threeBits = (dataInt & BitConsts.THIRD_BIT_TRIPLET_MASK) >> 6;
            dataInt <<= Consts.RGB_CHANNELS;

            int rgb = img.getRGB(xy.getValue0(), xy.getValue1());
            img.setRGB(xy.getValue0(), xy.getValue1(), RGBUtils.setChannelLSBs(rgb, threeBits));
        }
    }

    private void writeGoodWord(BufferedImage img, Integer word, int startPx) {
        writeGoodByte(img, (byte) ((word & 0xff00) >> 8), startPx);
        writeGoodByte(img, (byte) ((word & 0xff)), startPx + BYTE_SIZE_PIXELS);
    }

}
