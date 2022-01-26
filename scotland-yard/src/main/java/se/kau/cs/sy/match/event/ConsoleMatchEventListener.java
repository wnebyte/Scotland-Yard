package se.kau.cs.sy.match.event;

/**
 * A pretty simple listener that prints new states to the standard console.
 * @author Sebastian Herold
 *
 */
public class ConsoleMatchEventListener extends AbstractMatchEventListener {

	@Override
	public void onStateChange(StateChangeEvent event) {
		System.out.println(event.getNewState());
	}

}
