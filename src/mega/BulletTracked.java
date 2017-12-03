package mega;

import robocode.*;
/**
 * 
 * @author Gates
 *
 */
public class BulletTracked {

    private Bullet bullet;
    private PredictionData data;
    
    /**
     * 
     * @param bullet
     */
	public BulletTracked(Bullet bullet) {
        this.bullet = bullet;
    }
	
	/**
	 * 
	 * @return
	 */
    public Bullet getBullet() {
        return this.bullet;
    }
    
    /**
     * 
     * @param dataN
     */
    public void setData(PredictionData dataN) {
        data = dataN;
    }
    
    /**
     * 
     * @return
     */
    public PredictionData getData() {
        return data;
    }
}
