/*!
 *  GuessFactor (GF) is a way of measuring firing angles which, unlike a raw bearing offset, takes into account
 *   the enemy's relative direction at fire time and the maximum escape angle for the specific firing situation.
 *  
 *  /image GuessFactorsExplanation.png
 *  
 *  See more at: http://robowiki.net/wiki/Guess_Factor
 */
package mega;

/**
 * 
 * 
 * @author None
 *
 */
public class GunGuessFactor extends Gun {

	/**
	 * 
	 * @param state The particular condition that someone or something is in at a specific time.
	 */
	public GunGuessFactor(State state) {
		super(state);
	}

	/**
	 * 
	 */
	@Override
	public void execute() {		
	}

}
