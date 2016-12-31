package org.gearticks.autonomous.generic.component;

import android.util.Log;
import org.gearticks.opmodes.utility.Utils;
import java.util.logging.Logger;

/**
 * Implements the AutonomousComponent interface.
 * Adds
 * - Transition constants and a transition id generator
 * - toString()
 */
public abstract class AutonomousComponentAbstractImpl implements AutonomousComponent {
	/**
	 * Returning this from run() means that the component is not ready to transition to the next one
	 */
	public static final int NOT_DONE = 0;
	/**
	 * The default output port.
	 * Use if there is only one output port
	 */
	public static final int NEXT_STATE = 1;
	private static int firstUnusedTransition = Math.max(NOT_DONE, NEXT_STATE) + 1;
	/**
	 * Gets a unique transition id
	 * @return the lowest unused transition id
	 */
	public static int newTransition() {
		return firstUnusedTransition++;
	}

	protected final String id;

	public AutonomousComponentAbstractImpl() {
		this.id = this.getClass().getSimpleName();
	}

	public AutonomousComponentAbstractImpl(String id) {
		this.id = id;
	}

	/**
	 * Default is empty so subclass doesn't have to implement if not necessary
	 */
	public void onMatchStart() {}

	/**
	 * Default is empty so subclass doesn't have to implement if not necessary
	 */
	public void setup() {
		Log.d(Utils.TAG, "Setup of " + this.id);
	}

	/**
	 * Default is empty so subclass doesn't have to implement if not necessary.
	 * Returns NOT_DONE
	 */
	public int run() {
		return NOT_DONE;
	}

	/**
	 * Default is empty so subclass doesn't have to implement if not necessary
	 */
	public void tearDown() {
		Log.d(Utils.TAG, "TearDown of " + this.id);
	}

	@Override
	public String toString() {
		return this.id;
	}
}
