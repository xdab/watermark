package mini.xdab.digital.tools;

import lombok.Getter;
import lombok.NonNull;
import mini.xdab.constants.Consts;
import mini.xdab.singleton.Log;
import mini.xdab.utils.RGBUtils;

import java.awt.image.BufferedImage;

public class Stripes {

    private BufferedImage img;

    private int[] oneCounts;

    @Getter
    private Integer indexedDimSize;

    @Getter
    private Integer countedDimSize;

    @Getter
    private Boolean verticalMode;


    public Stripes(@NonNull BufferedImage img, @NonNull Boolean verticalMode) {
        this.img = img;
        this.verticalMode = verticalMode;

        indexedDimSize = verticalMode ? img.getWidth()  : img.getHeight();
        countedDimSize = verticalMode ? img.getHeight() : img.getWidth();
        oneCounts = new int[Consts.RGB_CHANNELS * indexedDimSize];

        Log.debug(this, ".(img,countVertical) Constructed in %s mode, img.width=%d, img.height=%d, indexedDimSize=%d, countedDimSize=%d",
                verticalMode ? "V" : "H", img.getWidth(), img.getHeight(), indexedDimSize, countedDimSize);

        recountAll();
    }

    public Integer getStripe(@NonNull Integer stripe) {
        return oneCounts[stripe];
    }

    public Integer getNumLines() {
        return indexedDimSize;
    }

    public Integer getNumStripes() {
        return Consts.RGB_CHANNELS * getNumLines();
    }

    public void recountAll() {
        for (int i = 0; i < indexedDimSize; ++i)
            recountLine(i);
    }

    public void recountStripe(@NonNull Integer stripe) {
        recountLine(stripe / Consts.RGB_CHANNELS);
    }

    public void recountLine(@NonNull Integer line) {
        int stripeCounts[] = new int[Consts.RGB_CHANNELS];

        for (int c = 0; c < countedDimSize; ++c) {
            int pxRGB = verticalMode ? img.getRGB(line, c) : img.getRGB(c, line);
            int chLSBs = RGBUtils.getChannelLSBs(pxRGB);

            if ((chLSBs & Consts.MASK_R) > 0) ++stripeCounts[Consts.CHANNEL_R];
            if ((chLSBs & Consts.MASK_G) > 0) ++stripeCounts[Consts.CHANNEL_G];
            if ((chLSBs & Consts.MASK_B) > 0) ++stripeCounts[Consts.CHANNEL_B];
        }

        for (int ch = 0; ch < Consts.RGB_CHANNELS; ++ch)
            oneCounts[line * Consts.RGB_CHANNELS + ch] = stripeCounts[ch];
    }

}
