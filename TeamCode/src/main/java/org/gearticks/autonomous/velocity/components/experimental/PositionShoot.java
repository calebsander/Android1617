package org.gearticks.autonomous.velocity.components.experimental;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.AutonomousPositionSelector;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.PositionOption;

/**
 * Created by BenMorris on 2/17/2017.
 */

public class PositionShoot extends NetworkedStateMachine{
	public PositionShoot(OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(id);

		final AutonomousComponent positionSelector = new AutonomousPositionSelector(opModeContext);
		final AutonomousComponent shoot = new Shoot2Balls(true, opModeContext, "Shoot 2 particles");

		setInitialComponent(positionSelector);
		addConnection(positionSelector, AutonomousPositionSelector.POSITION_1, shoot);
		//TODO: Program position 2
		addExitConnection(shoot, NEXT_STATE);
	}
}
