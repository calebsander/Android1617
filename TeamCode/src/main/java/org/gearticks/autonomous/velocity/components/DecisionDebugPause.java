package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;

public class DecisionDebugPause extends AutonomousComponentVelocityBase {
	private final DriveDirection direction = new DriveDirection();
	private GamepadWrapper[] gamepads;
	private Telemetry telemetry;
	/**
	 *
	 * @param telemetry - pass in the telemetry to see data on phone during debug
	 * @param gamepads - press x on this gamepad to continue, input to DriveDirection.gyroCorrect
	 * @param configuration
	 * @param id - descriptive name for logging
	 */
	public DecisionDebugPause(GamepadWrapper[] gamepads, Telemetry telemetry, @NonNull VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.gamepads = gamepads;
		this.telemetry = telemetry;
	}

	@Override
	public void setup(int inputPort) {
		super.setup(inputPort);
		// make sure motor are stopped
		this.direction.stopDrive();
		this.getConfiguration().move(this.direction, 0.06);
	}

	@Override
	public int run() {
		int transition = 0;
		super.run();
		this.telemetry.addData("heading:", this.getConfiguration().imu.getHeading());
		this.telemetry.addData("drive left:", this.getConfiguration().driveLeft.encoderValue());
		this.telemetry.addData("drive right:", this.getConfiguration().driveRight.encoderValue());
		if (this.gamepads[0].getX()) {
			transition = 1;
		}

		return transition;
	}

	@Override
	public void tearDown() {
		super.tearDown();
		//stop motors
		this.direction.stopDrive();
		this.getConfiguration().move(this.direction, 0.06);
	}




}
