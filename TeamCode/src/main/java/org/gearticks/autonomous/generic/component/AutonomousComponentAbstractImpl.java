package org.gearticks.autonomous.generic.component;

import android.util.Log;
import java.util.Arrays;
import java.util.Collection;
import org.gearticks.Utils;

/**
 * Implements the AutonomousComponent interface.
 * Adds
 * - Transition constants and a transition id generator
 * - toString()
 */
public abstract class AutonomousComponentAbstractImpl<TRANSITION_TYPE extends Enum<?>> implements AutonomousComponent<TRANSITION_TYPE> {
	private final Class<TRANSITION_TYPE> transitionClass;
	protected final String id;

	/**
	 * Creates a component using the class name as the ID
	 * @param transitionClass the {@link Class} this component transitions with
	 */
	public AutonomousComponentAbstractImpl(Class<TRANSITION_TYPE> transitionClass) {
		this.transitionClass = transitionClass;
		this.id = this.getClass().getSimpleName();
	}
	/**
	 * Creates a component with the specified ID
	 * @param transitionClass the {@link Class} this component transitions with
	 * @param id the debugging ID to use
	 */
	public AutonomousComponentAbstractImpl(Class<TRANSITION_TYPE> transitionClass, String id) {
		this.transitionClass = transitionClass;
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
	 * Always returns null (never finished).
	 */
	public TRANSITION_TYPE run() {
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

	public Collection<TRANSITION_TYPE> getPossibleTransitions() {
		return Arrays.asList(this.transitionClass.getEnumConstants());
	}

	@Override
	public String toString() {
		return this.getId();
	}
}
