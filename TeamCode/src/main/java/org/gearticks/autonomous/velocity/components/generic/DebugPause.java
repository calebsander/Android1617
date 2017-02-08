package org.gearticks.autonomous.velocity.components.generic;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class DebugPause extends AutonomousComponentHardware<VelocityConfiguration> {
	private final OpModeContext opModeContext;
	private boolean buttonPressed = false;


	/**
	 * waits until A is released
	 * @param opModeContext - the OpModeContext this is running in
	 * @param id - descriptive name for logging
	 */
	public DebugPause(OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(opModeContext.configuration, id);
		this.opModeContext = opModeContext;
	}

	@Override
	public void setup() {
		super.setup();
		this.buttonPressed = false;
		this.configuration.stopMotion();
	}

	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		this.opModeContext.telemetry.addData("heading", this.configuration.imu.getHeading());
		this.opModeContext.telemetry.addData("drive left", this.configuration.driveLeft.encoderValue());
		this.opModeContext.telemetry.addData("drive right", this.configuration.driveRight.encoderValue());
		if (this.opModeContext.gamepads[0].getA()) {
			this.buttonPressed = true;
		}
		else if (this.buttonPressed) {
			return NEXT_STATE;
		}

		return NOT_DONE;
	}

}
