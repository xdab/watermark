package mini.xdab.exception;

import mini.xdab.singleton.Strings;


public class ImageTooSmallException extends Exception {

    public ImageTooSmallException(Integer watermarkSizePx, Integer imageSizePx) {
        super(String.format(Strings.getString("image-too-small-exception-msg-fmt"), watermarkSizePx, imageSizePx));
    }

}
