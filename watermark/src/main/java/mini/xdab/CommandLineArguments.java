package mini.xdab;

import lombok.NonNull;
import mini.xdab.constants.CommandLineConstants;
import org.apache.commons.cli.CommandLine;

import java.util.Arrays;
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


    private String getDefaultOutput() {
        String[] inputSplitByDots = getInput().split("\\.");
        int fileExtensionIdx = inputSplitByDots.length - 1;
        if (fileExtensionIdx >= 0) {
            inputSplitByDots[fileExtensionIdx] = "_OUTPUT.png";
            return Arrays.stream(inputSplitByDots).reduce((a, b) -> a+b).orElse("output.png");
        }
        return "output.png";
    }

    private Integer getDefaultRepeat() {
        return 1;
    }

    private String getDefaultMessage() {
        return "WATERMARK";
    }
}
