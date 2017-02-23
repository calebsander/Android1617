package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.single.CheckPicture;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.deprecated.LeftPressBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.FrontPressBeacon;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide.PictureResult;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class BeaconCheck extends NetworkedStateMachine {
	public BeaconCheck(PictureResult pictureResult, OpModeContext<VelocityConfiguration> opModeContext) {
		super("Fix beacon");
		final AutonomousComponent checkPicture = new CheckPicture(pictureResult, opModeContext);
		final LinearStateMachine fixBeacon = new LinearStateMachine();
		fixBeacon.addComponent(new Wait(4.0, "Wait for beacon"));
		fixBeacon.addComponent(new FrontPressBeacon(opModeContext, "Press beacon"));
		fixBeacon.addComponent(new DisengageBeaconServo(opModeContext, "Stop pressing beacon"));

		this.setInitialComponent(checkPicture);
		this.addExitConnection(checkPicture, CheckPicture.CORRECT);
		this.addConnection(checkPicture, CheckPicture.WRONG, fixBeacon);
		this.addExitConnection(fixBeacon, NEXT_STATE);
	}
}
