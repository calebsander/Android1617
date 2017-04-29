package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Delay;
import org.gearticks.autonomous.velocity.components.velocity.composite.BlockingBlueNear;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.IncrementOption;

@Disabled
@Autonomous
public class BlockingAutonomous extends VelocityBaseOpMode {

	@Override
	protected void loopBeforeStart() {
		super.loopBeforeStart();
		this.configuration.safeShooterStopper(VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_UP);
		this.configuration.advanceShooterToDownAutonomous();
	}

	protected AutonomousComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
		final IncrementOption delayOption = new IncrementOption("Delay", 20.0);
		this.addOption(delayOption);

		final LinearStateMachine sm = new LinearStateMachine();
		sm.addComponent(new Delay(opModeContext, delayOption));
		sm.addComponent(new BlockingBlueNear(opModeContext));
		return sm;
	}
	protected boolean isV2() {
		return true;
	}
}