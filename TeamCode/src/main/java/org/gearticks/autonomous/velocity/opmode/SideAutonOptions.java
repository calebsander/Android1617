package org.gearticks.autonomous.velocity.opmode;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.velocity.composite.PositionToWall;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration;

/**
 * Created by BenMorris on 2/11/2017.
 */

public class SideAutonOptions extends VelocityBaseOpMode{
	private static final int DISTANCE_FROM_WALL = 9;

	@Override
	protected void loopBeforeStart() {
		super.loopBeforeStart();
		this.configuration.safeShooterStopper(VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_UP);
		this.configuration.advanceShooterToDown();
	}

	protected AutonomousComponent getComponent() {
		final VuforiaConfiguration vuforiaConfiguration = new VuforiaConfiguration();
		final NetworkedStateMachine sm = new NetworkedStateMachine();

		//Initialization components
		final AutonomousComponent disengageBeaconServo = new DisengageBeaconServo(this.configuration, "Disengage beacon button");

		//Initialize
		sm.setInitialComponent(disengageBeaconServo);

		//Position components
		final AutonomousComponent position1 = new PositionToWall(1, this.configuration, "Position 1");
		final AutonomousComponent position2 = new PositionToWall(2, this.configuration, "Position 2");

		return sm;
	}
	protected boolean isV2() {
		return true;
	}
}

