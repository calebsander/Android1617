package org.gearticks.components.velocity.component.motor;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.hardware.drive.MotorWrapper;

public class RunIntake extends OpModeComponentHardware<VelocityConfiguration, DefaultTransition> {
    private final double time;
    private final boolean rampUp;
    private final boolean reversed;

    /**
     * @param time - the time to run the intake for (in seconds)
     * @param rampUp - whether to slowly ramp up the intake power
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public RunIntake(double time, boolean rampUp, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        this(time, rampUp, false, opModeContext, id);
    }

    public RunIntake(double time, boolean rampUp, boolean reversed, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, DefaultTransition.class, id);
        this.time = time;
        this.rampUp = rampUp;
        this.reversed = reversed;
    }

    @Override
    public DefaultTransition run() {
        final DefaultTransition superTransition = super.run();
        if (superTransition != null) return superTransition;

        final double accelLimit;
        if (this.rampUp) accelLimit = 0.02;
        else accelLimit = MotorWrapper.NO_ACCEL_LIMIT;
        if (reversed) this.configuration.intake.accelLimit(MotorConstants.INTAKE_OUT, accelLimit);
        else this.configuration.intake.accelLimit(MotorConstants.INTAKE_IN, accelLimit);

        if (this.stageTimer.seconds() > this.time) return DefaultTransition.DEFAULT;
        else return null;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.configuration.intake.stop();
    }
}