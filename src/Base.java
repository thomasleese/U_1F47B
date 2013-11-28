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
        return x + margin > this.state.owner.getBattleFieldWidth() -
                            (this.state.owner.getWidth() / 2) || // right edge
               x - margin < this.state.owner.getWidth() / 2 || // left edge
               y + margin > this.state.owner.getBattleFieldHeight() -
                            (this.state.owner.getHeight() / 2) || // top edge
               y - margin < this.state.owner.getHeight() / 2; // bottom edge
    }

    public void onHitRobot(HitRobotEvent e) {
        // implement this
    }

}
