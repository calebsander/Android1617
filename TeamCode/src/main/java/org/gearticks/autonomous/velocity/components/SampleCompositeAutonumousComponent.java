package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Sample to create a composite autonomous component that will consist of a linear state-machine of 2 or more AutonomousComponents
 */
public class SampleCompositeAutonumousComponent extends LinearStateMachine {
    public SampleCompositeAutonumousComponent(@NonNull VelocityConfiguration configuration, String id) {
        super();
        this.addComponent(new Wait(0.5, id + " - Wait for 0.5 sec"));
    }
}
