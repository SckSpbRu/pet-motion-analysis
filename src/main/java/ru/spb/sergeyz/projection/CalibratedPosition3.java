package ru.spb.sergeyz.projection;

public class CalibratedPosition3 {

    private static double[][] v3d = new double[][]{
            new double[]{42, 77, 90, 160, 160},
            new double[]{162, 220, 318, 288, 205},
            new double[]{0, 0, 0, 0, 0},
            new double[]{1, 1, 1, 1, 1}
    };

    private static double[][] v2d = new double[][]{
            new double[]{286, 244, 98, 239, 358},
            new double[]{80, 271, 428, 555, 500},
            new double[]{0, 0, 0, 0, 0},
            new double[]{1, 1, 1, 1, 1}
    };

    private static double[][] v2dTesting = new double[][]{
            new double[]{320, 18, 190, 358},
            new double[]{60, 275, 577, 496},
            new double[]{0, 0, 0, 0},
            new double[]{1, 1, 1, 1}
    };

    public static double[][] getV3d() {
        return v3d;
    }

    public static double[][] getV2d() {
        return v2d;
    }

    public static double[][] getV2dTesting() {
        return v2dTesting;
    }
}
