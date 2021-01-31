package mini.xdab.digital;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import mini.xdab.consts.BitConsts;
import mini.xdab.consts.Consts;
import mini.xdab.digital.util.Stripes;
import mini.xdab.exception.ImageTooSmallException;
import mini.xdab.singleton.Log;
import mini.xdab.singleton.Random;
import mini.xdab.digital.util.MessagesBuffer;
import mini.xdab.digital.util.ShiftRegister;
import mini.xdab.utils.BitUtils;
import mini.xdab.utils.RGBUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.lang.Math.abs;
import static mini.xdab.consts.StripesConsts.BYTE_SIZE_STRIPES;
import static mini.xdab.consts.StripesConsts.WORD_SIZE_STRIPES;
import static mini.xdab.utils.ImageUtils.getPositionFromStripeDims;


public class StripesWatermark extends DigitalWatermark {

    @Getter @Setter
    private Boolean verticalMode = false;

    @Getter @Setter
    private Boolean majorityMode = false;


    @Override
    public byte[] read(@NonNull BufferedImage img) {
        var stripes = new Stripes(img, verticalMode);
        return processProbableMessages(stripes, findProbableMessages(stripes));
    }

    @SneakyThrows
    @Override
    public void write(@NonNull BufferedImage img, @NonNull byte[] data) {
        var stripes = new Stripes(img, verticalMode);
        int wmSizeStripes = data.length * BYTE_SIZE_STRIPES + 2 * WORD_SIZE_STRIPES;

        if (stripes.getNumStripes() < wmSizeStripes)
            throw new ImageTooSmallException(wmSizeStripes, stripes.getNumStripes());

        // Start from random position
        int stripe = Random.getInt(stripes.getNumStripes() - wmSizeStripes - 1);
        Log.debug(this,".write Writing at stripe=%d", stripe);

        writeGoodWord(img, stripes, BitConsts.SYNC_WORD, stripe);
        stripe += WORD_SIZE_STRIPES;

        for (byte b : data) {
            writeGoodByte(img, stripes, b, stripe);
            stripe += BYTE_SIZE_STRIPES;
        }

        writeGoodWord(img, stripes, BitConsts.END_WORD, stripe);
    }


    private Integer getMajority(Stripes stripes) {
        int majority = stripes.getCountedDimSize();
        if (majorityMode)
            majority /= 2;
        return majority;
    }

    private ArrayList<Integer> findProbableMessages(Stripes stripes) {
        var probableMessages = new ArrayList<Integer>();
        Log.info(this,".findProbableMessages Starting for %d stripes", stripes.getNumStripes());

        int majority = getMajority(stripes);
        var lastRead = new ShiftRegister();

        for (int s = 0; s < stripes.getNumStripes(); ++s) {
            int oneCount = stripes.getStripe(s);

            lastRead.shift((oneCount >= majority));
            int lastWord = lastRead.get(WORD_SIZE_STRIPES);
            Log.ultra(this, ".findProbableMessages at stripe=%d of %d, oneCount=%d, majority=%d, lastWord=0x%05x", s, stripes.getNumStripes(), oneCount, majority, lastWord & 0x3ffff);

            if (BitUtils.isGoodSyncWord(lastWord)) {
                int probableMessageStart = s - WORD_SIZE_STRIPES + 1;
                probableMessages.add(probableMessageStart);
                Log.debug(this, ".findProbableMessages Probable message at stripe=%d of %d", probableMessageStart, stripes.getNumStripes());
            }
        }

        Log.debug(this, ".findProbableMessages Returning %d probable messages", probableMessages.size());
        return probableMessages;
    }


