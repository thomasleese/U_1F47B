package U_1F47B;

import java.awt.geom.Point2D;

public class Vector extends Point2D.Double {

    public static final Vector ZERO = new Vector(0, 0);

    public Vector(double x, double y) {
        super(x, y);
    }

    public double length() {
        return Math.sqrt(this.lengthSq());
    }

    public double lengthSq() {
        return this.x*this.x + this.y*this.y;
    }

    public Vector add(Vector v, double coefficient) {
        return new Vector(this.getX() + coefficient * v.getX(),
                          this.getY() + coefficient * v.getY());
    }

    public Vector mul(double coefficient) {
        return new Vector(coefficient * this.getX(),
                          coefficient * this.getY());
    }

    public Vector div(double coefficient) {
        return new Vector(this.getX() / coefficient,
                          this.getY() / coefficient);
    }

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

    public Vector add(Vector v) {
        return this.add(v, 1.0);
    }

    public Vector square() {
        return new Vector(this.getX() * this.getX(), this.getY() * this.getY());
    }

    public double getAngle() {
        return Util.getAngle(-this.getX(), -this.getY());
    }

    @Override
    public String toString() {
        return "Vector(" + this.getX() + ", " + this.getY() +")";
    }

    public boolean equals(Vector v) {
        return this.getX() == v.getX() && this.getY() == v.getY();
    }

    public Vector round(int n) {
        return new Vector(Util.round(this.getX(), n), Util.round(this.getY(), n));
    }

    public Vector bound(double xMin, double xMax, double yMin, double yMax) {
        return new Vector(Util.clamp(this.getX(), xMin, xMax), Util.clamp(this.getY(), yMin, yMax));
    }
}
