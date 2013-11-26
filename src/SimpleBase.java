package bot;

import robocode.Rules;
import robocode.BattleRules;

public class SimpleBase extends Base {

    private double overrideRotation;

    public SimpleBase(State state, double rotation) {
        super(state);
        this.rotation = rotation;
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
            this.overrideRotation = 100 * this.rotation;
            this.speed = 0;
        } else {
            this.overrideRotation = 0;
            this.speed = Rules.MAX_VELOCITY;
        }

        this.rotation = (this.overrideRotation == 0 ? this.rotation : this.overrideRotation);
    }

}