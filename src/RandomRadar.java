package bot;

public class RandomRadar extends Radar {

    public RandomRadar(State state) {
        super(state);
    }

    @Override
    public double getRotation() {
        return Math.random()*360 - 180;
    }

}