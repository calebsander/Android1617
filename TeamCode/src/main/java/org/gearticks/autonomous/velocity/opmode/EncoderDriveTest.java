package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.velocity.components.GiroDriveEncoder;

@Autonomous
public class EncoderDriveTest extends VelocityBaseOpMode {
	protected AutonomousComponent getComponent() {
		return new GiroDriveEncoder(0.0, 1.0, 1000, this.configuration, "Drive");
	}
}