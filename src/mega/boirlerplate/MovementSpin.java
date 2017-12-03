package mega.boirlerplate;

import robocode.*;
import robocode.util.*;

/**
 * 
 * @author Gates
 *
 */
public class MovementSpin extends Movement {

	/**
	 * 
	 * @param state
	 */
	public MovementSpin(State state) {
		super(state);
	}

	/**
	 * 
	 */
	@Override
    public void execute() {
		// Tell the game that when we take move,
		// we'll also want to turn right... a lot.
		this.rotation = 10000;
		// Limit our speed to 5
		this.speed = 5;
		// Start moving (and turning)
		this.state.owner.ahead(10000);
		// Repeat.
	}
	
	/**
	 * If it's our fault, we'll stop turning and moving, so we need to turn again to keep spinning.
	 */
	@Override
	public void onHitRobot(HitRobotEvent e) {
		if (e.isMyFault()) {
			this.rotation = 10;
		}
	}

}
