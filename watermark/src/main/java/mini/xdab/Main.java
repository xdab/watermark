package mini.xdab;

import mini.xdab.singleton.Log;
import mini.xdab.singleton.Options;
import mini.xdab.utils.ImageUtils;


public class Main
{

    public static void main(String[] args) {
        Options.init(args);
        run();
    }


    private static void run() {
        var img = ImageUtils.loadFromFile(Options.getInput());

        if (Options.getRead()) {
            Log.info("Main.run Reading");
            var reader = Options.getReader();
            String readMessage = reader.readString(img);
            System.out.println(readMessage);
        }

        else if (Options.getWrite()) {
            Log.info("Main.run Writing");
            var writer = Options.getWriter();
            writer.write(img, Options.getMessage());
            ImageUtils.saveToFile(img, Options.getOutput());
        }
    }

}
