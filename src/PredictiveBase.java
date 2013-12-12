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
    private Destination lastDestination;

    private double lastEnemyBearing;

    public PredictiveBase(State state) {
        super(state);
        this.actions = new Stack<Action>();
        this.destinations = new ArrayList<Destination>();
    }

    private double evaluatePosition(Vector position, double angle) {
        double score = 0.0;

        // if it is out of bounds return a terrible score
        if (this.isOutOfBattleField(position.getX(), position.getY(), 0)) {
            return Double.NEGATIVE_INFINITY;
        }

        double averageDistance = 0;
        double averageBearing = 0;
        int numberOfAverageBearing = 0;
        double averageEnemyBearing = 0;
        int numberOfAverageEnemyBearing = 0;
        double averageBulletPosition = 0;
        int numberOfAverageBulletPosition = 0;
        ArrayList<OtherRobot> closestRobots = new ArrayList<OtherRobot>(this.state.otherRobots.size());
        for (OtherRobot robot : this.state.otherRobots.values()) {
            OtherRobot.Tick tick = robot.getHistory(-1);
            Vector diff = tick.position.add(position, -1);

            double diffLength = diff.lengthSq();
            boolean closerFound = false;
            for (OtherRobot otherRobot : this.state.otherRobots.values()) {
                if (robot == otherRobot) {
                    continue;
                }
                if (tick.position.add(otherRobot.getHistory(-1).position, -1).lengthSq() < diffLength) {
                    closerFound = true;
                    break;
                }
            }
            if (!closerFound) {
                closestRobots.add(robot);
            }

            averageDistance += diffLength * (tick.energy/this.state.owner.getEnergy());
            for (int i = 0; (closerFound ? i < 1 : i < 2); i++) {
                averageBearing += Util.headinglessAngle(diff.getAngle() - angle);
                numberOfAverageBearing++;
            }
            OtherRobot.Tick previous = robot.getHistory(-2);
            Vector currentPositionRounded  = tick.position.round(3);
            Vector previousPositionRounded = previous.position.round(3);
            if (!currentPositionRounded.equals(previousPositionRounded)) {
                averageEnemyBearing += currentPositionRounded.add(previousPositionRounded, -1).getAngle() - diff.getAngle();
                numberOfAverageEnemyBearing++;
            }

            for (BulletWave wave : robot.getAllBullets()) {
                averageBulletPosition += position.add(wave.getAveragePosition(), -1).lengthSq();
                numberOfAverageBulletPosition++;
            }
        }

        if (numberOfAverageBulletPosition != 0) {
            averageBulletPosition /= numberOfAverageBulletPosition;
            score += Math.sqrt(averageBulletPosition) / 20;
            System.out.print("b: " + Math.sqrt(averageBulletPosition) / 20);
        }

        if (this.state.otherRobots.size() != 0) {
            averageDistance /= this.state.otherRobots.size();
            score += Math.sqrt(averageDistance) / 10;
            System.out.print(", d: " + Math.sqrt(averageDistance) / 10);

            averageBearing = Util.headinglessAngle(averageBearing / numberOfAverageBearing);
            score += Math.abs(averageBearing);
            System.out.print(", b: " + Math.abs(averageBearing));

            if (numberOfAverageEnemyBearing > 0) {
                this.lastEnemyBearing = averageEnemyBearing / numberOfAverageEnemyBearing;
            }
            score += Math.abs(Util.headinglessAngle(this.lastEnemyBearing + 90) / 3);
            System.out.print(", h: " + Math.abs(Util.headinglessAngle(this.lastEnemyBearing + 90)));

            double pScore = score;
            score += (this.state.otherRobots.size() - closestRobots.size()) * 100 / this.state.otherRobots.size();
            System.out.print(", f: " + (score - pScore));

            if (this.state.otherRobots.size() > 1) {
                Vector[] gravity = new Vector[4];
                gravity[0] = new Vector(100, 300);
                gravity[1] = new Vector(700, 300);
                gravity[2] = new Vector(400, 100);
                gravity[3] = new Vector(400, 500);
                pScore = score;
                for (Vector v : gravity) {
                    score += (300 / v.add(position, -1).length());
                }
                System.out.print(", r: " + (score - pScore));
            }

        }

        if (this.lastDestination != null) {
            double pScore = score;
            score += this.lastDestination.position.add(position, -1).length() / 10;
            System.out.print(", l: " + (score - pScore));
        }


        if (score < 0) {
            score = 0;
        }

        System.out.println();
        return score;
    }

    private Destination pickBestPosition() {
        Vector origin = new Vector(this.state.owner.getX(), this.state.owner.getY());

        Destination currentDestination = null;
        double currentScore = Double.NEGATIVE_INFINITY;

        this.destinations.clear();

        int base = 250;
        int max  = 550;
        int step = 75;
        if (this.state.owner.getOthers() == 1) {
            base = 200;
            max = 250;
            step = 25;
        }

        for (int i = base; i <= max; i += step)
        {
            Vector radius = new Vector(0, i * Math.random()+32);

            for (int angle = 0; angle < 360; angle += 10) {
                Vector pointGenerator = radius.rotate(angle, Vector.ZERO);
                Vector destination = origin.add(pointGenerator, 1).bound(0, 800, 0, 600);

                double score = evaluatePosition(destination, pointGenerator.getAngle());
                Destination d = new Destination(destination.bound(32, 768, 32, 568), score);
                this.destinations.add(d);
                if (score > currentScore) {
                    currentScore = score;
                    currentDestination = d;
                }
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

        if (diff.lengthSq() <= 16 * 16) {
            this.lastDestination = this.destination;
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
        this.destination = null;
    }

    @Override
    public void onPaint(Graphics2D g) {
        if (this.destination != null) {

            if (this.destinations.size() != 0) {
                for (Destination d : this.destinations) {
                    int score = (int)Util.clamp((d.score/this.destination.score) * 255, 0, 255);
                    g.setColor(new Color(128, 128, 128, score));
                    g.drawRect((int)d.position.getX() - 5, (int)d.position.getY() - 5, 10, 10);
                }
            }

            g.setColor(new Color(255, 0, 0, 255));
            g.drawRect((int) this.destination.position.getX() - 5, (int) this.destination.position.getY() - 5, 10, 10);
        }
    }
}