package mini.xdab;

import java.awt.*;
import java.util.Random;

public class Placement {

    // Vertical
    public static final int TOP = 0b10;
    public static final int BOTTOM = 0b01;
    public static final int Y_RANDOM = 0b11;
    //public static final int Y_CENTER = 0b00;

    // Horizontal
    public static final int LEFT = 0b1000;
    public static final int RIGHT = 0b0100;
    public static final int X_RANDOM = 0b1100;
    //public static final int X_CENTER = 0b0000;

    // Overall
    public static final int CENTER = 0b0000;
    public static final int RANDOM = Y_RANDOM | X_RANDOM;

    // Masks
    protected static final int Y_MASK = 0b11;
    protected static final int X_MASK = 0b1100;


    // Static utility functions

    protected static int getX(int objectWidth, int totalWidth, int placement) {
        if (objectWidth > totalWidth)
            throw new RuntimeException("Positioning object wider than total width");
        if (objectWidth == totalWidth)
            return 0;

        switch (placement & X_MASK) {
            case LEFT:
                return 0;
            case RIGHT:
                return totalWidth - objectWidth - 1;
            case X_RANDOM:
                return Random.getInt(totalWidth - objectWidth);
            default: case CENTER:
                return (totalWidth - objectWidth - 1) / 2;
        }
    }


    protected static int getY(int objectHeight, int totalHeight, int placement) {
        if (objectHeight > totalHeight)
            throw new RuntimeException("Positioning object taller than total height");
        if (objectHeight == totalHeight)
            return 0;

        switch (placement & Y_MASK) {
            case TOP:
                return 0;
            case BOTTOM:
                return totalHeight - objectHeight - 1;
            case Y_RANDOM:
                return Random.getInt(totalHeight - objectHeight);
            default: case CENTER:
                return (totalHeight - objectHeight - 1) / 2;
        }
    }


    public static Point getPosition(int objectWidth, int objectHeight, int totalWidth, int totalHeight, int placement){
        int x = getX(objectWidth, totalWidth, placement);
        int y = getY(objectHeight, totalHeight, placement);
        return new Point(x, y);
    }

}
