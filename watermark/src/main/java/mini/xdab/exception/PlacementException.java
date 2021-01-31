package mini.xdab.exception;

import mini.xdab.singleton.Strings;

public class PlacementException extends Exception {

    public PlacementException(Integer objectSize, Integer totalSize, boolean sizeIsWidth) {
        super(String.format(Strings.get("placement-exception-msg-fmt"),
            sizeIsWidth ? Strings.get("width") : Strings.get("height"),
            objectSize,
            totalSize
        ));
    }

}
