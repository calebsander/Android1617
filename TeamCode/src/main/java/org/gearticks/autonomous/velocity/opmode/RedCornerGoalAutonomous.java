package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Delay;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.IncrementOption;

@Autonomous
public class RedCornerGoalAutonomous extends InitializedAutonomous {
	private static final double START_HEADING = 360.0 - 22.0;

	protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
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