package org.gearticks.autonomous.generic.statemachine;

import org.gearticks.autonomous.generic.component.AutonomousComponent;

import java.util.Collection;

/**
 *
 */
public interface StateMachine extends AutonomousComponent{

    void addComponent(AutonomousComponent ac);

    void addComponents(Collection<AutonomousComponent> components);
}
