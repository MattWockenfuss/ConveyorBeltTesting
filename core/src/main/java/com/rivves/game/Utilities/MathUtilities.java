package com.rivves.game.Utilities;

public class MathUtilities {

    public static boolean isBetween(float value, float bound1, float bound2) {
        return (value >= Math.min(bound1, bound2) && value <= Math.max(bound1, bound2));
    }



}
