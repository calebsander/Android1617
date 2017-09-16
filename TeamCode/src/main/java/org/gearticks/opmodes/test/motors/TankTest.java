package org.gearticks.opmodes.test.motors;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.hardware.drive.MotorWrapper.MotorType;
import org.gearticks.hardware.drive.TankDrive;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
@Disabled
public class TankTest extends BaseOpMode {
	private TankDrive drive;
	private DriveDirection direction;

	protected void initialize() {
		this.drive = new TankDrive();
		this.drive.addLeftMotor(new MotorWrapper(this.hardwareMap.dcMotor.get("LeftTop"), MotorType.NEVEREST_20, true));
		this.drive.addLeftMotor(new MotorWrapper(this.hardwareMap.dcMotor.get("LeftBottom"), MotorType.NEVEREST_20, true));
		this.drive.addRightMotor(new MotorWrapper(this.hardwareMap.dcMotor.get("RightTop"), MotorType.NEVEREST_20, true));
		this.drive.addRightMotor(new MotorWrapper(this.hardwareMap.dcMotor.get("RightBottom"), MotorType.NEVEREST_20, true));
		this.direction = new DriveDirection();
	}
	protected void loopAfterStart() {
		this.direction.drive(0.0, scaleStick(this.gamepads[0].getLeftY()));
		this.direction.turn(scaleStick(this.gamepads[0].getRightX()));
		this.drive.calculatePowers(this.direction);
		this.drive.scaleMotorsDown();
		this.drive.commitPowers();
	}
	private static float scaleStick(float stick) {
		return stick * stick * stick;
	}
}