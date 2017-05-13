package org.gearticks.autonomous.generic.statemachine;

import java.util.Map;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import org.gearticks.joystickoptions.ValuesJoystickOption;

/**
 * Allows for running different branches based off the value of a {@link ValuesJoystickOption}.
 * Determines what branch to run at runtime.
 * @param <E>
 */
public class SelectedComponent<E extends Enum<?>> extends NetworkedStateMachine {
	/**
	 * @param option the option selector
	 * @param components a map of options to components; each component should always exit with {@link DefaultTransition#DEFAULT}; should cover all options
	 */
	public SelectedComponent(ValuesJoystickOption<E> option, Class<E> optionClass, Map<E, AutonomousComponent<?>> components) {
		this.initialize(option, components, optionClass);
	}
	/**
	 * @param option the option selector
	 * @param components a map of options to components; each component should always exit with {@link DefaultTransition#DEFAULT}; should cover all options
	 * @param id the id to display as
	 */
	public SelectedComponent(ValuesJoystickOption<E> option, Class<E> optionClass, String id, Map<E, AutonomousComponent<?>> components) {
		super(id);
		this.initialize(option, components, optionClass);
	}

	private void initialize(ValuesJoystickOption<E> option, Map<E, AutonomousComponent<?>> components, Class<E> optionClass) {
		final AutonomousComponent<E> switcher = new AutonomousComponentAbstractImpl<E>(optionClass, "Switcher") {
			@Override
			public E run() {
				return option.getRawSelectedOption();
			}
		};
		this.setInitialComponent(switcher);
		for (final Map.Entry<E, AutonomousComponent<?>> valueComponent : components.entrySet()) {
			final LinearStateMachine componentTransitionSuppressed = new LinearStateMachine("Suppress transition");
			componentTransitionSuppressed.addComponent(valueComponent.getValue());
			this.addConnection(switcher, valueComponent.getKey(), componentTransitionSuppressed);
			this.addExitConnection(componentTransitionSuppressed, DefaultTransition.DEFAULT);
		}
	}
}