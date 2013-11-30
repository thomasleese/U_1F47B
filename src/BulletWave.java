package bot;

import java.awt.Color;
import java.awt.Graphics2D;
import robocode.*;
import robocode.util.*;

public class BulletWave {

	private VirtualBullet[] bullets;
	private double power;
	private long time;
	private double confidence;

	public BulletWave(Vector position, double power, long time, int resolution, double confidence) {
		this.power = power;
		this.time = time;
		this.confidence = confidence;
		this.bullets = new VirtualBullet[resolution];
		for(int i = 0; i < resolution; i++) {
			this.bullets[i] = new VirtualBullet(position.getX(), position.getY(), this.power, i, this.time);
		}
	}

	public double getPower() {
		return this.power;
	}

	public long getTime() {
		return this.time;
	}

	public void onPaint(Graphics2D g, long time) {
		g.setColor(new Color(0, 255, 0, (int)(255 * this.confidence)));
		for(VirtualBullet bullet : this.bullets) {
			bullet.onPaint(g, time);
		}
	}
}