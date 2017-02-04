package org.gearticks.opmodes.test.sensors;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
@Disabled
public class WhiteLineIRTest extends BaseOpMode {
	private VelocityConfiguration configuration;
	private DriveDirection direction;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.direction = new DriveDirection();
	}
	protected void loopAfterStart() {
//		this.telemetry.addData("IR sensor", this.configuration.whiteLineSensor.getState());
		this.direction.drive(0.0, scaleStick(this.gamepads[0].getLeftY()));
		this.direction.turn(scaleStick(this.gamepads[0].getRightX()));
		this.configuration.move(this.direction, MotorWrapper.NO_ACCEL_LIMIT);
	}

	private static double scaleStick(double x) {
		return x * x * x;
	}
}