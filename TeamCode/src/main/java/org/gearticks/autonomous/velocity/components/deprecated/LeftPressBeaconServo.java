package org.gearticks.autonomous.velocity.components.deprecated;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Deprecated
public class LeftPressBeaconServo extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public LeftPressBeaconServo(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, id);
    }

    @Override
    public void setup() {
        super.setup();
        //configuration.beaconPresserEngageLeft();
    }

    @Override
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        if (this.stageTimer.seconds() > VelocityConfiguration.MotorConstants.PRESSER_V2_TIME_TO_MOVE) return NEXT_STATE;
        else return null;
    }
}
