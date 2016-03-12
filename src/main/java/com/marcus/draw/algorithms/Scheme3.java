package com.marcus.draw.algorithms;

import javafx.scene.paint.Color;

/**
 * @author marcus
 */
public class Scheme3  implements DetermineColor {

    @Override
    public Color pixelColor(int noIts) {
        int val1 = Math.abs(noIts - 255) % 255;
        int val2 = Math.abs(noIts*3) % 255;
        int val3 = Math.abs(noIts*4  * noIts ) % 255;

        return Color.rgb(val1, val2, val3, 1.0);
    }
}
