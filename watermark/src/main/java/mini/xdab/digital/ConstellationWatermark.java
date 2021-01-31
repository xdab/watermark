package mini.xdab.digital;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mini.xdab.consts.BitConsts;
import mini.xdab.consts.Consts;
import mini.xdab.consts.LSBConsts;
import mini.xdab.digital.util.MessagesBuffer;
import mini.xdab.digital.util.ShiftRegister;
import mini.xdab.singleton.Random;
import mini.xdab.utils.BitUtils;
import mini.xdab.utils.ImageUtils;
import mini.xdab.utils.RGBUtils;
import org.w3c.dom.css.RGBColor;

import java.awt.image.BufferedImage;
import java.util.*;

public class ConstellationWatermark extends DigitalWatermark {

    // todo: make this an option
    protected static final Integer MAX_MSG_SIZE_PX    = 16;
    protected static final Integer MAX_MSG_SIZE_BYTES = LSBConsts.BYTE_SIZE_PIXELS * MAX_MSG_SIZE_PX;

    @Getter @Setter
    protected Integer key = 0;


    @Override
    public byte[] read(@NonNull BufferedImage img) {
        Random.seed(key);

        int imgSizePx = ImageUtils.getImageSize(img);
        var visitedPositions = new HashSet<Integer>();
        int visitedCount = 0;

        var bitset = new BitSet(MAX_MSG_SIZE_BYTES * 8);

        while (visitedCount < MAX_MSG_SIZE_PX) {
            int position;
            do { position = Random.getInt(imgSizePx); }
            while (visitedPositions.contains(position));
            visitedPositions.add(position);

            var xy = ImageUtils.getPositionFromPxIndex(img, position, false);
            int pxLSBs = RGBUtils.getChannelLSBs(img.getRGB(xy.getValue0(), xy.getValue1()));

            bitset.set(3 * visitedCount    , (pxLSBs & Consts.MASK_R) > 0);
            bitset.set(3 * visitedCount + 1, (pxLSBs & Consts.MASK_G) > 0);
            bitset.set(3 * visitedCount + 2, (pxLSBs & Consts.MASK_B) > 0);

            ++visitedCount;
        }

        var register = new ShiftRegister();

        var firstWord = Long.valueOf(bitset.get(0, 9).toLongArray()[0]).intValue();
        if (firstWord != BitConsts.GOOD_SYNC_WORD)
            return new byte[0]; // no message

        var msgsBuffer = new MessagesBuffer();
        msgsBuffer.beginMessage();

        for (int B = 0; B < MAX_MSG_SIZE_BYTES; ++B) {
            for (int i = 0; i < LSBConsts.BYTE_SIZE_PIXELS; ++i) {
                register.shift(bitset.get(9 * B + 3 * i));
                register.shift(bitset.get(9 * B + 3 * i + 1));
                register.shift(bitset.get(9 * B + 3 * i + 2));
            }

            var lastWord = register.get(9);
            var goodByte = BitUtils.goodByteOrNull(lastWord);
            if (goodByte == null)
                return new byte[0];
            msgsBuffer.addMessageByte(goodByte);

            if (lastWord.equals(BitConsts.GOOD_END_WORD)) {
                msgsBuffer.concludeMessage();
                break;
            }
        }

        return msgsBuffer.getMessages();
    }

    @Override
    public void write(@NonNull BufferedImage img, @NonNull byte[] data) {
        Random.seed(key);

        int imgSizePx = ImageUtils.getImageSize(img);
        var visitedPositions = new HashSet<Integer>();
        int visitedCount = 0;

        // todo: ...
    }

}
