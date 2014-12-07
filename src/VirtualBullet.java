package U_1F47B;

import java.awt.Graphics2D;

public class VirtualBullet {

    private Vector position;
    private Vector velocity;
    private long flightTime;
    private State state;

    public VirtualBullet(Vector position, double power, double angleD, State state) {
        double angleR = Math.toRadians(angleD);
        double speed = Util.firepowerToSpeed(power);
        this.position = new Vector(position);
        this.velocity = new Vector(Math.sin(angleR) * speed, Math.cos(angleR) * speed);
        this.flightTime = 0;
        this.state = state;
    }

    public boolean advance() {
        this.flightTime++;
        this.position = this.position.add(this.velocity);
        if (Util.isOutOfBattleField(this.position.getX(), this.position.getY(), state.battleWidth, state.battleHeight)) {
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
