package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoderStraighten;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;

@Autonomous
@Disabled
public class EncoderDriveTest extends VelocityBaseOpMode {
	protected AutonomousComponent getComponent() {
		return new GiroDriveEncoderStraighten(0.0, 1.0, 2000, this.configuration, "Drive");
	}
	protected boolean isV2() {
		return false;
	}
}