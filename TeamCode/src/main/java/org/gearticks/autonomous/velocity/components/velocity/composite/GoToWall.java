package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.AutonomousPositionSelector;
import org.gearticks.autonomous.velocity.components.experimental.AutonomousSideSelector;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Created by BenMorris on 2/17/2017.
 */

public class GoToWall extends NetworkedStateMachine {
	public GoToWall(OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(id);

		final AutonomousComponent positionSelector = new AutonomousPositionSelector(opModeContext);
		final AutonomousComponent sideSelector = new AutonomousSideSelector(opModeContext);

		setInitialComponent(positionSelector);
		addConnection(positionSelector, NEXT_STATE, sideSelector);

		//TODO: Program go to wall
	}
}
