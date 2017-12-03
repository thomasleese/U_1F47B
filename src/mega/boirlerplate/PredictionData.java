package mega.boirlerplate;

import robocode.*;

/**
 * 
 * @author Gates
 *
 */
public class PredictionData {

    private OtherRobot.PresentHistoryDatas phs;
    private ProjectedBot.TurnBehaviours tb;
    private ProjectedBot.SpeedBehaviours sb;
    private int timeFrame;
    
    /**
     * 
     * @param phsN
     * @param tbN
     * @param sbN
     * @param timeFrameN
     */
    public PredictionData(OtherRobot.PresentHistoryDatas phsN, ProjectedBot.TurnBehaviours tbN, ProjectedBot.SpeedBehaviours sbN, int timeFrameN) {
        phs = phsN;
        tb = tbN;
        sb = sbN;
        timeFrame = timeFrameN;
    }
    
    /**
     * 
     * @return
     */
    public OtherRobot.PresentHistoryDatas getPhs() {
        return phs;
    }
    
    /**
     * 
     * @return
     */
    public ProjectedBot.TurnBehaviours getTb() {
        return tb;
    }
    
    /**
     * 
     * @return
     */
    public ProjectedBot.SpeedBehaviours getSb() {
        return sb;
    }
    
    /**
     * 
     * @return
     */
    public int getTimeFrame() {
        return timeFrame;
    }
}
