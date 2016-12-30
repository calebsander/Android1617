package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;

public class GiroDriveEncoderBeacon extends AutonomousComponentVelocityBase {
    private final DriveDirection direction = new DriveDirection();
    private double power = 0;
	private final double targetHeading;
	private long encoderTarget;
	private boolean allianceColorIsBlue;
	private final double angleMultiplier;
	private double buttonAngle;

    /**
     *
     * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
     * @param power - between 0 and 1, input for DriveDirection
     * @param encoderTarget - target for the encoder. If the encoderPositive exceeds this target, the component transitions
     * @param configuration
     * @param id - descriptive name for logging
     */
	public GiroDriveEncoderBeacon(double targetHeading, double power, long encoderTarget, @NonNull VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.power = power;
		this.targetHeading = targetHeading;
        this.encoderTarget = encoderTarget;
		this.allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
		if (this.allianceColorIsBlue) angleMultiplier = 1.0; //angles were calculated for blue side
		else angleMultiplier = -1.0; //invert all angles for red side
	}

	@Override
	public void setup(int inputPort) {
		super.setup(inputPort);
		this.getConfiguration().resetEncoder();
	}

	@Override
	public int run() {
		int transition = 0;
		super.run();
		
		//control giro drive
		this.buttonAngle = 90.0 * angleMultiplier;
		this.direction.drive(0.0, this.power);
        this.direction.gyroCorrect(this.buttonAngle += this.targetHeading, 1.0, this.getConfiguration().imu.getRelativeYaw(), 0.05, 0.1);
        this.getConfiguration().move(this.direction, 0.06);

        if (this.getConfiguration().encoderPositive() > this.encoderTarget) {
           transition = 1;
        }

		return transition;
	}

	@Override
	public void tearDown() {
		super.tearDown();
		//stop motors
        this.direction.stopDrive();
        this.getConfiguration().move(this.direction, 0.06);
	}
	
	
	

}
