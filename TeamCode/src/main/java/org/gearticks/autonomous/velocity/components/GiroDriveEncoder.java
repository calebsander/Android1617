package org.gearticks.autonomous.velocity.components;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;

public class GiroDriveEncoder extends AutonomousComponentVelocityBase {
    private final DriveDirection direction = new DriveDirection();
    private double power = 0;
	private final double targetHeading;
	private long encoderTarget;

    /**
     *
     * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
     * @param power - between 0 and 1, input for DriveDirection
     * @param encoderTarget - target for the encoder. If the encoderPositive exceeds this target, the component transitions
     * @param configuration
     * @param id - descriptive name for logging
     */
	public GiroDriveEncoder(double targetHeading, double power, long encoderTarget, VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.power = power;
		this.targetHeading = targetHeading;
        this.encoderTarget = encoderTarget;
	}

	@Override
	public void setup(int inputPort) {
		super.setup(inputPort);
	}

	@Override
	public int run() {
		int transition = 0;
		super.run();
		
		//control giro drive
        this.direction.drive(0.0, this.power);
        this.direction.gyroCorrect(this.targetHeading, 1.0, this.getConfiguration().imu.getRelativeYaw(), 0.05, 0.1);
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
