package bot;

public class RandomGun extends Gun {

	@Override
	public double getRotation() {
		return Math.random()*360 - 180;
	}

}