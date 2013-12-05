package bot;

import java.awt.Color;
import java.awt.Graphics2D;
import robocode.*;
import robocode.util.*;

public class PredictiveBase extends Base {

    private Vector destination;

    public PredictiveBase(State state) {
        super(state);
    }

    private double evaluatePosition(Vector position) {
        double score = 0;

        // if it is out of bounds return a terrible score
        if (this.isOutOfBattleField(position.getX(), position.getY(), 16)) {
            return Double.NEGATIVE_INFINITY;
        }

        double minDistance = Double.POSITIVE_INFINITY;
        for (OtherRobot robot : this.state.otherRobots.values()) {
            OtherRobot.Tick tick = robot.getHistory(-1);
            Vector diff = tick.position.add(position, -1);
            if (diff.lengthSq() <= minDistance) {
                minDistance = diff.lengthSq();
            }
        }

        score = minDistance;

        return score;
    }

    private Vector pickBestPosition() {
        Vector origin = new Vector(this.state.owner.getX(), this.state.owner.getY());
        Vector radius = new Vector(0, 50);

        Vector currentDestination = null;
        double currentScore = Double.NEGATIVE_INFINITY;

        for (int angle = 0; angle < 360; angle += 45) {
            Vector destination = origin.add(radius.rotate(angle, Vector.ZERO), 1);
            double score = evaluatePosition(destination);
            if (score > currentScore) {
                currentScore = score;
                currentDestination = destination;
            }
        }

        return currentDestination;
    }

    @Override
    public void execute() {
        if (this.destination == null) {
            this.destination = pickBestPosition();
        }

        Vector position = new Vector(this.state.owner.getX(), this.state.owner.getY());
        Vector diff = this.destination.add(position, -1);

        double destinationAngle = diff.getAngle();
        double angleDiff = Utils.normalRelativeAngleDegrees(this.state.owner.getHeading() - destinationAngle);
        
        if (Math.abs(angleDiff) <= 15) {
            this.rotation = 0;
            this.speed = Double.POSITIVE_INFINITY;
        } else if (Math.abs(angleDiff) >= 165) {
            this.rotation = 0;
            this.speed = Double.NEGATIVE_INFINITY;
        } else {
            if (Math.abs(angleDiff) >= 90)
                this.rotation = angleDiff;
            else
                this.rotation = Utils.normalRelativeAngleDegrees(180 + angleDiff);
            this.speed = 0;
        }

        if (diff.lengthSq() <= 4 * 4) {
            this.destination = null;
        }
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {

    }

    @Override
    public void onPaint(Graphics2D g) {
        if (this.destination != null) {
            g.setColor(new Color(255, 0, 0, 255));
            g.drawRect((int) this.destination.getX() - 5, (int) this.destination.getY() - 5, 10, 10);
        }
    }

}
