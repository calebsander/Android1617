package org.gearticks.autonomous.velocity.components;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class DebugPause extends AutonomousComponentHardware<VelocityConfiguration> {
	private final GamepadWrapper gamepad;
	private final Telemetry telemetry;

	/**
	 *
	 * @param telemetry - pass in the telemetry to see data on phone during debug
	 * @param gamepads - press x on this gamepad to continue, input to DriveDirection.gyroCorrect
	 * @param configuration
	 * @param id - descriptive name for logging
	 */
	public DebugPause(GamepadWrapper[] gamepads, Telemetry telemetry, VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.gamepad = gamepads[0];
		this.telemetry = telemetry;
	}

	@Override
	public void setup() {
		super.setup();
		this.configuration.stopMotion();
	}

	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		this.telemetry.addData("heading", this.configuration.imu.getHeading());
		this.telemetry.addData("drive left", this.configuration.driveLeft.encoderValue());
		this.telemetry.addData("drive right", this.configuration.driveRight.encoderValue());
		if (this.gamepad.getA()) return NEXT_STATE;
		else return NOT_DONE;
	}
}
