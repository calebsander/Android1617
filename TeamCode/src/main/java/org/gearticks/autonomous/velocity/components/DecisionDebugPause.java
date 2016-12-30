package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;
import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.opmodes.utility.Utils;

public class DecisionDebugPause extends AutonomousComponentVelocityBase {
	private final DriveDirection direction = new DriveDirection();
	private final GamepadWrapper[] gamepads;
	private final Telemetry telemetry;
	/**
	 *
	 * @param telemetry - pass in the telemetry to see data on phone during debug
	 * @param gamepads - press x on this gamepad to continue, input to DriveDirection.gyroCorrect
	 * @param configuration
	 * @param id - descriptive name for logging
	 */
	public DecisionDebugPause(GamepadWrapper[] gamepads, @NonNull Telemetry telemetry, @NonNull VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.gamepads = gamepads;
		this.telemetry = Utils.assertNotNull(telemetry);
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
		Telemetry telemetry = Utils.assertNotNull(this.telemetry);
		this.telemetry.addData("heading:", this.getConfiguration().imu.getHeading());
		this.telemetry.addData("drive left:", this.getConfiguration().driveLeft.encoderValue());
		this.telemetry.addData("drive right:", this.getConfiguration().driveRight.encoderValue());
		if (this.gamepads[0].getX()) {
			transition = 1;
			Log.d(Utils.TAG, "Transition 1 at DecisionDebugPause: X pressed");
		}
		else if (this.gamepads[0].getY()){
			Log.d(Utils.TAG, "Transition 2 at DecisionDebugPause: Y pressed");
			transition = 2;
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
