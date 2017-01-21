package org.gearticks.autonomous.velocity.components.generic;

import android.support.annotation.NonNull;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class GiroDriveEncoderStraighten extends LinearStateMachine {
	/**
	 *
	 * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param power - between 0 and 1, input for DriveDirection
	 * @param encoderTarget - target for the encoder. If the encoderPositive exceeds this target, the component transitions
	 * @param configuration
	 * @param id - descriptive name for logging
	 */
	public GiroDriveEncoderStraighten(double targetHeading, double power, int encoderTarget, @NonNull VelocityConfiguration configuration, String id) {
		super(id);
		this.addComponent(new GiroDriveEncoder(targetHeading, power, encoderTarget, configuration, "Driving"));
		this.addComponent(new Wait(0.05, "Waiting for stop"));
		this.addComponent(new GiroTurn(targetHeading, configuration, "Turning back"));
	}
}