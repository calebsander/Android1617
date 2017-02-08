package org.gearticks.autonomous.velocity.components.sample;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class SampleAutonomousComponent extends AutonomousComponentHardware<VelocityConfiguration> {
    public SampleAutonomousComponent(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext.configuration, id);
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