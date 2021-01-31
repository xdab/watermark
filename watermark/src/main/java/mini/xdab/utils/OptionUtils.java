package mini.xdab.utils;

import lombok.NonNull;
import mini.xdab.OptionComparator;
import mini.xdab.consts.OptionConsts;
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
        var input = new Option(OptionConsts.SHORT_ARGUMENT_INPUT, OptionConsts.LONG_ARGUMENT_INPUT,
                true, Strings.get("input-arg-desc"));
        input.setRequired(true);
        input.setArgName("FILE");
        options.addOption(input);

        var output = new Option(OptionConsts.SHORT_ARGUMENT_OUTPUT, OptionConsts.LONG_ARGUMENT_OUTPUT,
                true, Strings.get("output-arg-desc"));
        output.setRequired(false);
        output.setArgName("FILE");
        options.addOption(output);

        var type = new Option(OptionConsts.SHORT_ARGUMENT_TYPE, OptionConsts.LONG_ARGUMENT_TYPE,
                true, Strings.get("type-arg-desc"));
        type.setRequired(false);
        type.setArgName("NAME");
        options.addOption(type);

        var message = new Option(OptionConsts.SHORT_ARGUMENT_MESSAGE, OptionConsts.LONG_ARGUMENT_MESSAGE,
                true, Strings.get("message-arg-desc"));
        message.setRequired(false);
        message.setArgName("STRING");
        options.addOption(message);

        var repeat = new Option(OptionConsts.SHORT_ARGUMENT_REPEAT, OptionConsts.LONG_ARGUMENT_REPEAT,
                true, Strings.get("repeat-arg-desc"));
        repeat.setRequired(false);
        repeat.setArgName("NUMBER");
        options.addOption(repeat);

        var key = new Option(OptionConsts.SHORT_ARGUMENT_KEY, OptionConsts.LONG_ARGUMENT_KEY,
                true, Strings.get("key-arg-desc"));
        key.setRequired(false);
        key.setArgName("NUMBER");
        options.addOption(key);
    }

    private static void addQuickTypesOptionGroup(org.apache.commons.cli.Options options) {
        var quickTypes = new OptionGroup();

        var quickTypeLSB = new Option(OptionConsts.QUICK_TYPE_LSB,
                Strings.get("quick-type-lsb-arg-desc"));
        quickTypes.addOption(quickTypeLSB);

        var quickTypeStripes = new Option(OptionConsts.QUICK_TYPE_STRIPES,
                Strings.get("quick-type-stripes-arg-desc"));
        quickTypes.addOption(quickTypeStripes);

        var quickTypeConstellation = new Option(OptionConsts.QUICK_TYPE_CONSTELLATION,
                Strings.get("quick-type-constellation-arg-desc"));
        quickTypes.addOption(quickTypeConstellation);

        options.addOptionGroup(quickTypes);
    }

    private static void addFlags(org.apache.commons.cli.Options options) {

        var rwOptions = new OptionGroup();

        var read = new Option(OptionConsts.FLAG_READ,
                Strings.get("flag-read-arg-desc"));
        rwOptions.addOption(read);

        var write = new Option(OptionConsts.FLAG_WRITE,
                Strings.get("flag-write-arg-desc"));
        rwOptions.addOption(write);
        rwOptions.setRequired(true);

        options.addOptionGroup(rwOptions);

        var flagOptions = new OptionGroup();

        var horizontal = new Option(OptionConsts.FLAG_HORIZONTAL,
                Strings.get("flag-horizontal-arg-desc"));
        flagOptions.addOption(horizontal);

        var vertical = new Option(OptionConsts.FLAG_VERTICAL,
                Strings.get("flag-vertical-arg-desc"));
        flagOptions.addOption(vertical);

        options.addOptionGroup(flagOptions);

        var majority = new Option(OptionConsts.FLAG_MAJORITY,
                Strings.get("flag-majority-arg-desc"));
        options.addOption(majority);

        var visLSBs = new Option(OptionConsts.FLAG_VISUALIZE_LSBS,
                Strings.get("flag-visualize-lsbs-arg-desc"));
        options.addOption(visLSBs);
    }

}
