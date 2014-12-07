package U_1F47B;

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

	public BulletWave(Vector position, double power, int startAngle, int endAngle, double confidence, State state) {
		this.power = power;
		this.confidence = confidence;
		this.bullets = new ArrayList<VirtualBullet>();
		for(int i = startAngle; i < endAngle; i++) {
			this.bullets.add(new VirtualBullet(position, this.power, Utils.normalAbsoluteAngleDegrees(i), state));
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

	public Vector getAveragePosition() {
		Vector averagePosition = new Vector(0, 0);
		for (VirtualBullet bullet : this.bullets) {
			averagePosition = averagePosition.add(bullet.getPosition());
		}

		return averagePosition.div(this.bullets.size());
	}

	public void onPaint(Graphics2D g) {
		g.setColor(new Color(0, 255, 0, (int)(255 * this.confidence)));
		for(VirtualBullet bullet : this.bullets) {
			bullet.onPaint(g);
		}

		g.setColor(new Color(255, 0, 0, (int)(255 * this.confidence)));
		Vector averagePos = this.getAveragePosition();
		int diameter = 6;
		g.fillArc((int)(averagePos.getX() - diameter/2), (int)(averagePos.getY() - diameter/2), diameter, diameter, 0, 360);
	}
}
