package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.ParallelComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.WaitToCrossLine;
import org.gearticks.autonomous.velocity.components.velocity.single.BananaTurnNoGiro;
import org.gearticks.autonomous.velocity.components.velocity.single.ShootBall;
import org.gearticks.hardware.configurations.VelocityConfiguration;

import java.util.ArrayList;
import java.util.Collection;

public class BlockOpponents extends LinearStateMachine {
	private static final double BANANA_TURN_END_ANGLE = 25.0;
	private static final double BEACON_TARGET_ANGLE = -45.0; //for blue side
	private static final double LINE_ANGLE = 135.0;

	public BlockOpponents(OpModeContext<VelocityConfiguration> opModeContext) {
		this.addComponent(new ShootBall(opModeContext, "Shoot"));
		this.addComponent(new BananaTurnNoGiro(LINE_ANGLE/2.0, 0.7, 4000, opModeContext, "Drive into center"));
		this.addComponent(new BananaTurnNoGiro(LINE_ANGLE, -0.7, 4000, opModeContext, "Drive past ball"));
		this.addComponent(new WaitToCrossLine(opModeContext));
		this.addComponent(new DebugPause(opModeContext));
		this.addComponent(new GiroDriveEncoder(BEACON_TARGET_ANGLE, 0.5, 2000, opModeContext, "Drive to beacons"));
	}
}