package org.gearticks.hardware.configurations;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController.Mode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;
import org.gearticks.dimsensors.i2c.BNO055;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.hardware.drive.TankDrive;

public class VelocityConfiguration implements HardwareConfiguration {
	public final MotorWrapper intake, shooter;
	public final MotorWrapper driveLeft, driveRight;
	public final TankDrive drive;
	public final Servo clutch, particleBlocker;
	public final CRServo shooterStopper;
	public final BNO055 imu;
	public final DigitalChannel shooterNear, shooterFar;

	public VelocityConfiguration(HardwareMap hardwareMap) {
		this.intake = new MotorWrapper((DcMotor)hardwareMap.get("intake"), MotorWrapper.MotorType.NEVEREST_40);
		this.shooter = new MotorWrapper((DcMotor)hardwareMap.get("shooter"), MotorWrapper.MotorType.NEVEREST_40);
		this.driveLeft = new MotorWrapper((DcMotor)hardwareMap.get("left"), MotorWrapper.MotorType.NEVEREST_20, true);
		this.driveRight = new MotorWrapper((DcMotor)hardwareMap.get("right"), MotorWrapper.MotorType.NEVEREST_20, true);
		this.drive = new TankDrive();
		this.drive.addLeftMotor(this.driveLeft);
		this.drive.addRightMotor(this.driveRight);
		this.driveLeft.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
		this.driveRight.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

		this.clutch = (Servo)hardwareMap.get("clutch");
		this.clutch.setPosition(MotorConstants.CLUTCH_CLUTCHED);
		this.particleBlocker = (Servo)hardwareMap.get("particleBlocker");
		this.particleBlocker.setPosition(MotorConstants.PARTICLE_BLOCKER_BLOCKING);
		this.shooterStopper = (CRServo)hardwareMap.get("shooterStopper");
		this.shooterStopper.setPower(0.0);

		this.imu = new BNO055((I2cDevice)hardwareMap.get("bno"));
		this.shooterNear = (DigitalChannel)hardwareMap.get("shooterNear");
		this.shooterNear.setMode(Mode.INPUT);
		this.shooterFar = (DigitalChannel)hardwareMap.get("shooterFar");
		this.shooterFar.setMode(Mode.INPUT);
	}
	public void teardown() {}
	public void stopMotion() {
		this.intake.stop();
		this.driveLeft.stop();
		this.driveRight.stop();
	}

	public void move(DriveDirection direction) {
		this.drive.calculatePowers(direction);
		this.drive.scaleMotorsDown();
		this.drive.commitPowers();
	}
	public void safeShooterStopped(double power) {
		if (
			(Math.signum(power) == Math.signum(MotorConstants.SHOOTER_STOPPER_UP) && this.shooterFar.getState()) ||
			(Math.signum(power) == Math.signum(MotorConstants.SHOOTER_STOPPER_DOWN) && this.shooterNear.getState())
		) {
			power = 0.0;
		}
		this.shooterStopper.setPower(power);
	}

	public static abstract class MotorConstants {
		public static final double INTAKE_IN = 1.0;
		public static final double INTAKE_OUT = -INTAKE_IN;

		public static final double SHOOTER_FORWARD = 0.5;
		public static final double SHOOTER_BACK = -SHOOTER_FORWARD;

		public static final double PARTICLE_BLOCKER_BLOCKING = 0.80;
		public static final double PARTICLE_BLOCKER_AWAY = 1.0;

		public static final double CLUTCH_CLUTCHED = 0.7;
		public static final double CLUTCH_ENGAGED = 0.3;

		public static final double SHOOTER_STOPPER_UP = 1.0;
		public static final double SHOOTER_STOPPER_DOWN = -SHOOTER_STOPPER_UP;

		//Flips a servo to the other range
		private static double invert(double pos) {
			return 1.0 - pos;
		}
	}
}
