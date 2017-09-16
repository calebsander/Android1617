package org.gearticks.opmodes.utility;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.Map;
import org.gearticks.joystickoptions.ValuesJoystickOption;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
public class ServoPositions extends BaseOpMode {
	private ValuesJoystickOption<String> servoOption;
	private Servo selectedServo;
	private boolean locking;

	protected void initialize() {
		final DeviceMapping<Servo> servoMap = this.hardwareMap.servo;
		final String[] servoNames = new String[servoMap.size()];
		int servoIndex = 0;
		for (final Map.Entry<String, Servo> nameAndServo : servoMap.entrySet()) {
			servoNames[servoIndex] = nameAndServo.getKey();
			servoIndex++;
		}
		this.servoOption = new ValuesJoystickOption<>("Servo", servoNames);
		this.addOption(this.servoOption);
		this.locking = false;
	}
	protected void matchStart() {
		this.selectedServo = (Servo)this.hardwareMap.get(this.servoOption.getRawSelectedOption());
	}
	protected void loopAfterStart() {
		this.telemetry.addData("Instructions", "Y to toggle lock");
		if (this.gamepads[0].getY() && !this.gamepads[0].getLast().getY()) this.locking = !this.locking;
		if (!this.locking) this.selectedServo.setPosition((this.gamepads[0].getLeftY() + 1.0) * 0.5);
		this.telemetry.addData("Position", this.selectedServo.getPosition());
	}
}