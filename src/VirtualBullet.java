package bot;

import java.awt.Graphics2D;

public class VirtualBullet {

    private Vector a;
    private Vector b;
    private double power;
    private long time;

    public VirtualBullet(double positionX, double positionY, double power, double angleD, long time) {
        this.power = power;
        this.time = time;
        double angleR = Math.toRadians(angleD);
        this.a = new Vector(positionX, positionY);
        this.b = new Vector(Math.sin(angleR) * this.getSpeed(), Math.cos(angleR) * this.getSpeed());
    }

    public Vector getPosition(long time) {
        return this.a.add(this.b, time - this.time);
    }

    public double getPower() {
        return this.power;
    }

    public double getSpeed() {
        return 20 - (3 * this.getPower());
    }

    public long getTime() {
        return this.time;
    }

    public void onPaint(Graphics2D g, long time) {
        int diameter = 4;

        Vector position = this.getPosition(time);
        g.fillArc((int)(position.getX() - diameter/2), (int)(position.getY() - diameter/2), diameter, diameter, 0, 360);
    }
}
