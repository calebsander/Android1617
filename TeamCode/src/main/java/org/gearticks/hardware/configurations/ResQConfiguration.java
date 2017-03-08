package org.gearticks.hardware.configurations;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.hardware.drive.ServoWrapper;
import org.gearticks.hardware.drive.TankDrive;

public class ResQConfiguration implements HardwareConfiguration {
	public final MotorWrapper screw, lift;
	public final MotorWrapper door;
	public MotorWrapper fl, bl, fr, br;
	public CRServo bucketLift, allClear;
	public ServoWrapper hook, liftBrake, bladeLeft, bladeRight;
	public ServoWrapper climbers, plow, zipLeft, zipRight, churroHooks, bumper;
	public final TankDrive drive;

	public ResQConfiguration(HardwareMap hardwareMap) {
		this.lift = new MotorWrapper((DcMotor)hardwareMap.get("lift"), MotorWrapper.MotorType.NEVEREST_60);
		this.screw = new MotorWrapper((DcMotor)hardwareMap.get("screw"), MotorWrapper.MotorType.NEVEREST_20);
		this.door = new MotorWrapper((DcMotor)hardwareMap.get("door"), MotorWrapper.MotorType.NEVEREST_60);
		this.bucketLift = (CRServo)hardwareMap.get("bucketLift");
		this.fl = new MotorWrapper((DcMotor)hardwareMap.get("fl"), MotorWrapper.MotorType.NEVEREST_40);
		this.fr = new MotorWrapper((DcMotor)hardwareMap.get("fr"), MotorWrapper.MotorType.NEVEREST_40);
		this.bl = new MotorWrapper((DcMotor)hardwareMap.get("bl"), MotorWrapper.MotorType.NEVEREST_40);
		this.br = new MotorWrapper((DcMotor)hardwareMap.get("br"), MotorWrapper.MotorType.NEVEREST_40);
		this.drive = new TankDrive();
		this.drive.addLeftMotor(this.fl);
		this.drive.addLeftMotor(this.bl);
		this.drive.addRightMotor(this.fr);
		this.drive.addRightMotor(this.br);

		this.bucketLift = (CRServo)hardwareMap.get("bucketLift");
		this.bucketLift.setPower(0.0);
		this.hook = new ServoWrapper((Servo)hardwareMap.get("hook"));
		this.hook.setPos(MotorConstants.HOOK_DOWN);
		this.liftBrake = new ServoWrapper((Servo)hardwareMap.get("liftBrake"));
		this.liftBrake.setPos(MotorConstants.BRAKE_OFF);
		this.zipLeft = new ServoWrapper((Servo)hardwareMap.get("zipLeft"));
		this.zipLeft.setPos(MotorConstants.ZIP_LEFT_AWAY);
		this.zipRight = new ServoWrapper((Servo)hardwareMap.get("zipRight"));
		this.zipRight.setPos(MotorConstants.ZIP_RIGHT_AWAY);
		this.churroHooks = new ServoWrapper((Servo)hardwareMap.get("churroHooks"));
		this.churroHooks.setPos(MotorConstants.CHURRO_HOOKS_UP);
		this.bladeLeft = new ServoWrapper((Servo)hardwareMap.get("bladeLeft"));
		this.bladeLeft.setPos(MotorConstants.BLADE_LEFT_UP);
		this.bladeRight = new ServoWrapper((Servo)hardwareMap.get("bladeRight"));
		this.bladeRight.setPos(MotorConstants.BLADE_RIGHT_UP);
		this.bumper = new ServoWrapper((Servo)hardwareMap.get("bumper"));
		this.bumper.setPos(MotorConstants.BUMPER_START);
		this.climbers = new ServoWrapper((Servo)hardwareMap.get("climbers"));
		this.climbers.setPos(MotorConstants.CLIMBER_STICK_START);
		this.plow = new ServoWrapper((Servo)hardwareMap.get("plow"));
		this.plow.setPos(MotorConstants.PLOW_UP);
		this.allClear = (CRServo)hardwareMap.get("allClear");
		this.allClear.setPower(MotorConstants.ALL_CLEAR_STOPPED);
		this.fl.setRunMode(RunMode.RUN_WITHOUT_ENCODER);
		this.fr.setRunMode(RunMode.RUN_WITHOUT_ENCODER);
		this.bl.setRunMode(RunMode.RUN_WITHOUT_ENCODER);
		this.br.setRunMode(RunMode.RUN_WITHOUT_ENCODER);
	}
	public void tearDown() {}
	public void stopMotion() {
		this.screw.setPower(MotorWrapper.STOPPED);
		this.lift.setPower(MotorWrapper.STOPPED);
		this.door.setPower(MotorWrapper.STOPPED);
		this.bucketLift.setPower(MotorWrapper.STOPPED);
		this.fl.setPower(MotorWrapper.STOPPED);
		this.fr.setPower(MotorWrapper.STOPPED);
		this.bl.setPower(MotorWrapper.STOPPED);
		this.br.setPower(MotorWrapper.STOPPED);
	}
	public void move(DriveDirection direction, double accelLimit) {
		this.drive.calculatePowers(direction);
		this.drive.scaleMotorsDown();
		this.drive.accelLimit(accelLimit);
		this.drive.commitPowers();
	}

