package org.gearticks.autonomous.velocity.opmode;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.selectors.BeaconSelector;
import org.gearticks.autonomous.velocity.components.selectors.PositionSelector;
import org.gearticks.autonomous.velocity.components.velocity.composite.ParkCenter;
import org.gearticks.autonomous.velocity.components.velocity.composite.PositionToWall;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration;
import static org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl.NEXT_STATE;


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
		final AutonomousComponent positionSelector = new PositionSelector(this.configuration);
		final AutonomousComponent beaconSelector = new BeaconSelector(this.configuration);
		final AutonomousComponent wait = new Wait(0.2, "Wait for 0.3 seconds");

		//Autonomous components
		final AutonomousComponent position1 = new PositionToWall(1, this.configuration, "Position 1");
		final AutonomousComponent position2 = new PositionToWall(2, this.configuration, "Position 2");
		final AutonomousComponent park = new ParkCenter(1, this.configuration, "Park in center from pos 1");

		//Initialize
		sm.setInitialComponent(disengageBeaconServo);
		sm.addConnection(disengageBeaconServo, NEXT_STATE, positionSelector);

		//Positions
		sm.addConnection(positionSelector, PositionSelector.POSITION_1, position1);
		sm.addConnection(positionSelector, PositionSelector.POSITION_2, position2);
		sm.addConnection(position1, NEXT_STATE, wait);
		sm.addConnection(position2, NEXT_STATE, wait);

		//Beacons
		sm.addConnection(wait, BeaconSelector.NO_BEACONS, park);

		return sm;
	}
	protected boolean isV2() {
		return true;
	}
}

