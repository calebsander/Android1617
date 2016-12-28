package org.gearticks.autonomous.velocity.components;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import java.util.ArrayList;
import java.util.List;

/**
 * Sample to create a composite autonomous component that will consist of a linear state-machine of 2 or more AutonomousComponents
 */
public class SampleCompositeAutonumousComponent extends LinearStateMachine {
    public SampleCompositeAutonumousComponent(VelocityConfiguration configuration, String id) {
        super(getComponents(configuration, id));
    }

    private static List<AutonomousComponent> getComponents(VelocityConfiguration configuration, String id) {
        List<AutonomousComponent> components = new ArrayList<>();
        components.add(new Wait(0.5, id + " - Wait for 0.5 sec"));
        return components;
    }
}
