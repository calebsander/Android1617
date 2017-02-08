package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class RaiseSideRollers extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public RaiseSideRollers(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext.configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        configuration.rollersUp();
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        if (this.stageTimer.seconds() > 0.5) return NEXT_STATE;
        else return NOT_DONE;
    }
}
