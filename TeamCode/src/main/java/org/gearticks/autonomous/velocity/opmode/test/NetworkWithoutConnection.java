package org.gearticks.autonomous.velocity.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.ParallelComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@TeleOp
@Disabled
public class NetworkWithoutConnection extends VelocityBaseOpMode {
	public AutonomousComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
		final GiroDriveEncoder driveForward = new GiroDriveEncoder(0.0, 0.5, 1500, opModeContext, "Drive forward");
		final NetworkedStateMachine sm = new NetworkedStateMachine("NSM without connection");
		sm.setInitialComponent(driveForward);
		final ParallelComponent parallelComponent = new ParallelComponent();
		parallelComponent.addComponent(sm);
		final LinearStateMachine lsm = new LinearStateMachine();
		lsm.addComponent(parallelComponent);
		final NetworkedStateMachine nsm = new NetworkedStateMachine();
		nsm.setInitialComponent(lsm);
		nsm.addConnection(lsm, DefaultTransition.DEFAULT, lsm);
		return nsm;
	}
	public boolean isV2() {
		return false;
	}
}
