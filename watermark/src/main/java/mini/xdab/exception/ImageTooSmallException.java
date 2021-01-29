package mini.xdab.exception;

import java.util.ResourceBundle;

public class ImageTooSmallException extends Exception {

    private final ResourceBundle strings;

    public ImageTooSmallException(ResourceBundle strings) {
        this.strings = strings;
    }

}