    private byte[] processProbableMessages(Stripes stripes, ArrayList<Integer> probableMessages) {
        var msgsBuffer = new MessagesBuffer();

        int lastRead = 0x0000;
        for (int msgStartStripe : probableMessages) {
            msgsBuffer.beginMessage();

            for (int stripe = msgStartStripe; stripe < stripes.getNumStripes() - BYTE_SIZE_STRIPES; stripe += BYTE_SIZE_STRIPES) {
                Byte b = readGoodByte(stripes, stripe);
                if (b == null) {
                    msgsBuffer.abortMessage();
                    break;
                }

                lastRead = (lastRead << 8) | b;
                msgsBuffer.addMessageByte(b);
                if (BitUtils.isEndWord(lastRead)) {
                    msgsBuffer.concludeMessage();
                    Log.info(this,".processProbableMessages Message (startStripe=%d, endStripe=%d) concluded", msgStartStripe, stripe);
                    break;
                }
            }
        }

        Log.info(this,".processProbableMessages Returning %d bytes of data", msgsBuffer.size());
        return msgsBuffer.getMessages();
    }


    private Byte readGoodByte(Stripes stripes, Integer stripe) {
        int byteCandidate = 0x000;

        int majority = getMajority(stripes);
        for (int offset = 0; offset < BYTE_SIZE_STRIPES; ++offset) {
            int oneCount = stripes.getStripe(stripe + offset);

            byteCandidate <<= 1;
            if (oneCount >= majority)
                byteCandidate |= 1;
        }

        return BitUtils.goodByteOrNull(byteCandidate);
    }

    private void writeGoodWord(BufferedImage img, Stripes stripes, Integer word, Integer startStripe) {
        Log.ultra(this, ".writeGoodWord word=0x%04x at stripe=%d", word, startStripe);
        writeGoodByte(img, stripes, (byte) ((word & 0xff00) >> 8), startStripe);
        writeGoodByte(img, stripes, (byte) ((word & 0xff)), startStripe + BYTE_SIZE_STRIPES);
    }

    private void writeGoodByte(BufferedImage img, Stripes stripes, Byte b, Integer startStripe) {
        Integer goodByte = BitUtils.goodByteOf(b);
        Log.ultra(this, ".writeGoodByte byte=0x%02x, goodByte=0x%02x at stripe=%d", b, goodByte, startStripe);
        for (int offset = 0; offset < BYTE_SIZE_STRIPES; ++offset) {
            int bit = ((goodByte & 0x100) > 0) ? 1 : 0;
            writeBit(img, stripes, bit, startStripe + offset);
            goodByte <<= 1;
        }
    }

    private void writeBit(BufferedImage img, Stripes stripes, Integer bit, Integer stripe) {
        int stripeOnes = stripes.getStripe(stripe);
        int majority = getMajority(stripes);

        boolean stripeIsOne = (stripeOnes >= majority);
        boolean    bitIsOne = (bit > 0);

        int    line = stripe / Consts.RGB_CHANNELS;
        int channel = stripe % Consts.RGB_CHANNELS;

        if (!majorityMode) {
            for (int c = 0; c < stripes.getCountedDimSize(); ++c) {
                var xy = getPositionFromStripeDims(line, c, stripes.getVerticalMode());
                int rgb = img.getRGB(xy.getValue0(), xy.getValue1());
                img.setRGB(xy.getValue0(), xy.getValue1(), RGBUtils.setChannelLSB(rgb, channel, bit));
            }
        }

        else if (stripeIsOne ^ bitIsOne) {
            int delta = abs(majority - stripeOnes) + 1;
            Log.ultra(this, ".writeBit(majority) BIT=%d stripeOnes=%d majority=%d, delta=%d", bit, stripeOnes, majority, delta);

            while (delta > 0) {
                int c = Random.getInt(stripes.getCountedDimSize());
                var xy = getPositionFromStripeDims(line, c, stripes.getVerticalMode());

                int rgb = img.getRGB(xy.getValue0(), xy.getValue1());
                if (RGBUtils.getChannelLSB(rgb, channel) != bit) {
                    Log.ultra(this, ".writeBit(majority) delta=%d, stripeOnes=%d target=%d flipping c=%d, xy=(%d, %d)", delta, stripeOnes, majority, c, xy.getValue0(), xy.getValue1());
                    img.setRGB(xy.getValue0(), xy.getValue1(), RGBUtils.setChannelLSB(rgb, channel, bit));
                    delta -= 1;
                }
            }
        }
    }
}
