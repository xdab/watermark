package mini.xdab;

import mini.xdab.constants.OptionConstants;
import org.apache.commons.cli.Option;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class OptionComparator<T extends Option> implements Comparator<T> {

    private static final String OPTS_ORDER =
            OptionConstants.FLAG_READ +
            OptionConstants.FLAG_WRITE +
            OptionConstants.SHORT_ARGUMENT_INPUT +
            OptionConstants.SHORT_ARGUMENT_OUTPUT +
            OptionConstants.SHORT_ARGUMENT_MESSAGE +
            OptionConstants.SHORT_ARGUMENT_TYPE +
            OptionConstants.SHORT_ARGUMENT_REPEAT +
            OptionConstants.FLAG_HORIZONTAL +
            OptionConstants.FLAG_VERTICAL +
            OptionConstants.FLAG_MAJORITY +
            OptionConstants.TYPE_ALIAS_LSB +
            OptionConstants.TYPE_ALIAS_STRIPES +
            OptionConstants.TYPE_ALIAS_BLOCKS +
            OptionConstants.TYPE_ALIAS_CONSTELLATION;

    @Override
    public int compare(@NotNull T o1, @NotNull T o2) {
        return OPTS_ORDER.indexOf(o1.getOpt()) - OPTS_ORDER.indexOf(o2.getOpt());
    }
    
}
