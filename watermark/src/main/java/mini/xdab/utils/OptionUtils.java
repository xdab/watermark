package mini.xdab.utils;

import lombok.NonNull;
import mini.xdab.OptionComparator;
import mini.xdab.constants.OptionConstants;
import mini.xdab.singleton.Strings;
import org.apache.commons.cli.*;

public class OptionUtils {

    public static CommandLine parseArgsToCmd(@NonNull String[] args) {
        var options = new org.apache.commons.cli.Options();

        addArgumentOptions(options);
        addQuickTypesOptionGroup(options);
        addFlags(options);

        var parser = new DefaultParser();
        var formatter = new HelpFormatter();
        formatter.setOptionComparator(new OptionComparator<Option>());

        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("watermark", options);
            System.exit(1);
        }

        return cmd;
    }

    private static void addArgumentOptions(org.apache.commons.cli.Options options) {
        var input = new Option(OptionConstants.SHORT_ARGUMENT_INPUT, OptionConstants.LONG_ARGUMENT_INPUT,
                true, Strings.getString("input-arg-desc"));
        input.setRequired(true);
        input.setArgName("FILE");
        options.addOption(input);

        var output = new Option(OptionConstants.SHORT_ARGUMENT_OUTPUT, OptionConstants.LONG_ARGUMENT_OUTPUT,
                true, Strings.getString("output-arg-desc"));
        output.setRequired(false);
        output.setArgName("FILE");
        options.addOption(output);

        var type = new Option(OptionConstants.SHORT_ARGUMENT_TYPE, OptionConstants.LONG_ARGUMENT_TYPE,
                true, Strings.getString("type-arg-desc"));
        type.setRequired(false);
        type.setArgName("NAME");
        options.addOption(type);

        var message = new Option(OptionConstants.SHORT_ARGUMENT_MESSAGE, OptionConstants.LONG_ARGUMENT_MESSAGE,
                true, Strings.getString("message-arg-desc"));
        message.setRequired(false);
        message.setArgName("STRING");
        options.addOption(message);

        var repeat = new Option(OptionConstants.SHORT_ARGUMENT_REPEAT, OptionConstants.LONG_ARGUMENT_REPEAT,
                true, Strings.getString("repeat-arg-desc"));
        repeat.setRequired(false);
        repeat.setArgName("NUMBER");
        options.addOption(repeat);
    }

    private static void addQuickTypesOptionGroup(org.apache.commons.cli.Options options) {
        var quickTypes = new OptionGroup();

        var quickTypeLSB = new Option(OptionConstants.QUICK_TYPE_LSB,
                Strings.getString("quick-type-lsb-arg-desc"));
        quickTypes.addOption(quickTypeLSB);

        var quickTypeStripes = new Option(OptionConstants.QUICK_TYPE_STRIPES,
                Strings.getString("quick-type-stripes-arg-desc"));
        quickTypes.addOption(quickTypeStripes);

        var quickTypeBlocks = new Option(OptionConstants.QUICK_TYPE_BLOCKS,
                Strings.getString("quick-type-blocks-arg-desc"));
        quickTypes.addOption(quickTypeBlocks);

        var quickTypeConstellation = new Option(OptionConstants.QUICK_TYPE_CONSTELLATION,
                Strings.getString("quick-type-constellation-arg-desc"));
        quickTypes.addOption(quickTypeConstellation);

        options.addOptionGroup(quickTypes);
    }

    private static void addFlags(org.apache.commons.cli.Options options) {
        var flagOptions = new OptionGroup();

        var horizontal = new Option(OptionConstants.FLAG_HORIZONTAL,
                Strings.getString("flag-horizontal-arg-desc"));
        flagOptions.addOption(horizontal);

        var vertical = new Option(OptionConstants.FLAG_VERTICAL,
                Strings.getString("flag-vertical-arg-desc"));
        flagOptions.addOption(vertical);

        options.addOptionGroup(flagOptions);

        var majority = new Option(OptionConstants.FLAG_MAJORITY,
                Strings.getString("flag-majority-arg-desc"));
        options.addOption(majority);

        var visLSBs = new Option(OptionConstants.FLAG_VISUALIZE_LSBS,
                Strings.getString("flag-visualize-lsbs-arg-desc"));
        options.addOption(visLSBs);
    }

}
