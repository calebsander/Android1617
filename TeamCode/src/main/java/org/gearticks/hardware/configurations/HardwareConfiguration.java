package org.gearticks.hardware.configurations;

public interface HardwareConfiguration {
	//What to do to stop the robot moving (for when match has ended but OpMode hasn't)
	void stopMotion();
	//What to do when OpMode is stopped
	void teardown();
}