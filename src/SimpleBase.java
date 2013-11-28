package bot;

import robocode.*;

public class SimpleBase extends Base {

    private double userRotation;
    private boolean reverse;
    private boolean wasNearWall;

    public SimpleBase(State state, double rotation) {
        super(state);
        this.userRotation = rotation;
        this.reverse = false;
        this.wasNearWall = false;
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
            if (!this.wasNearWall) {
                this.reverse = !this.reverse;
                this.wasNearWall = true;
            }
        } else {
            this.wasNearWall = false;
        }

        this.rotation = this.userRotation;
        this.speed = Rules.MAX_VELOCITY * (this.reverse ? -1 : 1);
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        this.reverse = !this.reverse;
    }

}