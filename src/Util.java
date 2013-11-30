package bot;

import java.util.Random;

public class Util {

    public static final Random RANDOM = new Random();
    public static final int ROBOT_WIDTH = 48;
    public static final int ROBOT_HEIGHT = 48;

    public static int modulo(int c, int mod) {
        int result = c % mod;
        return result < 0 ? mod + result : result;
    }

    public static double clamp(double val, double lower, double upper) {
        if (val < lower)
            return lower;
        if (val > upper)
            return upper;
        return val;
    }

    public static double round(double d, int digits) {
        double factor = Math.pow(10.0, (double)digits);
        return (double)Math.round(d * factor) / factor;
    }

    public static double roundTo1(double d) {
        return (double)Math.round(d * 10) / 10;
    }

    public static double firepowerToSpeed(double firepower) {
        return 20 - 3 * firepower;
    }

    public static double speedToFirepower(double speed) {
        return (20 - speed) / 3;
    }

    // degrees
    public static double speedToMaxTurnRate(double speed) {
        return (10 - 0.75 * Math.abs(speed));
    }

    public static double getDistance(double dx, double dy)
    {
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double getAngle(double dx, double dy)
    {
        return -90 - Math.toDegrees(Math.atan2(dy, dx));
    }

    public double getDistance(double ax, double ay, double bx, double by)
    {
        double x = ax - bx;
        double y = ay - by;
        return getDistance(x, y);
    }

    /**
     * a is origin
     */
    public double getAngle(double ax, double ay, double bx, double by)
    {
        double x = ax - bx;
        double y = ay - by;
        return getAngle(y, x);
    }

    public static boolean isOutOfBattleField(double x, double y, double width, double height,
                                             double marginLeft, double marginTop,
                                             double marginRight, double marginBottom) {
        return x + marginRight > width || // right edge
               x - marginLeft < 0 || // left edge
               y + marginTop > height || // top edge
               y - marginBottom < 0; // bottom edge
    }

    public static boolean isOutOfBattleField(Vector position, double width, double height,
                                             double marginLeft, double marginTop,
                                             double marginRight, double marginBottom) {
        return Util.isOutOfBattleField(position.getX(), position.getY(), width, height,
                                       marginLeft, marginTop, marginRight, marginBottom);
    }

    public static boolean isOutOfBattleField(double x, double y, double width, double height) {
        return Util.isOutOfBattleField(x, y, width, height, 0, 0, 0, 0);
    }

    public static boolean isOutOfBattleField(Vector position, double width, double height) {
        return Util.isOutOfBattleField(position.getX(), position.getY(), width, height);
    }
}
