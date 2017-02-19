package org.gearticks.autonomous.generic.component;

import android.util.Log;
import org.gearticks.opmodes.utility.Utils;

/**
 * Implements the AutonomousComponent interface.
 * Adds
 * - Transition constants and a transition id generator
 * - toString()
 */
public abstract class AutonomousComponentAbstractImpl implements AutonomousComponent {
	/**
	 * The default output port.
	 * Use if there is only one output port
	 */
	public static final Transition NEXT_STATE = new Transition("Default");

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
	 * Returns null
	 */
	public Transition run() {
		return null;
	}

	/**
	 * Default is empty so subclass doesn't have to implement if not necessary
	 */
	public void tearDown() {
		Log.d(Utils.TAG, "TearDown of " + this.id);
	}

	public String getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return this.getId();
	}
}
