package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;
import android.util.Log;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.utility.Utils;

public class DecisionDebugPause extends AutonomousComponentHardware<VelocityConfiguration> {
	public static final int X_TRANSITION = newTransition(), Y_TRANSITION = newTransition();
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
	public void setup() {
		super.setup();
		// make sure motor are stopped
		this.configuration.stopMotion();
	}

	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		this.telemetry.addData("heading:", this.configuration.imu.getHeading());
		this.telemetry.addData("drive left:", this.configuration.driveLeft.encoderValue());
		this.telemetry.addData("drive right:", this.configuration.driveRight.encoderValue());
		if (this.gamepads[0].getX()) {
			Log.d(Utils.TAG, "Transition 1 at DecisionDebugPause: X pressed");
			return X_TRANSITION;
		}
		else if (this.gamepads[0].getY()){
			Log.d(Utils.TAG, "Transition 2 at DecisionDebugPause: Y pressed");
			return Y_TRANSITION;
		}
		else return NOT_DONE;
	}

	@Override
	public void tearDown() {
		super.tearDown();
		//stop motors
		this.configuration.stopMotion();
	}
}
