package org.gearticks.components.velocity.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.hardwareagnostic.component.GiroDriveEncoderStraighten;
import org.gearticks.components.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
@Disabled
public class EncoderDriveTest extends VelocityBaseOpMode {
	protected OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
		return new GiroDriveEncoderStraighten(0.0, 1.0, 2000, opModeContext, "Drive");
	}
	protected boolean isV2() {
		return false;
	}
}