package bot;

public class Vector extends java.awt.geom.Point2D.Double {

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
}