	public static abstract class MotorConstants {
		public static final double SCREW_BACKWARD = 1.0;
		public static final double SCREW_FORWARD = -SCREW_BACKWARD;

		public static final double DOOR_LEFT = 1.0;
		public static final double DOOR_RIGHT = -DOOR_LEFT;

		public static final double BUCKET_UP = 1.0;
		public static final double BUCKET_DOWN = -BUCKET_UP;

		public static final double LIFT_UP = 1.0;
		public static final double LIFT_DOWN = -LIFT_UP;

		public static final double BUCKET_LIFT_UP = 1.0;
		public static final double BUCKET_LIFT_DOWN = -BUCKET_LIFT_UP;

		public static final double CHURRO_HOOKS_UP = 0.55;
		public static final double CHURRO_HOOKS_DOWN = 0.13;

		public static final double ZIP_RIGHT_AWAY = 0.02;
		public static final double ZIP_RIGHT_MANUAL = 0.65;
		//Flips a servo to the other range
		private static double invert(double pos) {
			return 1.0 - pos;
		}
		public static final double ZIP_LEFT_AWAY = invert(ZIP_RIGHT_AWAY);
		public static final double ZIP_LEFT_MANUAL = 0.3;

		public static final double HOOK_DOWN = 1.0;
		public static final double HOOK_UP = 0.15;
		public static final double HOOK_NEUTRAL = HOOK_DOWN + (HOOK_UP - HOOK_DOWN) * 0.75;
		public static final double HOOK_INCREMENT_SPEED = 0.3;

		public static final double BRAKE_OFF = 0.8;
		public static final double BRAKE_ON = 0.7;

		public static final double BLADE_LEFT_DOWN = 0.0;
		public static final double BLADE_LEFT_UP = 0.80;
		public static final double BLADE_LEFT_NEUTRAL = 0.5;
		public static final double BLADE_LEFT_SPEED = 0.6;
		public static final double BLADE_RIGHT_DOWN = 0.85;
		public static final double BLADE_RIGHT_UP = 0.1;
		public static final double BLADE_RIGHT_NEUTRAL = 0.25;
		public static final double BLADE_RIGHT_SPEED = 0.7;

		public static final double CLIMBER_STICK_START = 0.0;
		public static final double CLIMBER_STICK_OUT = 0.37;
		public static final double CLIMBER_STICK_RIGHT = 0.65;
		public static final double CLIMBER_STICK_FAR_RIGHT = 0.9;
		public static final double CLIMBER_STICK_INCREMENT_SPEED = 0.6;

		public static final double BUMPER_START = 0.15;
		public static final double BUMPER_RIGHT = 0.7;
		public static final double BUMPER_LEFT = 0.92;

		public static final double PLOW_DOWN = 0.1;
		public static final double PLOW_UP = 0.9;

		public static final double ALL_CLEAR_STOPPED = -0.05;
		public static final double ALL_CLEAR_OUT = 1.0;
		public static final double ALL_CLEAR_IN = -1.0;
	}
}
