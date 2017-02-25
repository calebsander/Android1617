package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.hardware.drive.MotorWrapper;

public class IntakeAndDrive extends AutonomousComponentHardware<VelocityConfiguration> {
	private final double power;
	private final double encoderTarget;
	private final boolean rampUp;

	/**
	 * @param power - drive power
	 * @param encoderTarget - distance to drive
	 * @param rampUp - whether to slowly ramp up the intake power
	 * @param opModeContext - the OpModeContext this is running in
	 * @param id - descriptive name for logging
	 */
	public IntakeAndDrive(double power, double encoderTarget, boolean rampUp, OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(opModeContext, id);
		this.power = power;
		this.encoderTarget = encoderTarget;
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

		if (this.stageTimer.seconds() > 1.0) return NEXT_STATE;
		else return null;
	}

	@Override
	public void tearDown() {
		super.tearDown();
		this.configuration.intake.stop();
	}
}