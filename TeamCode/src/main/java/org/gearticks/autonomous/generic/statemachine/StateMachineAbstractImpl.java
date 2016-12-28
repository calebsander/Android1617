package org.gearticks.autonomous.generic.statemachine;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implements
 */
public abstract class StateMachineAbstractImpl extends AutonomousComponentAbstractImpl implements StateMachine {

	protected AutonomousComponent currentState;
	protected final List<AutonomousComponent> components;

	public StateMachineAbstractImpl() {
		super();
		this.currentState = null;
		this.components = new ArrayList<>();
	}

	public StateMachineAbstractImpl(String id) {
		super(id);
		this.currentState = null;
		this.components = new ArrayList<>();
	}

	public void addComponent(AutonomousComponent component) {
		this.components.add(component);
	}
	public void addComponents(Collection<AutonomousComponent> components) {
		this.components.addAll(components);
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
