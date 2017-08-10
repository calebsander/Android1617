package org.gearticks.components.generic.statemachine;

import java.util.Map;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.component.OpModeComponentAbstract;
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
	public SelectedComponent(ValuesJoystickOption<E> option, Class<E> optionClass, Map<E, OpModeComponent<?>> components) {
		this.initialize(option, components, optionClass);
	}
	/**
	 * @param option the option selector
	 * @param components a map of options to components; each component should always exit with {@link DefaultTransition#DEFAULT}; should cover all options
	 * @param id the id to display as
	 */
	public SelectedComponent(ValuesJoystickOption<E> option, Class<E> optionClass, String id, Map<E, OpModeComponent<?>> components) {
		super(id);
		this.initialize(option, components, optionClass);
	}

	private void initialize(ValuesJoystickOption<E> option, Map<E, OpModeComponent<?>> components, Class<E> optionClass) {
		final OpModeComponent<E> switcher = new OpModeComponentAbstract<E>(optionClass, "Switcher") {
			@Override
			public E run() {
				return option.getRawSelectedOption();
			}
		};
		this.setInitialComponent(switcher);
		components.entrySet().forEach(valueComponent -> {
			final LinearStateMachine componentTransitionSuppressed = new LinearStateMachine("Suppress transition");
			componentTransitionSuppressed.addComponent(valueComponent.getValue());
			this.addConnection(switcher, valueComponent.getKey(), componentTransitionSuppressed);
			this.addExitConnection(componentTransitionSuppressed, DefaultTransition.DEFAULT);
		});
	}
}