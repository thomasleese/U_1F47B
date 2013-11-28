package bot;

public class Util {

    public static int modulo(int c, int mod) {
        int result = c % mod;
        return result < 0 ? mod + result : result;
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

}