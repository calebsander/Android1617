/*Provides utility movement functions on top of a standard DcMotor object
	The primary reasons to use this over DcMotor:
	-Allows for easier resetting of encoders
	-The set commands won't send commands to the motor controller if the value hasn't changed
*/
package org.gearticks.hardware.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;

public class MotorWrapper {
	//What sort of motor it is - so we can figure out what direction to turn it
	public enum MotorType {
		NEVEREST_60(false, 2.0 / 3.0),
		NEVEREST_40(false, 1.0),
		NEVEREST_20(true, 2.0);

		//Whether the motor is reversed with respect to the NeveRest 40 direction
		public final boolean reversed;
		//Ratio of the distance travelled by an equivalent rotation of this motor to the NeveRest 40
		public final double scaling;

		MotorType(boolean reversed, double scaling) {
			this.reversed = reversed;
			this.scaling = scaling;
		}
	}
	//Motor being acted upon
	private DcMotor motor;
	//Last power assigned to the motor
	private double lastPower;
	//Point where encoder was "reset"
	private int encoderResetPoint;
	//The desired StopMode for this motor
	private ZeroPowerBehavior lastStopMode;
	//The current RunMode for this motor
	private RunMode lastRunMode;
	//The current encoder target
	private int lastTarget;
	//The motor type being used
	private MotorType type;
	//Whether the wheels turn the opposite direction as they would if a NeveRest 40 were directly attached to them
	private boolean reversed;

	//What lastTarget will default to (check against this to see if it hasn't been set yet)
	private static final int UNSET_TARGET = Integer.MIN_VALUE;
	//The motor power that stops a motor
	public static final double STOPPED = 0.0;

	//Pass the motor to be wrapped
	public MotorWrapper(DcMotor motor, MotorType type, boolean reversed) {
		this.motor = motor;
		this.lastPower = STOPPED;
		this.encoderResetPoint = 0;
		this.lastStopMode = ZeroPowerBehavior.BRAKE;
		this.lastRunMode = RunMode.RUN_WITHOUT_ENCODER;
		this.lastTarget = UNSET_TARGET;
		this.type = type;
		this.reversed = type.reversed ^ reversed;
	}
	public MotorWrapper(DcMotor motor, MotorType type) {
		this(motor, type, false);
	}
	public MotorWrapper(DcMotor motor) {
		this(motor, MotorType.NEVEREST_40);
	}

	//Sets the power
	public void setPower(double power) {
		if (Math.abs(power) > 1.0) power = Math.signum(power);
		if (this.reversed) power = -power;
		if (power != this.lastPower) { //don't do anything if the power hasn't changed
			this.motor.setPower(power);
			this.lastPower = power;
		}
	}
	//Stops the motor (using set stop mode)
	public void stop() {
		this.setPower(STOPPED);
	}
	//Returns the last set power
	public double getPower() {
		if (this.reversed) return -this.lastPower;
		else return this.lastPower;
	}
	//Sets the desired action when stopping the motor
	//If motor was stopped, will either brake or coast it immediately
	public void setStopMode(ZeroPowerBehavior newMode) {
		if (newMode != this.lastStopMode) {
			this.motor.setZeroPowerBehavior(newMode);
			this.lastStopMode = newMode;
		}
	}
	//Sets the desired run mode
	public void setRunMode(RunMode runMode) {
		if (runMode != this.lastRunMode) {
			this.motor.setMode(runMode);
			this.lastRunMode = runMode;
		}
	}
	//Sets the encoder target for running to position
	//Make sure you also tell the motor to move at some power or it will never start moving to the target
	public void setTarget(int target) {
		if (this.reversed) target = -target;
		target = (int)(target / this.type.scaling);
		target += this.encoderResetPoint; //since the input target is relative to the reset point
		if (this.lastTarget == UNSET_TARGET || target != this.lastTarget) {
			this.motor.setTargetPosition(target);
			this.lastTarget = target;
		}
	}
	//Returns whether or not the motor is still trying to reach the target position (wrapper for DcMotor)
	public boolean isBusy() {
		return this.motor.isBusy();
	}
	//Limits the change in power from the previous setting (returns limited power)
	public static double accelLimit(double lastPower, double desiredPower, double maxDiff) {
		if (Math.abs(desiredPower - lastPower) > maxDiff) return lastPower + maxDiff * Math.signum(desiredPower - lastPower);
		else return desiredPower;
	}
	//Actually sets the motor's power to an acceleration-limited value of desiredPower
	public void accelLimit(double desiredPower, double maxDiff) {
		this.setPower(MotorWrapper.accelLimit(this.lastPower, desiredPower, maxDiff));
	}
	//Limits acceleration unless trying to stop the motor
	public void startupLimit(double desiredPower, double maxDiff) {
		if (Math.abs(desiredPower - STOPPED) < 0.05) this.stop();
		else this.accelLimit(desiredPower, maxDiff);
	}
	//"Resets" the encoder
	public void resetEncoder() {
		this.encoderResetPoint = this.motor.getCurrentPosition();
	}
	//Gets the current value of the encoder after the reset
	public int encoderValue() {
		final int value = this.motor.getCurrentPosition() - this.encoderResetPoint;
		final int scaled = (int)(value * this.type.scaling);
		if (this.reversed) return -scaled;
		else return scaled;
	}
	public boolean equals(Object o) {
		if (!(o instanceof MotorWrapper)) return false;
		MotorWrapper otherMotor = (MotorWrapper)o;
		return otherMotor.motor.equals(this.motor);
	}
	public int hashCode() {
		return this.motor.hashCode();
	}
}