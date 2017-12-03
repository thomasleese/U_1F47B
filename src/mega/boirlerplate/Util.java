package mega.boirlerplate;

import java.util.Random;
import robocode.util.*;

/** Contains the collections framework, event model, utility classes and miscellaneous
 * 
 * @author VisualMelon
 * @author thomasleese
 * @author Cyanogenoid
 *
 */
public class Util {

    public static final Random RANDOM = new Random();
    public static final int ROBOT_WIDTH = 48;
    public static final int ROBOT_HEIGHT = 48;

    /**
     * 
     * @param c
     * @param mod
     * @return
     */
    public static int modulo(int c, int mod) {
        int result = c % mod;
        return result < 0 ? mod + result : result;
    }

    /**
     * 
     * @param val
     * @param lower
     * @param upper
     * @return
     */
    public static double clamp(double val, double lower, double upper) {
        if (val < lower)
            return lower;
        if (val > upper)
            return upper;
        return val;
    }

    /**
     * 
     * @param d
     * @param digits
     * @return
     */
    public static double round(double d, int digits) {
        double factor = Math.pow(10.0, (double)digits);
        return (double)Math.round(d * factor) / factor;
    }

    /**
     * 
     * @param d
     * @return
     */
    public static double roundTo1(double d) {
        return (double)Math.round(d * 10) / 10;
    }

    /**
     * 
     * @param firepower
     * @return
     */
    public static double firepowerToSpeed(double firepower) {
        return 20 - 3 * firepower;
    }

    /**
     * 
     * @param speed
     * @return
     */
    public static double speedToFirepower(double speed) {
        return (20 - speed) / 3;
    }

    /**
     * 
     * @param speed
     * @return
     */
    // degrees
    public static double speedToMaxTurnRate(double speed) {
        return (10 - 0.75 * Math.abs(speed));
    }

    /**
     * 
     * @param dx
     * @param dy
     * @return
     */
    public static double getDistance(double dx, double dy)
    {
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 
     * @param dx
     * @param dy
     * @return
     */
    public static double getAngle(double dx, double dy)
    {
        return -90 - Math.toDegrees(Math.atan2(dy, dx));
    }

    /**
     * 
     * @param ax
     * @param ay
     * @param bx
     * @param by
     * @return
     */
    public static double getDistance(double ax, double ay, double bx, double by)
    {
        double x = ax - bx;
        double y = ay - by;
        return getDistance(x, y);
    }

    /** a is origin
     * 
     * @param ax
     * @param ay
     * @param bx
     * @param by
     * @return
     */
    public static double getAngle(double ax, double ay, double bx, double by)
    {
        double x = ax - bx;
        double y = ay - by;
        return getAngle(x, y);
    }

    /**
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     * @param marginLeft
     * @param marginTop
     * @param marginRight
     * @param marginBottom
     * @return
     */
    public static boolean isOutOfBattleField(double x, double y, double width, double height,
                                             double marginLeft, double marginTop,
                                             double marginRight, double marginBottom) {
        return x + marginRight > width || /*<< right edge */
               x - marginLeft < 0 || /*<< left edge */
               y + marginTop > height || /*<< top edge */
               y - marginBottom < 0; /*<< bottom edge */
    }

    /**
     * 
     * @param position
     * @param width
     * @param height
     * @param marginLeft
     * @param marginTop
     * @param marginRight
     * @param marginBottom
     * @return
     */
    public static boolean isOutOfBattleField(Vector position, double width, double height,
                                             double marginLeft, double marginTop,
                                             double marginRight, double marginBottom) {
        return Util.isOutOfBattleField(position.getX(), position.getY(), width, height,
                                       marginLeft, marginTop, marginRight, marginBottom);
    }

    /**
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static boolean isOutOfBattleField(double x, double y, double width, double height) {
        return Util.isOutOfBattleField(x, y, width, height, 0, 0, 0, 0);
    }

    /**
     * 
     * @param position
     * @param width
     * @param height
     * @return
     */
    public static boolean isOutOfBattleField(Vector position, double width, double height) {
        return Util.isOutOfBattleField(position.getX(), position.getY(), width, height);
    }

    /**
     * 
     * @param angle
     * @return
     */
    public static double headinglessAngle(double angle) {
        double normalAngle = Utils.normalRelativeAngleDegrees(angle);
        double normalAngleReverse = Utils.normalRelativeAngleDegrees(angle + 180);
        if (Math.abs(normalAngle) < Math.abs(normalAngleReverse)) {
            return normalAngle;
        } else {
            return normalAngleReverse;
        }
    }
}
