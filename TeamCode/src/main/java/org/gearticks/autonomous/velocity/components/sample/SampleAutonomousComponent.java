package org.gearticks.autonomous.velocity.components.sample;

import android.support.annotation.NonNull;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public abstract class SampleAutonomousComponent extends AutonomousComponentHardware<VelocityConfiguration> {
    public SampleAutonomousComponent(@NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        //Custom code here
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        //Code to update control system

        if (true /*end condition met*/) return NEXT_STATE;
        else return NOT_DONE;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        //Custom code here
    }
}
