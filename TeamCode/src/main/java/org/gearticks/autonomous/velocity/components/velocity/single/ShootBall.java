package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class ShootBall extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public ShootBall(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, id);
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.shootFast();
    }

    @Override
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        if (this.configuration.hasShot() || this.stageTimer.seconds() > 1.0) return NEXT_STATE;
        else return null;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.configuration.shooter.stop();
    }
}
