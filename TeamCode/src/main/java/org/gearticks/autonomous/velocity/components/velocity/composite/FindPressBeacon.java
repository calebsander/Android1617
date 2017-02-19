package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Created by BenMorris on 2/17/2017.
 */

public class FindPressBeacon extends NetworkedStateMachine {
	public  FindPressBeacon(OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(id);

		//TODO: Program beacons
	}
}
