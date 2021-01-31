package mini.xdab;

import mini.xdab.consts.OptionConsts;
import org.apache.commons.cli.Option;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class OptionComparator<T extends Option> implements Comparator<T> {

    private static final String OPTS_ORDER =
            OptionConsts.FLAG_READ +
            OptionConsts.FLAG_WRITE +
            OptionConsts.SHORT_ARGUMENT_INPUT +
            OptionConsts.SHORT_ARGUMENT_OUTPUT +
            OptionConsts.SHORT_ARGUMENT_MESSAGE +
            OptionConsts.SHORT_ARGUMENT_TYPE +
            OptionConsts.SHORT_ARGUMENT_REPEAT +
            OptionConsts.SHORT_ARGUMENT_KEY +
            OptionConsts.FLAG_HORIZONTAL +
            OptionConsts.FLAG_VERTICAL +
            OptionConsts.FLAG_MAJORITY +
            OptionConsts.FLAG_VISUALIZE_LSBS +
            OptionConsts.TYPE_ALIAS_LSB +
            OptionConsts.TYPE_ALIAS_STRIPES +
            OptionConsts.TYPE_ALIAS_CONSTELLATION;

    @Override
    public int compare(@NotNull T o1, @NotNull T o2) {
        return OPTS_ORDER.indexOf(o1.getOpt()) - OPTS_ORDER.indexOf(o2.getOpt());
    }
    
}
