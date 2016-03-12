package com.marcus.draw.algorithms;

import javafx.scene.paint.Color;


/**
 * @author marcus
 */
public class Scheme1 implements DetermineColor {

    @Override
    public Color pixelColor(int noIts) {
        int val1 = noIts % 255;
        int val2 = Math.abs((noIts * noIts)) % 255;
        int val3 = Math.abs(noIts * noIts * noIts) % 255;

        return Color.rgb(val1, val2, val3, 1.0);
    }
}



