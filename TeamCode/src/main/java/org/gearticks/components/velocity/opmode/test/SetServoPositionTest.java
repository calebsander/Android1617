package org.gearticks.components.velocity.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.component.ParallelComponent;
import org.gearticks.components.generic.component.RepeatedComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.hardwareagnostic.component.SetServoPosition;
import org.gearticks.components.hardwareagnostic.component.Wait;
import org.gearticks.components.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;

@Autonomous
@Disabled
public class SetServoPositionTest extends VelocityBaseOpMode {
	protected OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
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
