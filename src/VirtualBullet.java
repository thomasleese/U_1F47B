package bot;

import java.awt.Graphics2D;

public class VirtualBullet {

    private Vector position;
    private Vector velocity;
    private long flightTime;

    public VirtualBullet(double positionX, double positionY, double power, double angleD) {
        double angleR = Math.toRadians(angleD);
        double speed = Util.firepowerToSpeed(power);
        this.position = new Vector(positionX, positionY);
        this.velocity = new Vector(Math.sin(angleR) * speed, Math.cos(angleR) * speed);
        this.flightTime = 0;
    }

    public boolean advance() {
        this.flightTime++;
        this.position = this.position.add(this.velocity);
        if (Util.isOutOfBattleField(this.position.getX(), this.position.getY(), 800, 600)) {
            return false;
        }
        return true;
    }

    public Vector getPosition() {
        return this.position;
    }

    public double getPower() {
        return this.velocity.length();
    }

    public long getFlightTime() {
        return this.flightTime;
    }

    public void onPaint(Graphics2D g) {
        int diameter = 4;

        g.fillArc((int)(this.position.getX() - diameter/2), (int)(this.position.getY() - diameter/2), diameter, diameter, 0, 360);
    }
}
