package mini.xdab;

import mini.xdab.singleton.Log;
import mini.xdab.singleton.Options;
import mini.xdab.singleton.Strings;
import mini.xdab.utils.ImageUtils;
import mini.xdab.utils.OptionUtils;

/**
 * Hello world!
 *
 */
public class Main
{

    public static void main(String[] args) {
        Options.init(args);
        run();
    }


    private static void run() {
        var writer = Options.getWriter();
        var reader = Options.getReader();

        var img = ImageUtils.loadFromFile(Options.getInput());

        writer.write(img, Options.getMessage());
        ImageUtils.saveToFile(img, Options.getOutput());

        if (reader != null) {
            var img2 = ImageUtils.loadFromFile(Options.getOutput());
            String readMessage = reader.readString(img2);
            System.out.println("Decoded message: '" + readMessage + "'");
        }
    }

}
