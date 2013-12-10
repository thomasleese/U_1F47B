package U_1F47B;

import robocode.*;

public class TrackedBullet {

    private Bullet bullet;
    private PredictionData data;

	public TrackedBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    public Bullet getBullet() {
        return this.bullet;
    }
    
    public void setData(PredictionData dataN) {
        data = dataN;
    }
    
    public PredictionData getData() {
        return data;
    }
}
