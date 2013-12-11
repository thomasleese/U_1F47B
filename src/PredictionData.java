package U_1F47B;

import robocode.*;

public class PredictionData {

    private OtherRobot.PresentHistoryDatas phs;
    private ProjectedBot.TurnBehaviours tb;
    private ProjectedBot.SpeedBehaviours sb;
    private int timeFrame;
    
    public PredictionData(OtherRobot.PresentHistoryDatas phsN, ProjectedBot.TurnBehaviours tbN, ProjectedBot.SpeedBehaviours sbN, int timeFrameN) {
        phs = phsN;
        tb = tbN;
        sb = sbN;
        timeFrame = timeFrameN;
    }
    
    public OtherRobot.PresentHistoryDatas getPhs() {
        return phs;
    }
    
    public ProjectedBot.TurnBehaviours getTb() {
        return tb;
    }
    
    public ProjectedBot.SpeedBehaviours getSb() {
        return sb;
    }
    
    public int getTimeFrame() {
        return timeFrame;
    }
}
