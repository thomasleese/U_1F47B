package bot;

public class RandomRadar extends Radar {

	@Override
	public double getRotation() {
		return Math.random()*360 - 180;
	}

}