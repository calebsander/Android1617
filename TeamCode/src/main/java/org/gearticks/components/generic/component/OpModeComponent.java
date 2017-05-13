package org.gearticks.components.generic.component;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import java.util.Collection;

/**
 * Designed to mirror the {@link OpMode} interface,
 * with the addition of being able to report being done.
 * Component has output "ports," allowing the component to
 * emit data about how it ended.
 * Interface is as simple as possible to allow for easy conversion.
 * A simple action, a state machine, or a whole autonomous
 * can all be represented as an OpModeComponent.
 * See samples.
 */
public interface OpModeComponent<TRANSITION_TYPE extends Enum<?>> {
	/**
	 * To be called once at start of program, e.g. to initialize some sensors
	 */
	void onMatchStart();

	/**
	 * Called each time the component state starts, can be multiple times in same autonomous program.
	 * Should contain all the code for resetting the state of the component.
	 */
	void setup();

	/**
	 * To be called in each loop cycle.
	 * Returns the "transition" or id of the "outputPort" through which this component ends.
	 * This allows for choice between different transitions/routes to next component.
	 *
	 * @return null if not yet done; the transition value if done
	 */
	TRANSITION_TYPE run();

	/**
	 * Called once each time the component ends
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
	 * Use if there is only one output port.
	 */
	enum DefaultTransition {
		DEFAULT
	}
	/**
	 * Indicates no possible transitions.
	 * Use for components that never terminate.
	 */
	enum NoTransition {}
}
