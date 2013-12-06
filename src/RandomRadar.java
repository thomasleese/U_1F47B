package U_1F47B;

public class RandomRadar extends Radar {

    public RandomRadar(State state) {
        super(state);
    }

    @Override
    public void execute() {
        this.rotation = Math.random()*360 - 180;
    }

}
