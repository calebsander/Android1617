package org.gearticks.components.hardwareagnostic.component;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.hardware.configurations.OrientableConfiguration;

public class GiroDriveEncoder extends LinearStateMachine {
	/**
	 *
	 * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param power - between 0 and 1, input for DriveDirection
	 * @param encoderTarget - target for the encoder. If the encoderPositive exceeds this target, the component transitions
	 * @param opModeContext - the OpModeContext this is running in
	 * @param id - descriptive name for logging
	 */
	public GiroDriveEncoder(double targetHeading, double power, int encoderTarget, OpModeContext<? extends OrientableConfiguration> opModeContext, String id) {
		super(id);
		this.addComponent(new GiroDriveEncoderNoStop(targetHeading, power, encoderTarget, opModeContext, "Driving"));
		this.addComponent(new OpModeComponentHardware<OrientableConfiguration, DefaultTransition>(opModeContext, DefaultTransition.class, "Stopping") {
			@Override
			public DefaultTransition run() {
				final DefaultTransition superTransition = super.run();
				if (superTransition != null) return superTransition;

				this.configuration.stopMotion();
				return DefaultTransition.DEFAULT;
			}
		});
	}
}
