package mini.xdab.singleton;

import lombok.NonNull;
import mini.xdab.IWatermarkReader;
import mini.xdab.IWatermarkWriter;
import mini.xdab.constants.OptionConstants;
import mini.xdab.digital.BlocksWatermark;
import mini.xdab.digital.ConstellationWatermark;
import mini.xdab.digital.LSBWatermark;
import mini.xdab.digital.StripesWatermark;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;


public final class Options {

    private static volatile CommandLine parsedArgs;

    public static void setParsedArgs(@NonNull CommandLine parsedArgs) {
        if (Options.parsedArgs != null)
            throw new RuntimeException("Setting CLI arguments for a second time");
        Options.parsedArgs = parsedArgs;
    }

    private static CommandLine getParsedArgs() {
        if (parsedArgs == null)
            throw new RuntimeException("CLI options unset. Ensure parsing takes place");
        return parsedArgs;
    }

    public static String getInput() {
        return getParsedArgs().getOptionValue(OptionConstants.LONG_ARGUMENT_INPUT);
    }

    public static String getOutput() {
        return Optional
                .ofNullable(getParsedArgs().getOptionValue(OptionConstants.LONG_ARGUMENT_OUTPUT))
                .orElseGet(Options::getDefaultOutput);
    }

    public static IWatermarkWriter getWriter() {
        var t = getParsedArgs().getOptionValue(OptionConstants.LONG_ARGUMENT_TYPE);
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
        int R = OptionConstants.DEFAULT_REPEAT;
        try { R = Integer.parseUnsignedInt(getParsedArgs().getOptionValue(OptionConstants.LONG_ARGUMENT_REPEAT)); }
        catch (NumberFormatException nfe) { }
        return R;
    }

    public static String getMessage() {
        return Optional
                .ofNullable(getParsedArgs().getOptionValue(OptionConstants.LONG_ARGUMENT_MESSAGE))
                .orElse(OptionConstants.DEFAULT_MESSAGE);
    }

    //

    private static String getDefaultOutput() {
        var modifiedInput = getInput().replaceFirst("^(.+)(\\.\\D+$)", "$1_out$2");
        if (!modifiedInput.equals(getInput()))
            return modifiedInput;
        return OptionConstants.DEFAULT_OUTPUT;
    }

    private static IWatermarkWriter getDefaultWM() {
        return getLSBWM();
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
