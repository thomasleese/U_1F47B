package mega.boirlerplate;

/**
 * 
 * @author Gates
 *
 */
public abstract class Gun extends Component {

    protected double bulletPower;
    protected double rotation;
    protected boolean shouldFire = true;

    /**
     * 
     * @param state
     */
    public Gun(State state) {
        super(state);
    }

    /**
     * 
     * @return
     */
    public double getBulletPower() {
        return this.bulletPower;
    }

    /**
     * 
     * @return
     */
    public double getRotation() {
        return this.rotation;
    }

    /**
     * 
     * @return
     */
    public boolean getShouldFire() {
        return this.shouldFire;
    }

    /**
     * 
     * @param tb
     */
    public void firedBullet(BulletTracked tb) {
        // no default behavior
    }

    /**
     * 
     * @param tb
     */
    public void bulletHit(BulletTracked tb) {
        // no default behavior
    }

    /**
     * 
     * @param tb
     */
    public void bulletMissed(BulletTracked tb) {
        // no default behavior
    }

}
