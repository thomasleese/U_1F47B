package bot;

public class RandomGun extends Gun {

    public RandomGun(State state) {
        super(state);
    }

    @Override
    public double getRotation() {
        return Math.random()*360 - 180;
    }

}