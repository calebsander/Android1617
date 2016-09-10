package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDevice;
import org.gearticks.dimsensors.i2c.BNO055;

@Autonomous(name = "BNO055 Test", group = "test")
@Disabled
public class BNO055Test extends OpMode {
	private BNO055 imu;

	public void init() {
		this.imu = new BNO055((I2cDevice)this.hardwareMap.get("bno"));
		this.imu.eulerRequest.startReading();
	}
	public void loop() {
		final BNO055.EulerAngle heading = this.imu.getHeading();
		if (heading != null) {
			this.telemetry.clear();
			this.telemetry.addData("yaw", heading.yaw);
			this.telemetry.addData("pitch", heading.pitch);
			this.telemetry.addData("roll", heading.roll);
		}
	}
	public void stop() {
		this.imu.eulerRequest.stopReading();
	}
}