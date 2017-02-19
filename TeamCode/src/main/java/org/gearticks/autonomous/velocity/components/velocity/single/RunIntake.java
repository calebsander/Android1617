package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class RunIntake extends AutonomousComponentHardware<VelocityConfiguration> {
    public final double time;

    /**
     *
     * @param time
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public RunIntake(double time, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, id);
        this.time = time;
    }

    @Override
    public void setup() {
        super.setup();
        //Custom code here
    }

    @SuppressWarnings({"ConstantIfStatement", "ConstantConditions"})
    @Override
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        this.configuration.intake.setPower(VelocityConfiguration.MotorConstants.INTAKE_IN);

        if (this.stageTimer.seconds() > this.time) return NEXT_STATE;
        else return null;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        //Custom code here
    }
}