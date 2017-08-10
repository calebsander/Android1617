package org.gearticks.opmodes.utility;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.gearticks.joystickoptions.ValuesJoystickOption;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
public class MotorPowers extends BaseOpMode {
	private ValuesJoystickOption<String> motorOption;
	private DcMotorSimple selectedMotor;
	private boolean locking;

	private static void addAllNames(DeviceMapping<?> deviceMap, List<String> names) {
		deviceMap.entrySet().stream()
			.map(Entry::getKey)
			.forEach(names::add);
	}

	protected void initialize() {
		final DeviceMapping<DcMotor> motorMap = this.hardwareMap.dcMotor;
		final DeviceMapping<CRServo> crServoMap = this.hardwareMap.crservo;
		final int motorCount = motorMap.size() + crServoMap.size();
		final List<String> motorNames = new ArrayList<>(motorCount);
		addAllNames(motorMap, motorNames);
		addAllNames(crServoMap, motorNames);
		this.motorOption = new ValuesJoystickOption<>("Motor", motorNames.toArray(new String[motorCount]));
		this.addOption(this.motorOption);
		this.locking = false;
	}
	protected void matchStart() {
		this.selectedMotor = (DcMotorSimple)this.hardwareMap.get(this.motorOption.getRawSelectedOption());
	}
	protected void loopAfterStart() {
		this.telemetry.addData("Instructions", "Y to toggle lock");
		if (this.gamepads[0].getY() && !this.gamepads[0].getLast().getY()) this.locking = !this.locking;
		if (!this.locking) this.selectedMotor.setPower(this.gamepads[0].getLeftY());
		this.telemetry.addData("Position", this.selectedMotor.getPower());
	}
}