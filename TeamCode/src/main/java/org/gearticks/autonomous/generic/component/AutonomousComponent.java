package org.gearticks.autonomous.generic.component;

import java.util.logging.Logger;

/**
 * Autonomous component has output "ports," each assigned a unique nonzero integer.
 * Interface is as simple as possible to allow for easy conversion.
 *
 * AutonomousComponent can be used in regular switch-like state-machine, but also by an automated state-machine.
 * See samples.
 */
public interface AutonomousComponent {
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
	 * @return "outputPort," where AutonomousComponentAbstractImpl.NOT_DONE means that the component is not done
	 */
	int run();

	/**
	 * Called each time the component ends
	 */
	void tearDown();

	/**
	 * Gets a Logger to which information can be appended for this component
	 *
	 * @return a Logger unique to this component or this component class
	 */
	Logger getLogger();
}
