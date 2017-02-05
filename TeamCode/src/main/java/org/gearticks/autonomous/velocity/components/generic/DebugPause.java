package org.gearticks.autonomous.velocity.components.generic;

import android.support.annotation.NonNull;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class DebugPause extends AutonomousComponentHardware<VelocityConfiguration> {
	private final GamepadWrapper[] gamepads;
	private final Telemetry telemetry;


	/**
	 * waits until A is released
	 * @param telemetry - pass in the telemetry to see data on phone during debug
	 * @param gamepads - press x on this gamepad to continue, input to DriveDirection.gyroCorrect
	 * @param configuration
	 * @param id - descriptive name for logging
	 */
	public DebugPause(@NonNull GamepadWrapper[] gamepads, Telemetry telemetry, @NonNull VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.gamepads = gamepads;
		this.telemetry = telemetry;
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

		this.telemetry.addData("heading", this.configuration.imu.getHeading());
		this.telemetry.addData("drive left", this.configuration.driveLeft.encoderValue());
		this.telemetry.addData("drive right", this.configuration.driveRight.encoderValue());
		if (this.gamepads[0].getA()) return NEXT_STATE;
		else return null;
	}

}
