package mega.boirlerplate;

import robocode.*;

/**
 * 
 * @author Gates
 *
 */
public abstract class Movement extends Component {

    protected double speed;
    protected double rotation;

    /**
     * 
     * @param state
     */
    public Movement(State state) {
        super(state);
    }

    /**
     * 
     * @return
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * 
     * @return
     */
    public double getRotation() {
        return this.rotation;
    }

    /**
     * 
     * @param x
     * @param y
     * @param margin
     * @return
     */
    public boolean isOutOfBattleField(double x, double y, double margin) {
        Robot owner = this.state.owner;
        return Util.isOutOfBattleField(x, y, owner.getBattleFieldWidth(), owner.getBattleFieldHeight(),
                                       owner.getWidth()/2 + margin, // left
                                       owner.getHeight()/2 + margin, // top
                                       owner.getWidth()/2 + margin, // right
                                       owner.getHeight()/2 + margin); // bottom
    }

}
