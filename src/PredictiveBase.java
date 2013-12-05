package bot;

import java.util.Stack;
import java.awt.Color;
import java.awt.Graphics2D;
import robocode.*;
import robocode.util.*;

public class PredictiveBase extends Base {

    private class Action {
        public double speed;
        public double angle;

        public Action(double speed, double angle) {
            this.speed = speed;
            this.angle = angle;
        }
    }

    private Stack<Action> actions;
    private Vector destination;

    public PredictiveBase(State state) {
        super(state);
        this.actions = new Stack<Action>();
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

        if (this.actions.size() == 0) {
            this.generateActions(diff);
        }

        Action action = this.actions.pop();
        this.rotation = action.angle;
        this.speed = action.speed;

        if (diff.lengthSq() <= 4 * 4) {
            this.destination = null;
        }
    }

    private void generateActions(Vector diff) {
        double destinationAngle = diff.getAngle();
        double angleDiff = Utils.normalRelativeAngleDegrees(destinationAngle - this.state.owner.getHeading());

        double anglePerDistance = Util.headinglessAngle(angleDiff) / diff.length();

        // rearrange rotation rate <-> speed formula, substitute rotation rate with anglePerDistance*speed, rearrange again
        double requiredSpeed = 40 / (3 + 4 * anglePerDistance);

        if (Math.abs(angleDiff) > 90) {
            requiredSpeed *= -1;
        }

        this.actions.push(new Action(requiredSpeed, Util.headinglessAngle(angleDiff)));
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