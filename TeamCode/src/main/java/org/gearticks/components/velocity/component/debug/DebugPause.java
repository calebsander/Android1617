package org.gearticks.components.velocity.component.debug;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class DebugPause extends OpModeComponentHardware<VelocityConfiguration, DefaultTransition> {
	private final OpModeContext<?> opModeContext;

	/**
	 * waits until A is released
	 * @param opModeContext - the OpModeContext this is running in
	 *
	 */
	public DebugPause(OpModeContext<VelocityConfiguration> opModeContext) {
		super(opModeContext, DefaultTransition.class);
		this.opModeContext = opModeContext;
	}

	@Override
	public void setup() {
		super.setup();
		this.configuration.stopMotion();
	}

	@Override
	public DefaultTransition run() {
		final DefaultTransition superTransition = super.run();
		if (superTransition != null) return superTransition;

		this.opModeContext.telemetry.addData("absolute heading", this.configuration.imu.getHeading());
		this.opModeContext.telemetry.addData("relative heading", this.configuration.imu.getRelativeYaw());
		this.opModeContext.telemetry.addData("drive left", this.configuration.driveLeft.encoderValue());
		this.opModeContext.telemetry.addData("drive right", this.configuration.driveRight.encoderValue());
		if (this.opModeContext.gamepads[0].getA()) return DefaultTransition.DEFAULT;
		else return null;
	}

}
