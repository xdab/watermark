package mini.xdab;

import org.apache.commons.cli.Option;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

class OptionComparator<T extends Option> implements Comparator<T> {

    private static final String OPTS_ORDER = "iotmrlsbchv"; // short option names

    @Override
    public int compare(@NotNull T o1, @NotNull T o2) {
        return OPTS_ORDER.indexOf(o1.getOpt()) - OPTS_ORDER.indexOf(o2.getOpt());
    }
    
}
