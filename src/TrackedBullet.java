package U_1F47B;

import robocode.*;

public class TrackedBullet {

    private Bullet bullet;

	public TrackedBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    public Bullet getBullet() {
        return this.bullet;
    }
}
