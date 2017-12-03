/*!
 *  A Simple Gun implementation based in sample code. They are good examples of various simple strategies.
 *  Beating all the Sample Bots is also a good early benchmark for new Robocoders before moving on to more sophisticated opponents.
 *  
 *  See more at: http://robowiki.net/wiki/Sample_Bots
 */
package mega.boirlerplate;

import robocode.*;
import robocode.util.*;
/** GunSimple extends Gun
 * Create a simple rotational calc to shot enemy
 * 
 * @author MatheusRV
 *
 */
public class GunSimple extends Gun {
	
    private double coefficient;

    /**
     * A constructor
     * Initialize Object  
     * @param state 
     * @param coefficient a integer argument.
     */
    public GunSimple(State state, double coefficient) {
        super(state); 
        this.coefficient = coefficient;
    }
    /**
     * 
     * @param bearing
     * @return
     */
    private double calculateRotation(double bearing) {
        double rotation = bearing + this.state.owner.getHeading() - /**< absolute rotation to enemy */
                          this.state.owner.getRadarHeading();   /**< relative rotation to gun */
        return this.coefficient * Utils.normalRelativeAngleDegrees(rotation); /**< normalise */
    }

    @Override
    /**
     * 
     */
    public void execute() {
        if (this.state.trackingRobot != null) {
            OtherRobot.Tick tick = this.state.trackingRobot.getHistory(-1);
            this.rotation = this.calculateRotation(tick.bearing);
        } else {
            this.rotation = Double.POSITIVE_INFINITY;
        }
    }

}
