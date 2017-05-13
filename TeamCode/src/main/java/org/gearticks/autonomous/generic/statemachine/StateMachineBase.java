package org.gearticks.autonomous.generic.statemachine;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import java.util.Collection;

/**
 * A bare state machine.
 * Handles mass initialization of component states.
 */
public abstract class StateMachineBase extends AutonomousComponentAbstractImpl<DefaultTransition> {
	protected AutonomousComponent<?> currentState;
	protected final Collection<AutonomousComponent<?>> components;

	public StateMachineBase(Collection<AutonomousComponent<?>> components) {
		super(DefaultTransition.class);
		this.currentState = null;
		this.components = components;
	}

	public StateMachineBase(Collection<AutonomousComponent<?>> components, String id) {
		super(DefaultTransition.class, id);
		this.currentState = null;
		this.components = components;
	}

	@SuppressWarnings("Convert2streamapi")
	@Override
	public void onMatchStart() {
		super.onMatchStart();
		for (final AutonomousComponent<?> component : this.components) {
			component.onMatchStart();
		}
	}

	@Override
	public void tearDown() {
		super.tearDown();
		this.currentState = null;
	}

	@Override
	public String getId() {
		final String initialSegment = super.getId() + " - in ";
		if (this.currentState == null) return initialSegment + "null";
		else return initialSegment + this.currentState.getId();
	}
}
