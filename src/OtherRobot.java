package bot;

import java.util.ArrayList;
import java.util.List;

public class OtherRobot {

    public static class Tick {
        
        public long time;
        public boolean isWatching;
        public double bearing;
        public double distance;
        public double energy;

        public Tick(long time, boolean isWatching) {
            this.time = time;
            this.isWatching = isWatching;
        }

        @Override
        public String toString() {
            return "Tick(time=" + this.time + ", isWatching=" + this.isWatching 
                + ", bearing=" + this.bearing + ", distance=" + this.distance
                + ", energy=" + this.energy + ")";
        }

    }

    private String name;
    private ArrayList<Tick> history = new ArrayList<Tick>(10000);

    public OtherRobot(String name) {
        this.name = name;
    }

    public List<Tick> getAllHistory() {
        return this.history;
    }

    public Tick getHistory(int index) {
        if (index < 0) {
            // for going backwards
            index = this.history.size() + index;
        }

        return this.history.get(index);
    }

    public void pushHistory(Tick tick) {
        this.history.add(tick);
    }

}
