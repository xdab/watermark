package mini.xdab;

import lombok.SneakyThrows;
import mini.xdab.constants.CommandLineConstants;
import org.apache.commons.cli.*;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Hello world!
 *
 */
public class App 
{
    @SneakyThrows
    public static void main(String[] args )
    {
        var strings = ResourceBundle.getBundle("strings", Locale.getDefault());

        var options = new Options();

        var input = new Option(CommandLineConstants.SHORT_ARGUMENT_INPUT, CommandLineConstants.LONG_ARGUMENT_INPUT,
                true, strings.getString("input-arg-desc"));
        input.setRequired(true);
        options.addOption(input);

        var output = new Option(CommandLineConstants.SHORT_ARGUMENT_OUTPUT, CommandLineConstants.LONG_ARGUMENT_OUTPUT,
                true, strings.getString("output-arg-desc"));
        output.setRequired(false);
        options.addOption(output);

        var type = new Option(CommandLineConstants.SHORT_ARGUMENT_TYPE, CommandLineConstants.LONG_ARGUMENT_TYPE,
                true, strings.getString("type-arg-desc"));
        type.setRequired(false);
        options.addOption(type);

        var message = new Option(CommandLineConstants.SHORT_ARGUMENT_MESSAGE, CommandLineConstants.LONG_ARGUMENT_MESSAGE,
                true, strings.getString("message-arg-desc"));
        message.setRequired(false);
        options.addOption(message);

        var repeat = new Option(CommandLineConstants.SHORT_ARGUMENT_REPEAT, CommandLineConstants.LONG_ARGUMENT_REPEAT,
                true, strings.getString("repeat-arg-desc"));
        repeat.setRequired(false);
        options.addOption(repeat);

        var parser = new DefaultParser();
        var formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("watermark", options);
            System.exit(1);
        }

        var cla = new CommandLineArguments(cmd);
        WatermarkApp.runBasedOnCommandLineArguments(cla);
    }
}
