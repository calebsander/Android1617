package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.WaitToCrossLine;
import org.gearticks.autonomous.velocity.components.velocity.single.BananaTurnNoGiro;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class BlockOpponents extends LinearStateMachine {
	private static final double BANANA_TURN_END_ANGLE = 25.0;
	private static final double BEACON_TARGET_ANGLE = -37.0; //for blue side

	public BlockOpponents(OpModeContext<VelocityConfiguration> opModeContext) {
		this.addComponent(new BananaTurnNoGiro(BANANA_TURN_END_ANGLE, 0.7, 4000, opModeContext, "Drive into center"));
		this.addComponent(new GiroDriveEncoder(BANANA_TURN_END_ANGLE, 0.7, 5000, opModeContext, "Drive past ball"));
		this.addComponent(new GiroTurn(BEACON_TARGET_ANGLE, 0.1, 10, opModeContext, "Turn to beacons"));
		this.addComponent(new WaitToCrossLine(opModeContext));
		this.addComponent(new GiroDriveEncoder(BEACON_TARGET_ANGLE, 1.0, 5000, opModeContext, "Drive to beacons"));
	}
}