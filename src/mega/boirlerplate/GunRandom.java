/*!
 *  A Random Gun implementation based in sample.random code. They are good examples of various simple strategies.
 *  Beating all the Sample Bots is also a good early benchmark for new Robocoders before moving on to more sophisticated opponents.
 *  
 *  See more at: http://robowiki.net/wiki/Sample_Bots
 */
package mega.boirlerplate;

/**
 * 
 * @author MatheusRV
 *
 */
public class GunRandom extends Gun {

	/**
	 * 
	 * @param state
	 */
    public GunRandom(State state) {
        super(state);
    }

    /**
     * 
     */
    @Override
    public void execute() {
        this.rotation = Math.random()*360 - 180;
    }

}
