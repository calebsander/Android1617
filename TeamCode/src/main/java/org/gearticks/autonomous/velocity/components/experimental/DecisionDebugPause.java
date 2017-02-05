package org.gearticks.autonomous.velocity.components.experimental;

import android.support.annotation.NonNull;
import android.util.Log;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.utility.Utils;

public class DecisionDebugPause extends AutonomousComponentHardware<VelocityConfiguration> {
	public static final Transition X_TRANSITION = new Transition("X"), Y_TRANSITION = new Transition("Y");
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
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

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
		else return null;
	}

	@Override
	public void tearDown() {
		super.tearDown();
		//stop motors
		this.configuration.stopMotion();
	}
}
