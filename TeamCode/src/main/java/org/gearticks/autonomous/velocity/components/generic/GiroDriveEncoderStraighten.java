package org.gearticks.autonomous.velocity.components.generic;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.hardware.configurations.OrientableConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class GiroDriveEncoderStraighten extends LinearStateMachine {
	/**
	 *
	 * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param power - between 0 and 1, input for DriveDirection
	 * @param encoderTarget - target for the encoder. If the encoderPositive exceeds this target, the component transitions
	 * @param opModeContext - the OpModeContext this is running in
	 * @param id - descriptive name for logging
	 */
	public GiroDriveEncoderStraighten(double targetHeading, double power, int encoderTarget, OpModeContext<? extends OrientableConfiguration> opModeContext, String id) {
		super(id);
		this.addComponent(new GiroDriveEncoder(targetHeading, power, encoderTarget, opModeContext, "Driving"));
		this.addComponent(new Wait(0.05, "Waiting for stop"));
		this.addComponent(new GiroTurn(targetHeading, opModeContext, "Turning back"));
	}
}