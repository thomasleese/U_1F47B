package bot;

public class RandomBase extends Base {

    public RandomBase(State state) {
        super(state);
    }

    @Override
    public double getSpeed() {
        return Math.random()*16 - 8;
    }

    @Override
    public double getRotation() {
        return Math.random()*360 - 180;
    }

}