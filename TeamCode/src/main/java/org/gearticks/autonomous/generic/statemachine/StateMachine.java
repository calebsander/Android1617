package org.gearticks.autonomous.generic.statemachine;

import org.gearticks.autonomous.generic.component.AutonomousComponent;

import java.util.Collection;

/**
 *
 */
public interface StateMachine extends AutonomousComponent{

    public void addComponent(AutonomousComponent ac);

    public void addComponents(Collection<AutonomousComponent> components);
}
