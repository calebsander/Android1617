package org.gearticks.autonomous.generic.component;

import java.util.logging.Logger;

/**
 * Implements the AutonomousComponent interface.
 * Adds
 * - setup() without a input port number as a convenience method
 * - toString()
 *
 */
public abstract class AutonomousComponentAbstractImpl implements AutonomousComponent {
	public static final int NOT_DONE = 0;
	public static final int NEXT_STATE = 1;
	private static int firstUnusedTransition = Math.max(NOT_DONE, NEXT_STATE) + 1;
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
	public void initialize() {}

	/**
	 * Default is empty so subclass doesn't have to implement if not necessary
	 */
	public void setup() {}

	/**
	 * Default is empty so subclass doesn't have to implement if not necessary.
	 * Returns 0;
	 */
	public int run() {
		return NOT_DONE;
	}

	/**
	 * Default is empty so subclass doesn't have to implement if not necessary
	 */
	public void tearDown() {}

	@Override
	public String toString() {
		return this.id;
	}

	public Logger getLogger() {
		return Logger.getLogger(this.getClass().getSimpleName());
	}
}
