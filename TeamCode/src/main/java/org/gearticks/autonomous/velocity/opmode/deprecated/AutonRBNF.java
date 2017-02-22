package org.gearticks.autonomous.velocity.opmode.deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.SelectedComponent;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideAutonumous.BlueSideAutonomousNF;
import org.gearticks.autonomous.velocity.components.velocity.composite.RedSideAutonomous;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.AllianceOption;

import java.util.HashMap;

@Autonomous
@Disabled
public class AutonRBNF extends VelocityBaseOpMode {
	private static final int DISTANCE_FROM_WALL = 9;

	protected AutonomousComponent getComponent(final OpModeContext<VelocityConfiguration> opModeContext) {
		final LinearStateMachine sm = new LinearStateMachine();
		sm.addComponent(new SelectedComponent<>(AllianceOption.allianceOption, new HashMap<AllianceOption, AutonomousComponent>() {{
			put(AllianceOption.BLUE, new BlueSideAutonomousNF(DISTANCE_FROM_WALL, opModeContext));
			put(AllianceOption.RED, new RedSideAutonomous(DISTANCE_FROM_WALL, opModeContext));
		}}));
		sm.addComponent(new DebugPause(opModeContext));
		return sm;
	}
	protected boolean isV2() {
		return true;
	}
}