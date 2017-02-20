package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.WaitToCrossLine;
import org.gearticks.autonomous.velocity.components.velocity.single.BananaTurnNoGiro;
import org.gearticks.autonomous.velocity.components.velocity.single.ShootBall;
import org.gearticks.autonomous.velocity.components.velocity.single.VerifyVuforiaPosition;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class BlockingBlueNear extends NetworkedStateMachine {
	public BlockingBlueNear(OpModeContext<VelocityConfiguration> opModeContext) {
		final LinearStateMachine getToLine = new LinearStateMachine("Get to line");
		getToLine.addComponent(new ShootBall(opModeContext, "Shoot"));
		getToLine.addComponent(new GiroDriveEncoder(0.0, 0.7, 500, opModeContext, "Drive off wall"));
		getToLine.addComponent(new BananaTurnNoGiro(25.0, 0.7, 4000, opModeContext, "Banana turn into ball"));
		getToLine.addComponent(new GiroDriveEncoder(25.0, 0.7, 500, opModeContext, "Drive along line"));
		getToLine.addComponent(new BananaTurnNoGiro(-18.0, 0.5, 1700, opModeContext, "Turn to line"));
		getToLine.addComponent(new GiroTurn(145.0, 0.12, 10, opModeContext, "Turn for Vuforia"));
		getToLine.addComponent(new DebugPause(opModeContext));
		getToLine.addComponent(new VerifyVuforiaPosition("Legos", 150.0, -1100.0, 30.0, opModeContext));

		final LinearStateMachine driveToBeacons = new LinearStateMachine("Drive to beacons");
		driveToBeacons.addComponent(new WaitToCrossLine(opModeContext));
		driveToBeacons.addComponent(new BananaTurnNoGiro(185.0, -0.5, 5000, opModeContext, "Turn into beacon"));

		this.setInitialComponent(getToLine);
		this.addConnection(getToLine, VerifyVuforiaPosition.CORRECT, driveToBeacons);
		this.addExitConnection(getToLine, VerifyVuforiaPosition.INCORRECT);
		this.addExitConnection(driveToBeacons, NEXT_STATE);
	}
}