package org.gearticks.components.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.hardwareagnostic.component.Delay;
import org.gearticks.components.hardwareagnostic.component.GiroDriveEncoder;
import org.gearticks.components.hardwareagnostic.component.GiroTurn;
import org.gearticks.components.velocity.component.composite.Shoot2Balls;
import org.gearticks.components.velocity.opmode.generic.InitializedAutonomous;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.IncrementOption;

@Autonomous
public class RedCornerGoalAutonomous extends InitializedAutonomous {
	private static final double START_HEADING = 360.0 - 22.0;

	protected OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
		final LinearStateMachine sm = new LinearStateMachine();

		sm.addComponent(new Shoot2Balls(true, opModeContext, "Shoot balls"));

		final IncrementOption delayOption = new IncrementOption("Delay", 20.0);
		this.addOption(delayOption);
		sm.addComponent(new Delay(opModeContext, delayOption));

		sm.addComponent(new GiroDriveEncoder(START_HEADING, 0.5, 800, opModeContext, "Drive out"));
		sm.addComponent(new GiroTurn(-90.0, 0.1, 10, opModeContext, "Turn to ramp"));
		sm.addComponent(new GiroDriveEncoder(-90.0, 0.7, 6000, opModeContext, "Drive to ramp"));

		return sm;
	}

	protected double targetHeading() {
		return START_HEADING;
	}
}