package bot;

import java.awt.Color;
import robocode.*;

public class Bot extends RateControlRobot {

    public Bot() {
        
    }

    @Override
    public void run() {
        setColors(new Color(0, 40, 43), 
                  new Color(0, 96, 102),
                  new Color(0, 120, 128));
        
        while (true) {
            // temporary robot logic for testing
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        System.out.println("Seen a robot: " + e);
    }

}
