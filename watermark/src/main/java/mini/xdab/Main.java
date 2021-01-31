package mini.xdab;

import mini.xdab.singleton.Log;
import mini.xdab.singleton.Options;
import mini.xdab.singleton.Random;
import mini.xdab.singleton.Strings;
import mini.xdab.tools.LSBVisualizer;
import mini.xdab.utils.ImageUtils;
import mini.xdab.utils.TextUtils;


public class Main
{

    public static void main(String[] args) {
        Options.init(args);
        if (Options.getKey() != null) {
            Random.seed(Options.getKey());
        }

        run();
    }


    private static void run() {
        var img = ImageUtils.loadFromFile(Options.getInput());
        if (img == null) {
            Log.error(null, "Main.run input image == null");
            throw new RuntimeException(Strings.get("image-not-found-exception-msg-fmt"));
        }

        if (Options.getRead()) {
            Log.info(null, "Main.run Reading");
            var reader = Options.getReader();
            String readMessage = reader.readString(img);
            System.out.println(readMessage);
        }

        else if (Options.getWrite()) {
            Log.info(null, "Main.run Writing");
            var writer = Options.getWriter();

            if (Options.getVisualizeLSBs()) {
                var inputLSBVizFileName = TextUtils.appendToFileName(Options.getInput(), "_LSBs");
                var inputLSBViz = LSBVisualizer.process(img);
                Log.info(null, "Saving input LSB viz -> %s", inputLSBVizFileName);
                ImageUtils.saveToFile(inputLSBViz, inputLSBVizFileName);
            }

            writer.write(img, Options.getMessage());
            Log.info(null, "Saving output -> %s", Options.getOutput());
            ImageUtils.saveToFile(img, Options.getOutput());

            if (Options.getVisualizeLSBs()) {
                var outputLSBVizFileName = TextUtils.appendToFileName(Options.getOutput(), "_LSBs");
                var outputLSBViz = LSBVisualizer.process(img);
                Log.info(null, "Saving output LSB viz -> %s", outputLSBVizFileName);
                ImageUtils.saveToFile(outputLSBViz, outputLSBVizFileName);
            }
        }
    }

}
