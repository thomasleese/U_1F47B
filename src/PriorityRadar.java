package bot;

import robocode.*;
import robocode.util.*;
import java.util.ArrayList;

public class PriorityRadar extends Radar {

    private OtherRobot.Tick lastSeen;

    public PriorityRadar(State state) {
        super(state);
        this.lastSeen = new OtherRobot.Tick(Long.MAX_VALUE);
    }

    @Override
    public void execute() {
        boolean reverse = false;
        if (this.state.trackingRobot == null && this.state.otherRobots.size() > 0) {
            // we need a pick a new robot to track and hopefully destroy
            int i = Util.RANDOM.nextInt(this.state.otherRobots.size());
            String key = new ArrayList<String>(this.state.otherRobots.keySet()).get(i); // a bit horrible
            this.state.trackingRobot = this.state.otherRobots.get(key);
        }

        if (this.state.otherRobots.size() == this.state.owner.getOthers()) {

            OtherRobot.Tick previousLastSeen = this.lastSeen;
            long maxLastSeen = Long.MAX_VALUE;

            for (OtherRobot r : this.state.otherRobots.values()) {
                if (r.getHistory(-1).time < maxLastSeen) {
                    this.lastSeen = r.getHistory(-1);
                    maxLastSeen = this.lastSeen.time;
                }
            }

            if (previousLastSeen == this.lastSeen) {
                // keep turning in the same direction
                return;
            }

            double rotation = Utils.normalRelativeAngleDegrees(this.lastSeen.bearing +
                                                               this.state.owner.getHeading() -
                                                               this.state.owner.getRadarHeading());
            reverse = rotation < 0;
        }

        this.rotation = reverse ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        this.state.trackingRobot = this.state.otherRobots.get(e.getName());
    }

}