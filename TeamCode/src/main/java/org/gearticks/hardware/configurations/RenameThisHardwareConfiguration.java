package org.gearticks.hardware.configurations;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class RenameThisHardwareConfiguration implements HardwareConfiguration {
	public RenameThisHardwareConfiguration(HardwareMap hardwareMap) {
		//e.g. this.fl = (DcMotor)hardwareMap.get("fl");
	}

	public void stopMotion() {}
	public void teardown() {}
}