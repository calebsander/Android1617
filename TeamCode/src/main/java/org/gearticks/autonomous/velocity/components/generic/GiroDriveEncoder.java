package org.gearticks.autonomous.velocity.components.generic;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class GiroDriveEncoder extends LinearStateMachine {
	/**
	 *
	 * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param power - between 0 and 1, input for DriveDirection
	 * @param encoderTarget - target for the encoder. If the encoderPositive exceeds this target, the component transitions
	 * @param opModeContext - the OpModeContext this is running in
	 * @param id - descriptive name for logging
	 */
	public GiroDriveEncoder(double targetHeading, double power, int encoderTarget, OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(id);
		this.addComponent(new GiroDriveEncoderNoStop(targetHeading, power, encoderTarget, opModeContext, "Driving"));
		this.addComponent(new AutonomousComponentHardware<VelocityConfiguration>(opModeContext, "Stopping") {
			@Override
			public Transition run() {
				final Transition superTransition = super.run();
				if (superTransition != null) return superTransition;

				this.configuration.stopMotion();
				return NEXT_STATE;
			}
		});
	}
}
