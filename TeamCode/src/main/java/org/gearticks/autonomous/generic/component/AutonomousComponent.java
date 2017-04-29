package org.gearticks.autonomous.generic.component;

import java.util.Collection;

/**
 * Autonomous component has output "ports," each assigned a unique nonzero integer.
 * Interface is as simple as possible to allow for easy conversion.
 *
 * AutonomousComponent can be used in regular switch-like state-machine, but also by an automated state-machine.
 * See samples.
 */
public interface AutonomousComponent<TRANSITION_TYPE extends Enum<?>> {
	/**
	 * To be called once at start of autonomous program, e.g. to initialize some sensors
	 */
	void onMatchStart();

	/**
	 * Called each time the component state starts, can be multiple times in same autonomous program.
	 * Should contain all the code for resetting the state of the component.
	 */
	void setup();

	/**
	 * To be called in each loop cycle.
	 * Returns the "transition" or id of the "outputPort" through which this AutonomousComponent ends.
	 * This allows for choice between different transitions/routes to next AutonomousComponents.
	 *
	 * @return null if not yet done; the transition value if done
	 */
	TRANSITION_TYPE run();

	/**
	 * Called each time the component ends
	 */
	void tearDown();

	/**
	 * Returns a string identifier for this component
	 * @return a display name for this component
	 */
	String getId();

	/**
	 * Returns a collection of all possible transition values
	 * that this component can emit.
	 * @return the values in the enum declaration of TRANSITION_TYPE
	 */
	Collection<TRANSITION_TYPE> getPossibleTransitions();

	/**
	 * The default output port.
	 * Use if there is only one output port
	 */
	enum DefaultTransition {
		DEFAULT
	}
}
