package org.gearticks.autonomous.generic.component;

import java.util.logging.Logger;

/**
 * Autonomous component has input and output 'ports', with numbers starting at 1.
 * Interface is as simple as possible to allow for easy conversion.
 *
 * AutonomousComponent can be used in regular switch-like state-machine, but also by an automated state-machine.
 * See samples.
 *
 */
public interface AutonomousComponent {
	/**
	 * To be called once at start of autonomous program, e.g. to initialize some sensors
	 */
	void initialize();


	/**
	 * Called each time the component state starts, can be multiple times in same autonomous program.
	 * The inputPort argument allows the setup to be dependent on the port through which iterator is triggered.
	 * For regular single-input-single-output components, this is not relevant.
	 */
	void setup();

	/**
	 * To be called in each loop cycle.
	 * Returns the 'transition' or  id of the 'outputPort' through which this AutonomousComponent ends.
	 * This allows for choice between different transitions/routes to next AutonomousComponents.
	 *
	 * @return 'outputPort', 0 means not ready
	 */
	int run();

	/**
	 * Called each time the component state ends
	 */
	void tearDown();

	Logger getLogger();

}
