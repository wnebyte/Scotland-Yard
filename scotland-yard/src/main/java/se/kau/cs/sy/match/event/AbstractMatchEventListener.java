package se.kau.cs.sy.match.event;

/**
 * Abstract super class that can be used to define match listeners.
 * Its {@code onEvent} method starts the handling of the event in a new
 * thread, ensuring non-blocking behaviour and not-so-great performance.
 * @author Sebastian Herold
 *
 */
public abstract class AbstractMatchEventListener implements MatchEventListener {

	public void onStateChangeEvent(StateChangeEvent event) {};
	
	public void onEvent(MatchEvent event) {
		new Thread() {
			public void run() {
				if (event instanceof StateChangeEvent) {
					onStateChange((StateChangeEvent) event);
				}
			}
		}.start();
	}
}
