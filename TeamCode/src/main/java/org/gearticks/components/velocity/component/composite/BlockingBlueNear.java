package org.gearticks.components.velocity.component.composite;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.hardwareagnostic.component.GiroDriveEncoder;
import org.gearticks.components.hardwareagnostic.component.GiroTurn;
import org.gearticks.components.hardwareagnostic.component.WaitToCrossLine;
import org.gearticks.components.velocity.component.experimental.BananaTurnNoGiro;
import org.gearticks.components.velocity.component.motor.ShootBall;
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