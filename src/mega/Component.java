package mega;

import java.awt.Graphics2D;
import robocode.*;
import robocode.util.*;

/**
 * 
 * @author Gates
 *
 */
public abstract class Component {
    protected State state;

    /**
     * 
     * @param state
     */
    public Component(State state) {
        this.state = state;
    }

    /**
     * 
     */
    public abstract void execute();

    /**
     * 
     * @param e
     */
    public void onHitRobot(HitRobotEvent e) {
        // TODO: implement this
    }
    
    /**
     * 
     * @param g
     */
    public void onPaint(Graphics2D g) {
    	// TODO: implement this
    }
}
