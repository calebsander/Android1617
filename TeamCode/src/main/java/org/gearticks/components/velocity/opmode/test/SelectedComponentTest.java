package org.gearticks.components.velocity.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import java.util.HashMap;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.generic.statemachine.SelectedComponent;
import org.gearticks.components.velocity.component.debug.DebugPause;
import org.gearticks.components.hardwareagnostic.component.GiroDriveEncoder;
import org.gearticks.components.hardwareagnostic.component.GiroTurn;
import org.gearticks.components.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.AllianceOption;

@Autonomous
@Disabled
public class SelectedComponentTest extends VelocityBaseOpMode {
	protected OpModeComponent<?> getComponent(final OpModeContext<VelocityConfiguration> opModeContext) {
		this.addOption(AllianceOption.allianceOption);
		final LinearStateMachine sm = new LinearStateMachine();
		sm.addComponent(new SelectedComponent<>(AllianceOption.allianceOption, AllianceOption.class, new HashMap<AllianceOption, OpModeComponent<?>>() {{
			put(AllianceOption.BLUE, new GiroDriveEncoder(0.0, 1.0, 2000, opModeContext, "Blue: Drive forward"));
			put(AllianceOption.RED, new GiroTurn(90.0, opModeContext, "Red: turn"));
		}}));
		sm.addComponent(new DebugPause(opModeContext));
		return sm;
	}
	protected boolean isV2() {
		return true;
	}
}