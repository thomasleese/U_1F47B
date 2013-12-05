package bot;

import robocode.*;
import robocode.util.*;

public class SimpleBase extends Base {

    private double userRotation;
    private boolean reverse;
    private boolean wasReversing;
    private boolean wasTurning;
    private int reversingFor;

    public SimpleBase(State state, double rotation) {
        super(state);
        this.userRotation = rotation;
        this.reverse = false;
        this.wasReversing = false;
        this.wasTurning = false;
        this.reversingFor = 0;
    }

    @Override
    public void execute() {
        double xDirection = Math.sin(Math.toRadians(this.state.owner.getHeading()));
        double yDirection = Math.cos(Math.toRadians(this.state.owner.getHeading()));

        double xPosition = this.state.owner.getX() + xDirection * this.state.owner.getVelocity();
        double yPosition = this.state.owner.getY() + yDirection * this.state.owner.getVelocity();

        for (double velocity = Math.min(Math.abs(this.state.owner.getVelocity()) + Rules.ACCELERATION,
                                        Rules.MAX_VELOCITY) *
                               (this.reverse ? -1 : 1 ); // if we try to go at max speed
             velocity * (this.reverse ? -1 : 1 ) > 0.0;
             velocity -= Rules.DECELERATION * (this.reverse ? -1 : 1 )) {

            xPosition += xDirection * velocity;
            yPosition += yDirection * velocity;
        }

        // check if we're going into a wall
        if (this.isOutOfBattleField(xPosition, yPosition, 0)) {
            if (!this.wasReversing) {
                this.reverse = !this.reverse;
                this.wasReversing = true;
            }
        } else {
            this.wasReversing = false;
        }

        if (Math.random() > 0.99 && !this.wasTurning) {
            this.userRotation = -this.userRotation;
        }

        // take average of bearing to all other robots
        double avgAngle = 0;
        for(OtherRobot robot : this.state.otherRobots.values())
        {
            OtherRobot.Tick tick = robot.getHistory(-1);
            avgAngle += tick.bearing;
        }
        avgAngle = Utils.normalRelativeAngleDegrees(avgAngle / this.state.otherRobots.size());

        // turn if we're at too steep of an angle, but only if we haven't tried turning
        if ((avgAngle < 45 && avgAngle > -45) ||
            (avgAngle > 135 || avgAngle < -135)) {
            if(!this.wasTurning) {
                this.userRotation = -this.userRotation;
                this.wasTurning = true;
            }
        } else {
            this.wasTurning = false;
        }

        this.rotation = this.userRotation;
        this.speed = Rules.MAX_VELOCITY * (this.reverse ? -1 : 1);
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        // make sure that we don't get stuck in a reverse loop
        if (this.reversingFor < 3) {
            this.reverse = !this.reverse;
            this.reversingFor++;
        } else {
            this.reversingFor = 0;
        }
    }

}
