package org.gearticks.opmodes.test.motors;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDevice;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.dimsensors.i2c.GearticksBNO055;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.hardware.drive.TankDrive;
import org.gearticks.opmodes.BaseOpMode;
import org.gearticks.opmodes.teleop.VelocityDrive;

/**
 * Created by BenMorris on 7/15/2017.
 */

@Autonomous
public class BananaTurnTest extends BaseOpMode {
	private TankDrive drive;
	private DriveDirection direction;
	private VelocityConfiguration configuration;
	private final double leftY = 1.0;
	private final double rightX = Math.sqrt(3.0) / 2.0;

	protected void initialize() {
		this.drive = new TankDrive();
		this.drive.addLeftMotor(new MotorWrapper(this.hardwareMap.dcMotor.get("left"), MotorWrapper.MotorType.NEVEREST_20, true));
		this.drive.addRightMotor(new MotorWrapper(this.hardwareMap.dcMotor.get("right"), MotorWrapper.MotorType.NEVEREST_20, true));
		this.direction = new DriveDirection();
		this.configuration = new VelocityConfiguration(this.hardwareMap, false, true);
	}

	public void loopAfterStart() {
		if(this.configuration.imu.getRelativeYaw() < 45.0) {
			final int driveGamepad;
			double yScaleFactor = 1.0;
			double sScaleFactor = Math.max(0.5, Math.abs(this.leftY * yScaleFactor));

			final double accelLimit = 0.06;
			final double maxPower = 1.0;

			this.direction.drive(0.0, scaleStick(this.leftY) * yScaleFactor);
			this.direction.turn(scaleStick(this.rightX) * sScaleFactor);

			this.drive.calculatePowers(this.direction);
			this.drive.scaleMotorsDown(maxPower);
			this.drive.accelLimit(accelLimit);
			this.drive.commitPowers();

			this.telemetry.addData("Left motor power: ", this.direction.getY() + this.direction.getS());
			this.telemetry.addData("Right motor power: ", -this.direction.getY() + this.direction.getS());
		}
		else {
			this.direction.stopDrive();
			this.drive.calculatePowers(this.direction);
			this.drive.scaleMotorsDown(1.0);
			this.drive.accelLimit(0.06);
			this.drive.commitPowers();
		}
	}

	private static double scaleStick(double stick) {
		return stick * stick * stick;
	}
}
