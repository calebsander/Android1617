package org.gearticks.components.generic.statemachine;

import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.OpModeComponentAbstract;
import java.util.Collection;

/**
 * A bare state machine.
 * Handles mass initialization of component states.
 */
public abstract class StateMachineBase extends OpModeComponentAbstract<DefaultTransition> {
	protected OpModeComponent<?> currentState;
	protected final Collection<OpModeComponent<?>> components;

	public StateMachineBase(Collection<OpModeComponent<?>> components) {
		super(DefaultTransition.class);
		this.currentState = null;
		this.components = components;
	}

	public StateMachineBase(Collection<OpModeComponent<?>> components, String id) {
		super(DefaultTransition.class, id);
		this.currentState = null;
		this.components = components;
	}

	@Override
	public void onMatchStart() {
		super.onMatchStart();
		this.components.forEach(OpModeComponent::onMatchStart);
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
