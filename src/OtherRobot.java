package bot;

import java.util.ArrayList;
import java.util.List;

public class OtherRobot implements Comparable<OtherRobot> {

    public static class Tick implements Comparable<Tick> {

        public long time;
        public double bearing;
        public double distance;
        public double energy;

        public Vector position;
        public Vector velocity;
        public double turnRate;

        public Tick(long time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "Tick(time=" + this.time + ", bearing=" + this.bearing
                + ", distance=" + this.distance + ", energy=" + this.energy + ")";
        }

        @Override
        public int compareTo(Tick t) {
            return this.time < t.time ?  1:
                   this.time > t.time ? -1:
                /* this.time ==t.time */ 0;
        }

    }
    
    public enum TurnBehaviours
    {
        noTurn,
        keepTurn,
        hardLeft,
        hardRight,
    }
    
    public enum SpeedBehaviours
    {
        keepSpeed,
        accel,
        reverse,
        stop,
    }
    
    public static class ProjectedBot
    {
        private double locX;
        private double locY;
        
        private double dir;
        private double speed;
        private double turnRate;
        
        /**
         * init a ProjectedBot from a tick
         */
        public ProjectedBot(Tick tick)
        {
            locX = tick.position.getX();
            locY = tick.position.getY();
            
            dir = tick.velocity.getAngle();
            speed = tick.velocity.length();
            turnRate = tick.turnRate;
        }
        
        /**
         * init a ProjectedBot from values
         */
        public ProjectedBot(double locXN, double locYN, double dirN, double speedN, double turnRateN)
        {
            locX = locXN;
            locY = locYN;
            
            dir = dirN;
            speed = speedN;
            turnRate = turnRateN;
        }
        
        public ProjectedBot clone()
        {
            return new ProjectedBot(locX, locY, dir, speed, turnRate);
        }
        
        public void project(int timeFrame, TurnBehaviours tb, SpeedBehaviours sb)
        {
            for (int i = 0; i < timeFrame; i++)
            {
                switch (sb)
                {
                    case keepSpeed:
                        break;
                    case accel:
                        if (speed < 8.0)
                        {
                            if (speed >= 0)
                                speed += 1.0;
                            else
                            {
                                speed += 2.0;
                                if (speed > 0)
                                    speed /= 2.0;
                            }
                        }
                        break;
                    case reverse:
                        if (speed >= -8.0)
                        {
                            if (speed <= 0)
                                speed += 1.0;
                            else
                            {
                                speed -= 2.0;
                                if (speed < 0)
                                    speed /= 2.0;
                            }
                        }
                        break;
                    case stop:
                        if (speed > 0)
                        {
                            speed -= 2.0;
                            if (speed < 0)
                                speed = 0;
                        }
                        else if (speed < 0)
                        {
                            speed += 2.0;
                            if (speed > 0)
                                speed = 0;
                        }
                        break;
                }
                
                switch (tb)
                {
                    case keepTurn:
                        break;
                    case noTurn:
                        turnRate = 0;
                        break;
                    case hardRight:
                        turnRate = 90;
                        break;
                    case hardLeft:
                        turnRate = -90;
                        break;
                }
                
                locX += Math.sin(Math.toRadians(dir)) * speed;
                locY += Math.cos(Math.toRadians(dir)) * speed;
                
                double clampedTR = Util.clamp(turnRate, -Util.speedToMaxTurnRate(speed), Util.speedToMaxTurnRate(speed));
                dir += clampedTR;
                
                if (locX < 16)
                {
                    locX = 16;
                    speed = 0;
                }
                if (locX > 800/*width of arena*/)
                {
                    locX = 800 - 16;
                    speed = 0;
                }
                if (locY < 16)
                {
                    locY = 16;
                    speed = 0;
                }
                if (locY > 600/*height of arena*/)
                {
                    locY = 600 - 16;
                    speed = 0;
                }
            }
        }
        
        public Vector getPosition()
        {  
            return new Vector(locX, locY);
        }
    }

    private String name;
    private ArrayList<Tick> history = new ArrayList<Tick>(10000);
    private ArrayList<VirtualBullet> bullets = new ArrayList<VirtualBullet>();

    public OtherRobot(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(OtherRobot robot) {
        return this.getHistory(-1).compareTo(robot.getHistory(-1));
    }

    public List<Tick> getAllHistory() {
        return this.history;
    }

    public Tick getHistory(int index) {
        if (this.history.size() == 0) {
            return null;
        }
        return this.history.get(Util.modulo(index, this.history.size()));
    }

    public int getHistorySize() {
        return this.history.size();
    }

    public void pushHistory(Tick tick) {
        this.history.add(tick);
    }

    public List<VirtualBullet> getAllBullets() {
        return this.bullets;
    }

    public VirtualBullet getBullet(int index) {
        if (this.bullets.size() == 0) {
            return null;
        }
        return this.bullets.get(Util.modulo(index, this.bullets.size()));
    }

    public String getName() {
        return this.name;
    }

    public double getGunHeat(long time) {
        VirtualBullet lastBullet = this.getBullet(-1);
        if (lastBullet == null) {
            return 0.0;
        }
        return (1 + (lastBullet.getPower()/5)) - (0.1 * (time - lastBullet.getTime()));
    }
    
    public Vector predictLocation(int timeFrame, TurnBehaviours tb, SpeedBehaviours sb)
    {
        ProjectedBot pb = new ProjectedBot(getHistory(-1));
        pb.project(timeFrame, tb, sb);
        return pb.getPosition();
    }
    
    public enum PresentHistoryDatas
    {
        none,
        positionOnly,
        positionVelocity,
        positionVelocityTurnRate,
    }
    
    public PresentHistoryDatas availablePresentHistoryData(long time)
    {
        if (time - 1 != getHistory(-1).time)
            return PresentHistoryDatas.none;
        else if (time - 2 != getHistory(-2).time)
            return PresentHistoryDatas.positionOnly;
        else if (time - 3 != getHistory(-3).time)
            return PresentHistoryDatas.positionVelocity;
        else
            return PresentHistoryDatas.positionVelocityTurnRate;
    }

    public boolean predictBulletShot(long time) {

        // check if it's possible that a bullet was shot
        if (this.getGunHeat(time) > 0) {
            return false;
        }

        OtherRobot.Tick previous = this.getHistory(-1);
        OtherRobot.Tick penultimate = this.getHistory(-2);

        if (previous == null || penultimate == null) {
            // we don't know what the shot power is
            // assume that no shot has been made
            return false;
        }

        double power = Util.roundTo1(penultimate.energy - previous.energy);

        // TODO: account for being hit by us

        // TODO: account for robot ramming into wall

        // TODO: account for robot ramming other robot

        // check if power is still less than allowed
        if (power < 0.1) {
            // assume that robot didn't shoot
            return false;
        }

        // something we didn't account for happened
        if (power > 3) {
            power = 3;
        }

        // TODO: predict angle of bullet
        // this.bullets.add(new VirtualBullet(position, power, angle, time));

        return true;
    }

}
