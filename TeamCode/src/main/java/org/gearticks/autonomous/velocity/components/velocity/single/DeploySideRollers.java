package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class DeploySideRollers extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public DeploySideRollers(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, id);
    }

    @Override
    public void setup() {
        super.setup();
        configuration.rollersDown();
    }

    @Override
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        if (this.stageTimer.seconds() > 0.5) return NEXT_STATE;
        else return null;
    }
}
