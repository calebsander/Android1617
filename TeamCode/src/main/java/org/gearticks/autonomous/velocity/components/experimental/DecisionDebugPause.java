package org.gearticks.autonomous.velocity.components.experimental;

import android.util.Log;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.autonomous.velocity.components.experimental.DecisionDebugPause.XYTransition;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.Utils;

public class DecisionDebugPause extends AutonomousComponentHardware<VelocityConfiguration, XYTransition> {
	public enum XYTransition {
		X, Y
	}
	private final OpModeContext<?> opModeContext;

	/**
	 *
	 * @param opModeContext - the OpModeContext this is running in
	 * @param id - descriptive name for logging
	 */
	public DecisionDebugPause(OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(opModeContext, XYTransition.class, id);
		this.opModeContext = opModeContext;
	}

	@Override
	public void setup() {
		super.setup();
		// make sure motor are stopped
		this.configuration.stopMotion();
	}

	@Override
	public XYTransition run() {
		final XYTransition superTransition = super.run();
		if (superTransition != null) return superTransition;

		this.opModeContext.telemetry.addData("heading:", this.configuration.imu.getHeading());
		this.opModeContext.telemetry.addData("drive left:", this.configuration.driveLeft.encoderValue());
		this.opModeContext.telemetry.addData("drive right:", this.configuration.driveRight.encoderValue());
		if (this.opModeContext.gamepads[0].getX()) {
			Log.d(Utils.TAG, "Transition 1 at DecisionDebugPause: X pressed");
			return XYTransition.X;
		}
		else if (this.opModeContext.gamepads[0].getY()){
			Log.d(Utils.TAG, "Transition 2 at DecisionDebugPause: Y pressed");
			return XYTransition.Y;
		}
		else return null;
	}
}
