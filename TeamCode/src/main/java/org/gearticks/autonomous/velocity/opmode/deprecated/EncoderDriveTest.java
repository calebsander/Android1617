package org.gearticks.autonomous.velocity.opmode.deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoderStraighten;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
@Disabled
public class EncoderDriveTest extends VelocityBaseOpMode {
	protected AutonomousComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
		return new GiroDriveEncoderStraighten(0.0, 1.0, 2000, opModeContext, "Drive");
	}
	protected boolean isV2() {
		return false;
	}
}