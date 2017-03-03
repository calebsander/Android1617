package org.gearticks.autonomous.velocity.components.sample;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class SampleAutonomousComponent extends AutonomousComponentHardware<VelocityConfiguration, DefaultTransition> {
    public SampleAutonomousComponent(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, DefaultTransition.class, id);
    }

    @Override
    public void setup() {
        super.setup();
        //Custom code here
    }

    @SuppressWarnings({"ConstantIfStatement", "ConstantConditions"})
    @Override
    public DefaultTransition run() {
        final DefaultTransition superTransition = super.run();
        if (superTransition != null) return superTransition;

        //Code to update control system

        if (true /*end condition met*/) return DefaultTransition.DEFAULT;
        else return null;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        //Custom code here (robot is already stopped by AutonomousComponentHardware)
    }
}