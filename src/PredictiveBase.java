package U_1F47B;

import java.util.ArrayList;
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

    private class Destination {
        public Vector position;
        public double score;

        public Destination(Vector position, double score) {
            this.position = position;
            this.score = score;
        }
    }

    private Stack<Action> actions;
    private ArrayList<Destination> destinations;
    private Destination destination;

    public PredictiveBase(State state) {
        super(state);
        this.actions = new Stack<Action>();
        this.destinations = new ArrayList<Destination>();
    }

    private double evaluatePosition(Vector position) {
        double score = 1000.0;

        // if it is out of bounds return a terrible score
        if (this.isOutOfBattleField(position.getX(), position.getY(), 16)) {
            return Double.NEGATIVE_INFINITY;
        }

        double averageBearing = 0;
        double minDistance = Double.POSITIVE_INFINITY;
        for (OtherRobot robot : this.state.otherRobots.values()) {
            OtherRobot.Tick tick = robot.getHistory(-1);
            Vector diff = tick.position.add(position, -1);
            if (diff.lengthSq() <= minDistance) {
                minDistance = diff.lengthSq();
            }
            averageBearing += diff.getAngle();
        }

        score += minDistance;
        if (this.state.otherRobots.size() != 0) {
            averageBearing = Util.headinglessAngle(averageBearing / this.state.otherRobots.size());
            score -= Math.abs(90 - Math.abs(averageBearing));
        }
        System.out.println("Got score of " + score);
        if (score < 0) {
            score = 0;
        }
        return score;
    }

    private Destination pickBestPosition() {
        Vector origin = new Vector(this.state.owner.getX(), this.state.owner.getY());
        Vector radius = new Vector(0, 100);

        Destination currentDestination = null;
        double currentScore = Double.NEGATIVE_INFINITY;

        this.destinations.clear();

        for (int angle = 0; angle < 360; angle += 45) {
            Vector destination = origin.add(radius.rotate(angle, Vector.ZERO), 1);
            double score = evaluatePosition(destination);
            Destination d = new Destination(destination, score);
            this.destinations.add(d);
            if (score > currentScore) {
                currentScore = score;
                currentDestination = d;
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
        Vector diff = this.destination.position.add(position, -1);

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

            if (this.destinations.size() != 0) {
                for (Destination d : this.destinations) {
                    int score = (int)Util.clamp((d.score/this.destination.score) * 255, 0, 255);
                    System.out.println(score);
                    g.setColor(new Color(128, 128, 128, score));
                    g.drawRect((int)d.position.getX() - 5, (int)d.position.getY() - 5, 10, 10);
                }
            }

            g.setColor(new Color(255, 0, 0, 255));
            g.drawRect((int) this.destination.position.getX() - 5, (int) this.destination.position.getY() - 5, 10, 10);
        }
    }

}
