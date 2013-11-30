package bot;

import java.awt.Color;
import java.awt.Graphics2D;
import robocode.*;
import robocode.util.*;

public class BulletWave {

	private VirtualBullet[] bullets;
	private double power;
	private double confidence;

	public BulletWave(Vector position, double power, int resolution, double confidence) {
		this.power = power;
		this.confidence = confidence;
		this.bullets = new VirtualBullet[resolution];
		for(int i = 0; i < resolution; i++) {
			this.bullets[i] = new VirtualBullet(position.getX(), position.getY(), this.power, i);
		}
		this.advance();
	}

	public void advance() {
		for (VirtualBullet bullet : this.bullets) {
			bullet.advance();
		}
	}

	public long getFlightTime() {
		return bullets[0].getFlightTime();
	}

	public double getPower() {
		return this.power;
	}

	public void onPaint(Graphics2D g) {
		g.setColor(new Color(0, 255, 0, (int)(255 * this.confidence)));
		for(VirtualBullet bullet : this.bullets) {
			bullet.onPaint(g);
		}
	}
}