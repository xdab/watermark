package mini.xdab.singleton;

import mini.xdab.IWatermarkReader;
import mini.xdab.IWatermarkWriter;
import mini.xdab.constants.OptionConstants;
import mini.xdab.digital.BlocksWatermark;
import mini.xdab.digital.ConstellationWatermark;
import mini.xdab.digital.LSBWatermark;
import mini.xdab.digital.StripesWatermark;
import org.apache.commons.cli.CommandLine;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;


public final class Options {

    private static volatile CommandLine cmd;

    public static CommandLine getCmd() {
        if (cmd == null)
            throw new RuntimeException("CLI options unset. Ensure parsing takes place");
        return cmd;
    }

    public static void setCmd(CommandLine cmd) {
        if (Options.cmd != null)
            throw new RuntimeException("Setting CLI options twice at runtime");
        Options.cmd = cmd;
    }

    public static String getInput() {
        return cmd.getOptionValue(OptionConstants.LONG_ARGUMENT_INPUT);
    }

    public static String getOutput() {
        return Optional
                .ofNullable(getCmd().getOptionValue(OptionConstants.LONG_ARGUMENT_OUTPUT))
                .orElseGet(Options::getDefaultOutput);
    }

    public static IWatermarkWriter getWriter() {
        var t = getCmd().getOptionValue(OptionConstants.LONG_ARGUMENT_TYPE);
        if (t == null || t.isEmpty())
            return getDefaultWM();

        t = t.toLowerCase(Locale.ROOT);
        switch (t) {
            case OptionConstants.TYPE_ALIAS_LSB:
                return getLSBWM();
            case OptionConstants.TYPE_ALIAS_STRIPES:
                return getStripesWM();
            case OptionConstants.TYPE_ALIAS_BLOCKS:
                return getBlocksWM();
            case OptionConstants.TYPE_ALIAS_CONSTELLATION:
                return getConstellationWM();
        }

        return getDefaultWM();
    }

    public static IWatermarkReader getReader() {
        var writer = getWriter();
        if (writer instanceof IWatermarkReader)
            return (IWatermarkReader) writer;
        return null;
    }

    public static Integer getRepeat() {
        int R = getDefaultRepeat();
        try { R = Integer.parseUnsignedInt(getCmd().getOptionValue(OptionConstants.LONG_ARGUMENT_REPEAT)); }
        catch (NumberFormatException nfe) { }
        return R;
    }

    public static String getMessage() {
        return Optional
                .ofNullable(getCmd().getOptionValue(OptionConstants.LONG_ARGUMENT_MESSAGE))
                .orElseGet(Options::getDefaultMessage);
    }

    //

    private static String getDefaultOutput() {
        String[] inputSplitByDots = getInput().split("\\.");
        int fileExtensionIdx = inputSplitByDots.length - 1;
        if (fileExtensionIdx >= 0) {
            inputSplitByDots[fileExtensionIdx] = "_OUTPUT.png";
            return Arrays.stream(inputSplitByDots).reduce((a, b) -> a+b).orElse("output.png");
        }
        return "output.png";
    }

    private static IWatermarkWriter getDefaultWM() {
        return getLSBWM();
    }

    private static Integer getDefaultRepeat() {
        return 1;
    }

    private static String getDefaultMessage() {
        return "WATERMARK";
    }

    //

    private static LSBWatermark getLSBWM() {
        // todo: parse other arguments to set lsb specific options
        return new LSBWatermark();
    }

    private static StripesWatermark getStripesWM() {
        // todo: parse other arguments to set stripes specific options
        return new StripesWatermark();
    }

    private static BlocksWatermark getBlocksWM() {
        // todo: parse other arguments to set blocks specific options
        return new BlocksWatermark();
    }

    private static ConstellationWatermark getConstellationWM() {
        // todo: parse other arguments to set constellation specific options
        return new ConstellationWatermark();
    }

}
