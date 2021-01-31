package mini.xdab.singleton;

import lombok.NonNull;
import lombok.SneakyThrows;
import mini.xdab.IWatermarkReader;
import mini.xdab.IWatermarkWriter;
import mini.xdab.consts.OptionConsts;
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
        return getParsedArgs().getOptionValue(OptionConsts.LONG_ARGUMENT_INPUT);
    }

    public static String getOutput() {
        return Optional
                .ofNullable(getParsedArgs().getOptionValue(OptionConsts.LONG_ARGUMENT_OUTPUT))
                .orElseGet(Options::getDefaultOutput);
    }

    public static IWatermarkWriter getWriter() {
        var t = getParsedArgs().getOptionValue(OptionConsts.LONG_ARGUMENT_TYPE);
        if (t == null || t.isEmpty())
            return getDefaultWM();

        t = t.toLowerCase(Locale.ROOT);
        switch (t) {
            case OptionConsts.TYPE_ALIAS_LSB:
                return getLSBWM();
            case OptionConsts.TYPE_ALIAS_STRIPES:
                return getStripesWM();
            case OptionConsts.TYPE_ALIAS_CONSTELLATION:
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
            getParsedArgs().getOptionValue(OptionConsts.LONG_ARGUMENT_REPEAT),
            OptionConsts.DEFAULT_REPEAT
        );
    }

    public static String getMessage() {
        return Optional
                .ofNullable(getParsedArgs().getOptionValue(OptionConsts.LONG_ARGUMENT_MESSAGE))
                .orElse(OptionConsts.DEFAULT_MESSAGE);
    }

    public static Boolean getRead() {
        return getParsedArgs().hasOption(OptionConsts.FLAG_READ);
    }

    public static Boolean getWrite() {
        return !getRead();
    }

    public static Boolean getVertical() {
        return getParsedArgs().hasOption(OptionConsts.FLAG_VERTICAL);
    }

    public static Boolean getMajority() {
        return getParsedArgs().hasOption(OptionConsts.FLAG_MAJORITY);
    }

    public static Boolean getHorizontal() {
        return !getVertical();
    }

    public static Boolean getVisualizeLSBs() {
        return getParsedArgs().hasOption(OptionConsts.FLAG_VISUALIZE_LSBS);
    }

    //

    private static String getDefaultOutput() {
        var modifiedInput = TextUtils.appendToFileName(getInput(), "_out");
        if (!modifiedInput.equals(getInput()))
            return modifiedInput;
        return OptionConsts.DEFAULT_OUTPUT;
    }

    private static IWatermarkWriter getDefaultWM() {
        return getLSBWM();
    }

    //

    private static LSBWatermark getLSBWM() {
        var lsbWM = new LSBWatermark();

        Log.debug(null, "Options.getLSBWM lsbWM.setVerticalMode(%s)", Boolean.toString(getVertical()));
        lsbWM.setVerticalMode(getVertical());

        return lsbWM;
    }

    private static StripesWatermark getStripesWM() {
        StripesWatermark stripesWM = new StripesWatermark();

        Log.debug(null, "Options.getStripesWM stripesWM.setVerticalMode(%s)", Boolean.toString(getVertical()));
        stripesWM.setVerticalMode(getVertical());
        Log.debug(null, "Options.getStripesWM stripesWM.setMajorityMode(%s)", Boolean.toString(getMajority()));
        stripesWM.setMajorityMode(getMajority());

        return stripesWM;
    }


    private static ConstellationWatermark getConstellationWM() {
        // todo: parse other arguments to set constellation specific options
        return new ConstellationWatermark();
    }

}
