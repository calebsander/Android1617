package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import java.util.HashMap;
import java.util.Map;
import org.gearticks.hardware.configurations.ResQConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
@Disabled
public class EncoderTest extends BaseOpMode {
	private ResQConfiguration configuration;
	private Map<String, MotorWrapper> driveMotors;
	private DriveDirection direction;

	private void resetEncoders() {
		for (MotorWrapper driveMotor : this.driveMotors.values()) {
			driveMotor.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
			driveMotor.setRunMode(RunMode.RUN_WITHOUT_ENCODER);
		}
	}
	protected void initialize() {
		this.configuration = new ResQConfiguration(this.hardwareMap);
		this.driveMotors = new HashMap<>();
		this.driveMotors.put("fl", this.configuration.fl);
		this.driveMotors.put("fr", this.configuration.fr);
		this.driveMotors.put("bl", this.configuration.bl);
		this.driveMotors.put("br", this.configuration.br);
		this.resetEncoders();
		this.direction = new DriveDirection();
	}
	public void loopAfterStart() {
		if (this.gamepads[0].getA() && !this.gamepads[0].getLast().getA()) this.resetEncoders();
		for (Map.Entry<String, MotorWrapper> nameAndMotor : this.driveMotors.entrySet()) this.telemetry.addData(nameAndMotor.getKey(), nameAndMotor.getValue().encoderValue());
		this.direction.drive(0.0, this.gamepads[0].getLeftY());
		this.direction.turn(this.gamepads[0].getRightX());
		this.configuration.move(this.direction);
	}
}