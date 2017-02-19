package org.gearticks.autonomous.velocity.components.generic;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class DebugPause extends AutonomousComponentHardware<VelocityConfiguration> {
	private final OpModeContext opModeContext;

	/**
	 * waits until A is released
	 * @param opModeContext - the OpModeContext this is running in
	 *
	 */
	public DebugPause(OpModeContext<VelocityConfiguration> opModeContext) {
		super(opModeContext);
		this.opModeContext = opModeContext;
	}

	@Override
	public void setup() {
		super.setup();
		this.configuration.stopMotion();
	}

	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		this.opModeContext.telemetry.addData("heading", this.configuration.imu.getHeading());
		this.opModeContext.telemetry.addData("drive left", this.configuration.driveLeft.encoderValue());
		this.opModeContext.telemetry.addData("drive right", this.configuration.driveRight.encoderValue());
		if (this.opModeContext.gamepads[0].getA()) return NEXT_STATE;
		else return null;
	}

}
