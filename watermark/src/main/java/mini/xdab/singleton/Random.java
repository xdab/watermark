package mini.xdab.singleton;


import org.apache.commons.cli.CommandLine;

public class Random {

    private static java.util.Random random;

    private static void init() {
        Random.random = new java.util.Random();
    }

    private static java.util.Random getRandom() {
        if (random == null)
            Random.init();
        return random;
    }

    public static Integer getInt(Integer bound) {
        return getRandom().nextInt(bound);
    }

    public static Integer getInt(Integer from, Integer to) {
        if (from > to) {
            Log.warn(null, "Random.getInt(int,int) Swapping misordered values (from>to)");
            int t = from;
            from = to;
            to = t;
        }

        int bound = to - from + 1;
        return from + Random.getInt(bound);
    }

}
