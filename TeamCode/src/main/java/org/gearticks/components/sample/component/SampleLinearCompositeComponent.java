package org.gearticks.components.sample.component;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.hardwareagnostic.component.Wait;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Sample to create a composite autonomous component that will consist of a linear state-machine of 2 or more AutonomousComponents
 */
public class SampleLinearCompositeComponent extends LinearStateMachine {
    public SampleLinearCompositeComponent(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super();
        this.addComponent(new Wait(0.5, id + " - Wait for 0.5 sec"));
    }
}
