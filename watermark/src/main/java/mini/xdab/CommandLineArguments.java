package mini.xdab;

import lombok.NonNull;
import mini.xdab.constants.CommandLineConstants;
import mini.xdab.digital.BlocksWatermark;
import mini.xdab.digital.ConstellationWatermark;
import mini.xdab.digital.LSBWatermark;
import mini.xdab.digital.StripesWatermark;
import org.apache.commons.cli.CommandLine;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;


public class CommandLineArguments {

    protected CommandLine cmd;


    public CommandLineArguments(@NonNull CommandLine cmd) {
        this.cmd = cmd;
    }


    public String getInput() {
        return cmd.getOptionValue(CommandLineConstants.CL_ARGUMENT_INPUT);
    }

    public String getOutput() {
        return Optional
                .ofNullable(cmd.getOptionValue(CommandLineConstants.CL_ARGUMENT_OUTPUT))
                .orElseGet(this::getDefaultOutput);
    }

    public IWatermarkWriter getWriter() {
        var t = cmd.getOptionValue(CommandLineConstants.CL_ARGUMENT_TYPE);
        if (t == null || t.isEmpty())
            return getDefaultWM();

        t = t.toLowerCase(Locale.ROOT);
        switch (t) {
            case CommandLineConstants.CL_TYPE_ALIAS_LSB:
                return getLSBWM();
            case CommandLineConstants.CL_TYPE_ALIAS_STRIPES:
                return getStripesWM();
            case CommandLineConstants.CL_TYPE_ALIAS_BLOCKS:
                return getBlocksWM();
            case CommandLineConstants.CL_TYPE_ALIAS_CONSTELLATION:
                return getConstellationWM();
        }

        return getDefaultWM();
    }

    public Integer getRepeat() {
        int R = getDefaultRepeat();
        try { R = Integer.parseUnsignedInt(cmd.getOptionValue(CommandLineConstants.CL_ARGUMENT_REPEAT)); }
        catch (NumberFormatException nfe) { }
        return R;
    }

    public String getMessage() {
        return Optional
                .ofNullable(cmd.getOptionValue(CommandLineConstants.CL_ARGUMENT_MESSAGE))
                .orElseGet(this::getDefaultMessage);
    }

    //

    private String getDefaultOutput() {
        String[] inputSplitByDots = getInput().split("\\.");
        int fileExtensionIdx = inputSplitByDots.length - 1;
        if (fileExtensionIdx >= 0) {
            inputSplitByDots[fileExtensionIdx] = "_OUTPUT.png";
            return Arrays.stream(inputSplitByDots).reduce((a, b) -> a+b).orElse("output.png");
        }
        return "output.png";
    }

    private IWatermarkWriter getDefaultWM() {
        return getLSBWM();
    }

    private Integer getDefaultRepeat() {
        return 1;
    }

    private String getDefaultMessage() {
        return "WATERMARK";
    }

    //

    private LSBWatermark getLSBWM() {
        // todo: parse other arguments to set lsb specific options
        return new LSBWatermark();
    }

    private StripesWatermark getStripesWM() {
        // todo: parse other arguments to set stripes specific options
        return new StripesWatermark();
    }

    private BlocksWatermark getBlocksWM() {
        // todo: parse other arguments to set blocks specific options
        return new BlocksWatermark();
    }

    private ConstellationWatermark getConstellationWM() {
        // todo: parse other arguments to set constellation specific options
        return new ConstellationWatermark();
    }
}
