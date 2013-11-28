package bot;

import java.awt.geom.Point2D;

public class Vector extends Point2D.Double {

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

    @Override
    public String toString() {
        return "Vector(" + this.getX() + ", " + this.getY() +")";
    }
}
