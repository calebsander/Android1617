package org.gearticks.components.velocity.component.composite;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.components.generic.component.ParallelComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.velocity.component.servo.LoadBall;
import org.gearticks.components.velocity.component.motor.MoveShooterDown;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class ShooterDownAndLoadSnake extends ParallelComponent {
	public ShooterDownAndLoadSnake(OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(id);
		this.addComponent(new MoveShooterDown(opModeContext, "Move Shooter Down"));
		final LinearStateMachine handleSnakeLoad = new LinearStateMachine("Load snake");
		handleSnakeLoad.addComponent(new OpModeComponentHardware<VelocityConfiguration, DefaultTransition>(opModeContext, DefaultTransition.class, "Wait for shooter down") {
			@Override
			public DefaultTransition run() {
				final DefaultTransition superTransition = super.run();
				if (superTransition != null) return superTransition;

				if (this.configuration.isShooterPassedEncoder() || this.configuration.isShooterDown()) return DefaultTransition.DEFAULT;
				else return null;
			}
		});
		handleSnakeLoad.addComponent(new LoadBall(opModeContext, "Load ball"));
		this.addComponent(handleSnakeLoad);
	}
}