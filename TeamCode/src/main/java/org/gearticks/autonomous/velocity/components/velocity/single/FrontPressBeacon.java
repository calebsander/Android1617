package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class FrontPressBeacon extends AutonomousComponentHardware<VelocityConfiguration, DefaultTransition> {
    /**
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public FrontPressBeacon(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, DefaultTransition.class, id);
    }

    @Override
    public void setup() {
        super.setup();
        configuration.beaconPresserFrontOut();
    }

    @Override
    public DefaultTransition run() {
        final DefaultTransition superTransition = super.run();
        if (superTransition != null) return superTransition;

        if (this.stageTimer.seconds() > (VelocityConfiguration.MotorConstants.PRESSER_V2_TIME_TO_MOVE)) {
            return DefaultTransition.DEFAULT;
        }
        else if (this.stageTimer.seconds() > (VelocityConfiguration.MotorConstants.PRESSER_V2_TIME_TO_MOVE*0.6)) {
            configuration.beaconPresserFrontOut();
        }
        else if (this.stageTimer.seconds() > (VelocityConfiguration.MotorConstants.PRESSER_V2_TIME_TO_MOVE*0.4)) {
            configuration.beaconPresserFrontOutPartial();
        }
        /*else if (this.stageTimer.seconds() > (VelocityConfiguration.MotorConstants.PRESSER_V2_TIME_TO_MOVE*0.5)) {
            configuration.beaconPresserFrontOut();
        }
        else if (this.stageTimer.seconds() > (VelocityConfiguration.MotorConstants.PRESSER_V2_TIME_TO_MOVE*0.4)) {
            configuration.beaconPresserFrontOutPartial();
        }*/
        else {
            return null;
        }
        return null;
    }
}
