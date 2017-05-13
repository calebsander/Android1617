//Represents a holonomic drive - stores the necessary motors and specifies how to calculate the motor powers
package org.gearticks.hardware.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.HashMap;
import java.util.Map;

public class OmniDrive extends DriveSystem {
	//The motors being controlled (FL, BL, FR, BR)
	private final MotorWrapper d, e, f, g;

	public OmniDrive(HardwareMap hardwareMap) {
		super();
		this.d = new MotorWrapper((DcMotor)hardwareMap.get("D"), MotorWrapper.MotorType.NEVEREST_40);
		this.e = new MotorWrapper((DcMotor)hardwareMap.get("E"), MotorWrapper.MotorType.NEVEREST_40);
		this.f = new MotorWrapper((DcMotor)hardwareMap.get("F"), MotorWrapper.MotorType.NEVEREST_40);
		this.g = new MotorWrapper((DcMotor)hardwareMap.get("G"), MotorWrapper.MotorType.NEVEREST_40);
		this.addMotor(this.d);
		this.addMotor(this.e);
		this.addMotor(this.f);
		this.addMotor(this.g);
		this.d.setRunMode(RunMode.RUN_USING_ENCODER);
		this.e.setRunMode(RunMode.RUN_USING_ENCODER);
		this.f.setRunMode(RunMode.RUN_USING_ENCODER);
		this.g.setRunMode(RunMode.RUN_USING_ENCODER);
	}

	protected Map<MotorWrapper, Double> powersFromDirection(DriveDirection direction) {
		final Map<MotorWrapper, Double> powers = new HashMap<>();
		powers.put(this.d, + direction.getX() + direction.getY() - direction.getS());
		powers.put(this.e, - direction.getX() + direction.getY() - direction.getS());
		powers.put(this.f, + direction.getX() - direction.getY() - direction.getS());
		powers.put(this.g, - direction.getX() - direction.getY() - direction.getS());
		return powers;
	}
}