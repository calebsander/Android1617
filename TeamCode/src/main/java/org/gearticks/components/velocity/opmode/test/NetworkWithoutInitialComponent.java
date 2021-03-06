package org.gearticks.components.velocity.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.ParallelComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.generic.statemachine.NetworkedStateMachine;
import org.gearticks.components.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@TeleOp
@Disabled
public class NetworkWithoutInitialComponent extends VelocityBaseOpMode {
	public OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
		final NetworkedStateMachine sm = new NetworkedStateMachine("NSM without initial component");
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
