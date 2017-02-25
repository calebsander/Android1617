package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.autonomous.generic.component.ParallelComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.velocity.single.LoadBall;
import org.gearticks.autonomous.velocity.components.velocity.single.MoveShooterDown;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class ShooterDownAndLoadSnake extends ParallelComponent {
	public ShooterDownAndLoadSnake(OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(id);
		this.addComponent(new MoveShooterDown(opModeContext, id));
		final LinearStateMachine handleSnakeLoad = new LinearStateMachine("Load snake");
		handleSnakeLoad.addComponent(new AutonomousComponentHardware<VelocityConfiguration>(opModeContext, "Wait for shooter down") {
			@Override
			public Transition run() {
				final Transition superTransition = super.run();
				if (superTransition != null) return superTransition;

				if (this.configuration.isShooterPassedEncoder()) return NEXT_STATE;
				else return null;
			}
		});
		handleSnakeLoad.addComponent(new LoadBall(opModeContext, "Load ball"));
		this.addComponent(handleSnakeLoad);
	}
}