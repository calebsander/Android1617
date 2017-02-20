package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.WaitToCrossLine;
import org.gearticks.autonomous.velocity.components.velocity.single.BananaTurnNoGiro;
import org.gearticks.autonomous.velocity.components.velocity.single.ShootBall;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class BlockingBlueNear extends LinearStateMachine {
	public BlockingBlueNear(OpModeContext<VelocityConfiguration> opModeContext) {
		final double BANANA_TURN_ANGLE = 20.0;
		this.addComponent(new ShootBall(opModeContext, "Shoot"));
		this.addComponent(new GiroDriveEncoder(0.0, 0.4, 3700, opModeContext, "Drive to cap ball"));
		this.addComponent(new GiroTurn(BANANA_TURN_ANGLE, opModeContext, "Turn to " + BANANA_TURN_ANGLE));
		this.addComponent(new WaitToCrossLine(opModeContext));
		this.addComponent(new GiroDriveEncoder(BANANA_TURN_ANGLE, 0.2, 500, opModeContext, "Hit cap ball"));
		this.addComponent(new BananaTurnNoGiro(true, 0.0, 0.2, 1000, opModeContext, "Banana turn straight"));
		this.addComponent(new GiroDriveEncoder(0.0, 0.7, 7000, opModeContext, "Block opponent"));
		this.addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
	}
}