package mega;

import java.awt.geom.Point2D;

/**
 * 
 * @author Gates
 *
 */
public class Vector extends Point2D.Double {

    public static final Vector ZERO = new Vector(0, 0);

    /**
     * 
     * @param x
     * @param y
     */
    public Vector(double x, double y) {
        super(x, y);
    }

    /**
     * 
     * @param copy
     */
    public Vector(Vector copy) {
        super(copy.getX(), copy.getY());
    }

    /**
     * 
     * @return
     */
    public double length() {
        return Math.sqrt(this.lengthSq());
    }

    /**
     * 
     * @return
     */
    public double lengthSq() {
        return this.x*this.x + this.y*this.y;
    }

    /**
     * 
     * @param v
     * @param coefficient
     * @return
     */
    public Vector add(Vector v, double coefficient) {
        return new Vector(this.getX() + coefficient * v.getX(),
                          this.getY() + coefficient * v.getY());
    }

    /**
     * 
     * @param coefficient
     * @return
     */
    public Vector mul(double coefficient) {
        return new Vector(coefficient * this.getX(),
                          coefficient * this.getY());
    }

    /**
     * 
     * @param coefficient
     * @return
     */
    public Vector div(double coefficient) {
        return new Vector(this.getX() / coefficient,
                          this.getY() / coefficient);
    }

    /**
     * 
     * @param angle
     * @param origin
     * @return
     */
    // degrees
    public Vector rotate(double angle, Vector origin)
    {
        double originX = origin.getX();
        double originY = origin.getY();
        double pointX = getX();
        double pointY = getY();
        double sinVar = (double)Math.sin(Math.toRadians(angle));
        double cosVar = (double)Math.cos(Math.toRadians(angle));
        double resX = originX + (pointX - originX) * cosVar - (pointY - originY) * sinVar;
        double resY = originY + (pointY - originY) * cosVar + (pointX - originX) * sinVar;
        return new Vector(resX, resY);
    }

    /**
     * 
     * @param v
     * @return
     */
    public Vector add(Vector v) {
        return this.add(v, 1.0);
    }

    /**
     * 
     * @return
     */
    public Vector square() {
        return new Vector(this.getX() * this.getX(), this.getY() * this.getY());
    }

    /**
     * 
     * @return
     */
    public double getAngle() {
        return Util.getAngle(-this.getX(), -this.getY());
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return "Vector(" + this.getX() + ", " + this.getY() +")";
    }

    /**
     * 
     * @param v
     * @return
     */
    public boolean equals(Vector v) {
        return this.getX() == v.getX() && this.getY() == v.getY();
    }

    /**
     * 
     * @param n
     * @return
     */
    public Vector round(int n) {
        return new Vector(Util.round(this.getX(), n), Util.round(this.getY(), n));
    }

    /**
     * 
     * @param xMin
     * @param xMax
     * @param yMin
     * @param yMax
     * @return
     */
    public Vector bound(double xMin, double xMax, double yMin, double yMax) {
        return new Vector(Util.clamp(this.getX(), xMin, xMax), Util.clamp(this.getY(), yMin, yMax));
    }
}
