package org.gearticks.autonomous.velocity.components.deprecated;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Deprecated
public class LeftPressBeaconServo extends AutonomousComponentHardware<VelocityConfiguration, DefaultTransition> {
    /**
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public LeftPressBeaconServo(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, DefaultTransition.class, id);
    }

    @Override
    public void setup() {
        super.setup();
        //configuration.beaconPresserEngageLeft();
    }

    @Override
    public DefaultTransition run() {
        final DefaultTransition superTransition = super.run();
        if (superTransition != null) return superTransition;

        if (this.stageTimer.seconds() > VelocityConfiguration.MotorConstants.PRESSER_V2_TIME_TO_MOVE) return DefaultTransition.DEFAULT;
        else return null;
    }
}
