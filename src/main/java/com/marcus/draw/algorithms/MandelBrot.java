package com.marcus.draw.algorithms;
import com.marcus.draw.DrawingApp;

import static java.lang.Math.*;
/**
 * @author marcus
 */
public class MandelBrot implements Algorithm {



    @Override
    public int execute(double a, double b) {

        // z = z2 + c
        // z = (x + iy)(x+iy) + a + bi
        // z = ( x2 + 2iyx -y2 + a + bi
        // z = x2 - y2 + a = i(2yx + b)


        // iterate point z0 = x +iy until the distance is > 2
        int noIts = 0;

        double x1 = 0;
        double y1 = 0;

        double currx = x1;
        double curry = y1;

        while (noIts < DrawingApp.MAX_ITS){

            noIts ++;
            x1 = currx*currx - curry*curry + a;
            y1 = 2*currx*curry + b;

            double length =  x1*x1 + y1*y1;

            if ( length > 4) {
               int mu = (int) (noIts + 1 - log (log(sqrt(length))) / log(2));
                return mu;
            }
            currx = x1;
            curry = y1;

        }

        return 0;
    }
}
