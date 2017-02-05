package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.single.CheckPicture;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.LeftPressBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide.PictureResult;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration;

public class FixBeacon extends NetworkedStateMachine {
	public FixBeacon(PictureResult pictureResult, VelocityConfiguration configuration, VuforiaConfiguration vuforiaConfiguration) {
		super("Fix beacon");
		final AutonomousComponent checkPicture = new CheckPicture(pictureResult, vuforiaConfiguration);
		final LinearStateMachine fixBeacon = new LinearStateMachine();
		fixBeacon.addComponent(new Wait(4.0, "Wait for beacon"));
		fixBeacon.addComponent(new LeftPressBeaconServo(configuration, "Press beacon"));
		fixBeacon.addComponent(new DisengageBeaconServo(configuration, "Stop pressing beacon"));

		this.setInitialComponent(checkPicture);
		this.addExitConnection(checkPicture, CheckPicture.CORRECT);
		this.addConnection(checkPicture, CheckPicture.WRONG, fixBeacon);
		this.addExitConnection(fixBeacon, NEXT_STATE);
	}
}
