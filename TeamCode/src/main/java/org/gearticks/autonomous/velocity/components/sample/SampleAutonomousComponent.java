package org.gearticks.autonomous.velocity.components.sample;

import android.support.annotation.NonNull;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class SampleAutonomousComponent extends AutonomousComponentHardware<VelocityConfiguration> {
    public SampleAutonomousComponent(@NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        //Custom code here
    }

    @Override
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        //Code to update control system

        if (true /*end condition met*/) return NEXT_STATE;
        else return null;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        //Custom code here
    }
}