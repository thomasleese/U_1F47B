package U_1F47B;

import java.awt.Graphics2D;
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

    public void onPaint(Graphics2D g) {
        // implement this
    }
}
