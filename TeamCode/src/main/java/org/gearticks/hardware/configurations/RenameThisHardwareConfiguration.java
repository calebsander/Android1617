package org.gearticks.hardware.configurations;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class RenameThisHardwareConfiguration implements HardwareConfiguration {
	public RenameThisHardwareConfiguration(HardwareMap hardwareMap) {
		//e.g. this.fl = (DcMotor)hardwareMap.get("fl");
	}

	public void stopMotion() {}
	public void teardown() {}

	public static abstract class MotorConstants {
		//e.g. public static final double SQUID_IN = 1.0;
	}
	public static abstract class ServoConstants {
		//e.g. public static final double CLIMBERS_OUT = 0.38;
	}
}