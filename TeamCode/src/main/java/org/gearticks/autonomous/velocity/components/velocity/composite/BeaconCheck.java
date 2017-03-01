package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.single.CheckPicture;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.FrontPressBeacon;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide.PictureResult;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class BeaconCheck extends NetworkedStateMachine {
	public static final Transition
			CORRECT = new Transition("Correct"),
			WRONG = new Transition("Wrong"),
			LEFT_TRANSITION = new Transition("Left"),
			RIGHT_TRANSITION = new Transition("Right"),
			UNKNOWN_TRANSITION = new Transition("Unknown");

	public BeaconCheck(boolean isBlue, PictureResult pictureResult, OpModeContext<VelocityConfiguration> opModeContext) {
		super("Beacon Check");
		final AutonomousComponent checkPicture = new CheckPicture(isBlue, opModeContext, pictureResult);

		this.setInitialComponent(checkPicture);
		this.addExitConnection(checkPicture, CheckPicture.CORRECT);
		this.addExitConnection(checkPicture, CheckPicture.WRONG);
		this.addExitConnection(checkPicture, CheckPicture.LEFT_TRANSITION);
		this.addExitConnection(checkPicture, CheckPicture.RIGHT_TRANSITION);
		this.addExitConnection(checkPicture, CheckPicture.UNKNOWN_TRANSITION);
	}
}
