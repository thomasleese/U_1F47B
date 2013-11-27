package bot;

import robocode.Rules;
import robocode.BattleRules;

public class SimpleBase extends Base {

    private double userRotation;

    public SimpleBase(State state, double rotation) {
        super(state);
        this.userRotation = rotation;
    }

    @Override
    public void execute() {
        double xDirection = Math.sin(Math.toRadians(this.state.owner.getHeading()));
        double yDirection = Math.cos(Math.toRadians(this.state.owner.getHeading()));

        double xPosition = this.state.owner.getX() + xDirection * this.state.owner.getVelocity();
        double yPosition = this.state.owner.getY() + yDirection * this.state.owner.getVelocity();

        for (double velocity = Math.min(this.state.owner.getVelocity() + Rules.ACCELERATION,
                                        Rules.MAX_VELOCITY); // if we try to go at max speed
             velocity > 0.0;
             velocity -= Rules.DECELERATION) {

            xPosition += xDirection * velocity;
            yPosition += yDirection * velocity;
        }

        // check if we're going into a wall
        if (this.isOutOfBattleField(xPosition, yPosition)) {
            this.rotation = 100 * this.userRotation;
            this.speed = 0;
        } else {
            this.rotation = this.userRotation;
            this.speed = Rules.MAX_VELOCITY;
        }
    }

}