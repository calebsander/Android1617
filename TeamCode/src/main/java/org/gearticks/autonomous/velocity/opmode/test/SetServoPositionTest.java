package org.gearticks.autonomous.velocity.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.ParallelComponent;
import org.gearticks.autonomous.generic.component.RepeatedComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.SetServoPosition;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;

@Autonomous
@Disabled
public class SetServoPositionTest extends VelocityBaseOpMode {
	protected AutonomousComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
		final VelocityConfiguration configuration = opModeContext.configuration;

		final LinearStateMachine frontBeaconPresserSM = new LinearStateMachine();
		frontBeaconPresserSM.addComponent(new SetServoPosition(configuration.frontBeaconPresser, MotorConstants.PRESSER_V2_FRONT_IN, "Presser in"));
		frontBeaconPresserSM.addComponent(new Wait(1.0, "Presser in wait"));
		frontBeaconPresserSM.addComponent(new SetServoPosition(configuration.frontBeaconPresser, MotorConstants.PRESSER_V2_FRONT_OUT, "Presser out"));
		frontBeaconPresserSM.addComponent(new Wait(1.0, "Presser out wait"));

		final LinearStateMachine snakeSM = new LinearStateMachine();
		snakeSM.addComponent(new SetServoPosition(configuration.snake, MotorConstants.SNAKE_V2_HOLDING, "Snake holding"));
		snakeSM.addComponent(new Wait(0.5, "Snake holding wait"));
		snakeSM.addComponent(new SetServoPosition(configuration.snake, MotorConstants.SNAKE_V2_DUMPING, "Snake dumping"));
		snakeSM.addComponent(new Wait(0.5, "Snake dumping wait"));

		final ParallelComponent component = new ParallelComponent();
		component.addComponent(new RepeatedComponent(frontBeaconPresserSM));
		component.addComponent(new RepeatedComponent(snakeSM));
		return component;
	}
	protected boolean isV2() {
		return true;
	}
}
