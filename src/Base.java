package bot;

import robocode.*;

public abstract class Base extends Component {

    protected double speed;
    protected double rotation;

    public Base(State state) {
        super(state);
    }

    public double getSpeed() {
        return this.speed;
    }

    public double getRotation() {
        return this.rotation;
    }

    public boolean isOutOfBattleField(double x, double y, double margin) {
        Robot owner = this.state.owner;
        return Util.isOutOfBattleField(x, y, owner.getBattleFieldWidth(), owner.getBattleFieldHeight(),
                                       owner.getWidth()/2 + margin, // left
                                       owner.getHeight()/2 + margin, // top
                                       owner.getWidth()/2 + margin, // right
                                       owner.getHeight()/2 + margin); // bottom
    }

}
