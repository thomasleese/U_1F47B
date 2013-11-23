package bot;

public class EnemyBullet {

    private Vector a;
    private Vector b;
    private double power;
    private long time;

    public EnemyBullet(double positionX, double positionY, double power, double angleD, long time) {
        double angleR = Math.toRadians(angleD);
        this.a = new Vector(positionX, positionY);
        this.b = new Vector(Math.sin(angleR) * this.getSpeed(), Math.cos(angleR) * this.getSpeed());
        this.power = power;
        this.time = time;
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
}