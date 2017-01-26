package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
@Disabled
@Deprecated
public class BeaconPresserMotorTest extends BaseOpMode {
	private static final double PRESS_SECONDS = 0.4;

	private VelocityConfiguration configuration;
	private ElapsedTime pressTimer;
	private double pressPower;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.pressTimer = new ElapsedTime();
		this.pressPower = MotorWrapper.STOPPED;
	}
	protected void loopAfterStart() {
		if (this.pressTimer.seconds() < PRESS_SECONDS) { //still pressing
			this.configuration.beaconBumpers.setRunMode(RunMode.RUN_WITHOUT_ENCODER);
			this.configuration.beaconBumpers.setPower(this.pressPower);
		}
		else { //pressing done
			this.configuration.beaconBumpers.setRunMode(RunMode.RUN_TO_POSITION);
			this.configuration.beaconBumpers.setTarget(0); //go back to neutral
			this.configuration.beaconBumpers.setPower(this.pressPower);
			if (this.gamepads[0].getX()) {
				this.pressPower = MotorConstants.BEACON_MOTOR_LEFT;
				this.pressTimer.reset();
			}
			else if (this.gamepads[0].getB()) {
				this.pressPower = MotorConstants.BEACON_MOTOR_RIGHT;
				this.pressTimer.reset();
			}
		}
	}
}