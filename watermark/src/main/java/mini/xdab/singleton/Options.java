package mini.xdab.singleton;

import lombok.NonNull;
import lombok.SneakyThrows;
import mini.xdab.IWatermarkReader;
import mini.xdab.IWatermarkWriter;
import mini.xdab.constants.OptionConstants;
import mini.xdab.digital.BlocksWatermark;
import mini.xdab.digital.ConstellationWatermark;
import mini.xdab.digital.LSBWatermark;
import mini.xdab.digital.StripesWatermark;
import mini.xdab.exception.OptionsException;
import mini.xdab.utils.OptionUtils;
import mini.xdab.utils.ParseUtils;
import mini.xdab.utils.TextUtils;
import org.apache.commons.cli.*;

import java.util.Locale;
import java.util.Optional;


public final class Options {

    private static volatile CommandLine parsedArgs;

    @SneakyThrows
    public static void init(@NonNull String[] args) {
        if (Options.parsedArgs != null)
            throw new OptionsException("Setting CLI arguments for a second time");
        Options.parsedArgs = OptionUtils.parseArgsToCmd(args);
    }

    @SneakyThrows
    private static CommandLine getParsedArgs() {
        if (parsedArgs == null)
            throw new OptionsException("CLI options unset. Ensure parsing takes place");
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
        return ParseUtils.uintOrDefault(
            getParsedArgs().getOptionValue(OptionConstants.LONG_ARGUMENT_REPEAT),
            OptionConstants.DEFAULT_REPEAT
        );
    }

    public static String getMessage() {
        return Optional
                .ofNullable(getParsedArgs().getOptionValue(OptionConstants.LONG_ARGUMENT_MESSAGE))
                .orElse(OptionConstants.DEFAULT_MESSAGE);
    }

    public static Boolean getRead() {
        return getParsedArgs().hasOption(OptionConstants.FLAG_READ);
    }

    public static Boolean getWrite() {
        return getParsedArgs().hasOption(OptionConstants.FLAG_WRITE);
    }

    //

    private static String getDefaultOutput() {
        var modifiedInput = TextUtils.appendToFileName(getInput(), "_out");
        if (!modifiedInput.equals(getInput()))
            return modifiedInput;
        return OptionConstants.DEFAULT_OUTPUT;
    }

    private static IWatermarkWriter getDefaultWM() {
        return getLSBWM();
    }

    //

    private static LSBWatermark getLSBWM() {
        var lsbWM = new LSBWatermark();

        if (getParsedArgs().hasOption(OptionConstants.FLAG_HORIZONTAL))
            lsbWM.setVerticalMode(Boolean.FALSE);
        if (getParsedArgs().hasOption(OptionConstants.FLAG_VERTICAL))
            lsbWM.setVerticalMode(Boolean.TRUE);

        return lsbWM;
    }

    private static StripesWatermark getStripesWM() {
        StripesWatermark stripesWM = (StripesWatermark) getLSBWM();

        if (getParsedArgs().hasOption(OptionConstants.FLAG_MAJORITY))
            stripesWM.setMajorityMode(Boolean.TRUE);

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
