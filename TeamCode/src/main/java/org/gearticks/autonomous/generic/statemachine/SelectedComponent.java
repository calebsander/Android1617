package org.gearticks.autonomous.generic.statemachine;

import java.util.HashMap;
import java.util.Map;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import org.gearticks.joystickoptions.ValuesJoystickOption;

/**
 * Allows for running different branches based off the value of a {@link ValuesJoystickOption}.
 * Determines what branch to run at runtime.
 * @param <E>
 */
public class SelectedComponent<E> extends NetworkedStateMachine {
	/**
	 * @param option the option selector
	 * @param components a map of options to components; each component should always exit with {@link AutonomousComponentAbstractImpl#NEXT_STATE}; should cover all options
	 */
	public SelectedComponent(ValuesJoystickOption<E> option, Map<E, AutonomousComponent> components) {
		this.initialize(option, components);
	}
	/**
	 * @param option the option selector
	 * @param components a map of options to components; each component should always exit with {@link AutonomousComponentAbstractImpl#NEXT_STATE}; should cover all options
	 * @param id the id to display as
	 */
	public SelectedComponent(ValuesJoystickOption<E> option, Map<E, AutonomousComponent> components, String id) {
		super(id);
		this.initialize(option, components);
	}

	private void initialize(ValuesJoystickOption<E> option, Map<E, AutonomousComponent> components) {
		final Map<E, Transition> transitions = new HashMap<>();
		for (final E optionValue : components.keySet()) {
			transitions.put(optionValue, new Transition(String.valueOf(optionValue)));
		}

		final AutonomousComponent switcher = new AutonomousComponentAbstractImpl("Switcher") {
			@Override
			public Transition run() {
				final E selectedValue = option.getRawSelectedOption();
				final Transition transition = transitions.get(selectedValue);
				if (transition == null) throw new RuntimeException("No component supplied for value: " + selectedValue);
				return transition;
			}
		};
		this.setInitialComponent(switcher);
		for (Map.Entry<E, AutonomousComponent> valueComponent : components.entrySet()) {
			final Transition transition = transitions.get(valueComponent.getKey());
			final AutonomousComponent component = valueComponent.getValue();
			this.addConnection(switcher, transition, component);
			this.addExitConnection(component, NEXT_STATE);
		}
	}
}