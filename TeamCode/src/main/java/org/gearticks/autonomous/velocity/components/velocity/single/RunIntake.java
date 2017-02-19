package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.hardware.drive.MotorWrapper;

public class RunIntake extends AutonomousComponentHardware<VelocityConfiguration> {
    private final double time;
    private final boolean rampUp;

    /**
     * @param time - the time to run the intake for (in seconds)
     * @param rampUp - whether to slowly ramp up the intake power
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public RunIntake(double time, boolean rampUp, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, id);
        this.time = time;
        this.rampUp = rampUp;
    }

    @Override
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        final double accelLimit;
        if (this.rampUp) accelLimit = 0.05;
        else accelLimit = MotorWrapper.NO_ACCEL_LIMIT;
        this.configuration.intake.accelLimit(MotorConstants.INTAKE_IN, accelLimit);

        if (this.stageTimer.seconds() > this.time) return NEXT_STATE;
        else return null;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.configuration.intake.stop();
    }
}