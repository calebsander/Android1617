package org.gearticks.autonomous.generic.statemachine;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import java.util.Collection;

/**
 * A bare state machine.
 * Handles mass initialization of component states.
 */
public abstract class StateMachineBase extends AutonomousComponentAbstractImpl {
	protected AutonomousComponent currentState;
	protected final Collection<AutonomousComponent> components;

	public StateMachineBase(Collection<AutonomousComponent> components) {
		super();
		this.currentState = null;
		this.components = components;
	}

	public StateMachineBase(Collection<AutonomousComponent> components, String id) {
		super(id);
		this.currentState = null;
		this.components = components;
	}

	@Override
	public void initialize() {
		super.initialize();
		for (final AutonomousComponent component : this.components) {
			component.initialize();
		}
	}

	@Override
	public void tearDown() {
		super.tearDown();
		this.currentState = null;
	}
}
