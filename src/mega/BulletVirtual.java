package mega;

import java.awt.Graphics2D;
/**
 * 
 * @author Cyanogenoid
 * @author thomasleese
 * @author rikkiprince
 *
 */
public class BulletVirtual {

    private Vector position;
    private Vector velocity;
    private long flightTime;
    private State state;
    
    /**
     * 
     * @param position
     * @param power
     * @param angleD
     * @param state
     */
    public BulletVirtual(Vector position, double power, double angleD, State state) {
        double angleR = Math.toRadians(angleD);
        double speed = Util.firepowerToSpeed(power);
        this.position = new Vector(position);
        this.velocity = new Vector(Math.sin(angleR) * speed, Math.cos(angleR) * speed);
        this.flightTime = 0;
        this.state = state;
    }
    
    /**
     * 
     * @return
     */
    public boolean advance() {
        this.flightTime++;
        this.position = this.position.add(this.velocity);
        if (Util.isOutOfBattleField(this.position.getX(), this.position.getY(), state.battleWidth, state.battleHeight)) {
            return false;
        }
        return true;
    }
    
    /**
     * 
     * @return
     */
    public Vector getPosition() {
        return this.position;
    }
    
    /**
     * 
     * @return
     */
    public double getPower() {
        return this.velocity.length();
    }
    
    /**
     * 
     * @return
     */
    public long getFlightTime() {
        return this.flightTime;
    }
    
    /**
     * 
     * @param g
     */
    public void onPaint(Graphics2D g) {
        int diameter = 4;

        g.fillArc((int)(this.position.getX() - diameter/2), (int)(this.position.getY() - diameter/2), diameter, diameter, 0, 360);
    }
}
