package bot;

import robocode.*;
import robocode.util.*;

public abstract class Component {
    protected State state;

    public Component(State state) {
        this.state = state;
    }

    public abstract void execute();

    public void onHitRobot(HitRobotEvent e) {
        // implement this
    }
}