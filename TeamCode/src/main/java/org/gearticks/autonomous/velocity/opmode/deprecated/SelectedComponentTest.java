package org.gearticks.autonomous.velocity.opmode.deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import java.util.HashMap;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.SelectedComponent;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.AllianceOption;

@Autonomous
@Disabled
public class SelectedComponentTest extends VelocityBaseOpMode {
	protected AutonomousComponent getComponent(final OpModeContext<VelocityConfiguration> opModeContext) {
		final LinearStateMachine sm = new LinearStateMachine();
		sm.addComponent(new SelectedComponent<>(AllianceOption.allianceOption, new HashMap<AllianceOption, AutonomousComponent>() {{
			put(AllianceOption.BLUE, new GiroDriveEncoder(0.0, 1.0, 2000, opModeContext, "Blue: Drive forward"));
			put(AllianceOption.RED, new GiroTurn(90.0, 1.0, 10, opModeContext, "Red: turn"));
		}}));
		sm.addComponent(new DebugPause(opModeContext));
		return sm;
	}
	protected boolean isV2() {
		return true;
	}
}