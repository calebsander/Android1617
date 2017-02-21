package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.AutonomousSideSelector;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideCornerAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.RedSideCornerAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.RunIntake;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

import static org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl.NEXT_STATE;

@Autonomous
public class SideAutonomousCorner extends VelocityBaseOpMode {

	@Override
	protected void loopBeforeStart() {
		super.loopBeforeStart();
		this.configuration.safeShooterStopper(VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_UP);
		this.configuration.advanceShooterToDown();
		this.configuration.beaconPresserDisengage();
		//this.configuration.rollersDown();
	}

	protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
		final NetworkedStateMachine sm = new NetworkedStateMachine();

		//Components
		//final AutonomousComponent sideRollers = new DeploySideRollers(opModeContext, "Deploy side rollers");
		final AutonomousComponent intake = new RunIntake(1.3, true, opModeContext, "Intake particle");
		final AutonomousComponent sideSelector = new AutonomousSideSelector(opModeContext);
		final AutonomousComponent blueSide = new BlueSideCornerAutonomous(opModeContext);
		final AutonomousComponent redSide = new RedSideCornerAutonomous(opModeContext);
		final LinearStateMachine teardown = new LinearStateMachine("Teardown");
		teardown.addComponent(new Stopped(opModeContext));

		//Run
		sm.setInitialComponent(intake);
		sm.addConnection(intake, NEXT_STATE, sideSelector);
		sm.addConnection(sideSelector, AutonomousSideSelector.BLUE, blueSide);
		sm.addConnection(sideSelector, AutonomousSideSelector.RED, redSide);
		sm.addConnection(blueSide, NEXT_STATE, teardown);
		sm.addConnection(redSide, NEXT_STATE, teardown);

		return sm;
	}

	protected boolean isV2() {
		return true;
	}

	protected double targetHeading() {
		return 225.0;
	}

}
