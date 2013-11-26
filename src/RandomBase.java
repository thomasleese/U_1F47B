package bot;

public class RandomBase extends Base {

    public RandomBase(State state) {
        super(state);
    }

    @Override
    public void execute() {
        this.speed = Math.random()*16 - 8;
        this.rotation = Math.random()*360 - 180;
    }

}