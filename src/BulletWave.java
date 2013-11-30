package bot;

import java.util.Iterator;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import robocode.*;
import robocode.util.*;

public class BulletWave {

	private ArrayList<VirtualBullet> bullets;
	private double power;
	private double confidence;

	public BulletWave(Vector position, double power, int resolution, double confidence) {
		this.power = power;
		this.confidence = confidence;
		this.bullets = new ArrayList<VirtualBullet>(resolution);
		for(int i = 0; i < resolution; i++) {
			this.bullets.add(new VirtualBullet(position.getX(), position.getY(), this.power, i));
		}
		this.advance();
	}

	public boolean advance() {
		Iterator<VirtualBullet> it = this.bullets.iterator();
		while (it.hasNext()) {
			if (!it.next().advance()) {
				it.remove();
			}
		}
		if (this.bullets.size() == 0) {
			return false;
		}
		return true;
	}

	public long getFlightTime() {
		return bullets.get(0).getFlightTime();
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