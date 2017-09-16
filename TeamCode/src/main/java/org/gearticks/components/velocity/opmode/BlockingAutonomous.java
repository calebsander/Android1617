package org.gearticks.components.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.hardwareagnostic.component.Delay;
import org.gearticks.components.velocity.component.composite.BlockingBlueNear;
import org.gearticks.components.velocity.opmode.generic.VelocityBaseOpMode;
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

	protected OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
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