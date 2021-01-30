package mini.xdab.exception;

import mini.xdab.singleton.Strings;

public class PlacementException extends Exception {

    public PlacementException(Integer objectSize, Integer totalSize, boolean sizeIsWidth) {
        super(String.format(Strings.getString("placement-exception-msg-fmt"),
            sizeIsWidth ? Strings.getString("width") : Strings.getString("height"),
            objectSize,
            totalSize
        ));
    }

}
