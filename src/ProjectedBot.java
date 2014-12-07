package U_1F47B;

public class ProjectedBot
{

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

    private double locX;
    private double locY;

    private double dir;
    private double speed;
    private double turnRate;

    /**
     * init a ProjectedBot from a tick
     */
    public ProjectedBot(OtherRobot.Tick tick)
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

    public void project(int timeFrame, TurnBehaviours tb, SpeedBehaviours sb, State state)
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
                            speed -= 1.0;
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
            
            if (speed > 8.0)
                speed = 8.0;
            if (speed < -8.0)
                speed = -8.0;

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
            if (locX > state.battleWidth - 16/*width of arena*/)
            {
                locX = state.battleWidth - 16;
                speed = 0;
            }
            if (locY < 16)
            {
                locY = 16;
                speed = 0;
            }
            if (locY > state.battleHeight - 16 /*height of arena*/)
            {
                locY = state.battleHeight - 16;
                speed = 0;
            }
        }
    }

    public Vector getPosition()
    {
        return new Vector(locX, locY);
    }
}
