package bot;

import java.awt.Color;
import java.awt.Graphics2D;
import robocode.*;
import robocode.util.*;

public class BulletWave {

	private Vector position;
	private double power;
	private long time;

	public BulletWave(Vector position, double power, long time) {
		this.position = position;
		this.power = power;
		this.time = time;
	}

	public double getPower() {
		return this.power;
	}

	public long getTime() {
		return this.time;
	}

	public void onPaint(Graphics2D g, long time) {
		g.setColor(new Color(0, 255, 0));

		double a = 2 * Util.firepowerToSpeed(this.power) * (time + 1 - this.time);
		System.out.println(a);
		g.drawArc((int)(this.position.getX() - a/2), (int)(this.position.getY() - a/2), (int)a, (int)a, 0, 360);
	}
}