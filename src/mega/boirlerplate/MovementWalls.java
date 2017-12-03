package mega.boirlerplate;

import robocode.*;
import robocode.util.*;

/**
 * 
 * @author Gates
 *
 */
public class MovementWalls extends Movement {

	private boolean gunIdent;
	private int dir;
	
	/**
	 * 
	 * @param state
	 */
	public MovementWalls(State state) {
		super(state);
	}

	/**
	 * 
	 */
	@Override
    public void execute() {
		int dir = 1;
		this.state.owner.getHeading();
		while(true) {
			if (Utils.isNear(this.state.owner.getHeadingRadians(), 0D) || Utils.isNear(this.state.owner.getHeadingRadians(), Math.PI)) {
				this.state.owner.ahead((Math.max(this.state.battleWidth - this.state.owner.getY(), this.state.owner.getY()) - 28) * dir);
			} else {
				this.state.owner.ahead((Math.max(this.state.battleWidth - this.state.owner.getX(), this.state.owner.getX()) - 28) * dir);
				this.state.owner.turnRight(90 * dir);
			}
		}
		
	}
}
