package org.gearticks.hardware.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;

/**
 * Provides utility movement functions on top of a standard DcMotor object
 * The primary reasons to use this over DcMotor:
 * - If motor type is changed, takes care of reversing its direction and scaling encoder counts
 * - Allows for software resetting of encoders relative based off current value
 * - The set commands won't send commands to the motor controller if the value hasn't changed
*/
public class MotorWrapper {
	/**
	 * Sorts of motors we use.
	 * Keeps track of the direction they spin and their encoder count scaling.
	 */
	public enum MotorType {
		NEVEREST_60(false, 2.0 / 3.0),
		NEVEREST_40(false, 1.0),
		NEVEREST_20(true, 2.0);

		/**
		 * Whether the motor is reversed with respect to the NeveRest 40 direction
		 */
		public final boolean reversed;
		/**
		 * Ratio of the distance travelled by this motor relative to the NeveRest 40
		 * if moved to the same encoder position
		 */
		public final double scaling;

		MotorType(boolean reversed, double scaling) {
			this.reversed = reversed;
			this.scaling = scaling;
		}
	}
	/**
	 * Motor being acted upon
	 */
	private final DcMotor motor;
	/**
	 * The current power assigned to the motor
	 */
	private double lastPower;
	/**
	 * Point where encoder was "reset"
	 */
	private int encoderResetPoint;
	/**
	 * The desired mode when this motor is stopped
	 */
	private ZeroPowerBehavior lastStopMode;
	/**
	 * The current RunMode for this motor
	 */
	private RunMode lastRunMode;
	/**
	 * The current encoder target
	 */
	private int lastTarget;
	/**
	 * The motor type being used
	 */
	private final MotorType type;
	/**
	 * Whether the drive shaft turns the opposite direction
	 * as they would if a NeveRest 40 were directly attached to them.
	 * Affected both by the motor being used and the gearing between the motor and drive shaft.
	 */
	private final boolean reversed;

	/**
	 * What lastTarget will default to (check against this to see if it hasn't been set yet)
	 */
	private static final int UNSET_TARGET = Integer.MIN_VALUE;
	/**
	 * The motor power that stops a motor
	 */
	public static final double STOPPED = 0.0;
	/**
	 * The value to pass to accelLimit() to indicate no acceleration limiting
	 */
	public static final double NO_ACCEL_LIMIT = Double.POSITIVE_INFINITY;

	/**
	 * @param motor the motor being wrapped
	 * @param type the type of the motor
	 * @param reversed whether the motor is reversed
	 */
	public MotorWrapper(DcMotor motor, MotorType type, boolean reversed) {
		this.motor = motor;
		this.lastPower = STOPPED;
		this.encoderResetPoint = 0;
		this.lastStopMode = null;
		this.lastRunMode = null;
		this.lastTarget = UNSET_TARGET;
		this.type = type;
		this.reversed = type.reversed ^ reversed;
	}
	/**
	 * Assumes motor is not reversed with respect to drive shaft
	 * @param motor the motor being wrapped
	 * @param type the type of the motor
	 */
	public MotorWrapper(DcMotor motor, MotorType type) {
		this(motor, type, false);
	}
	/**
	 * Assumes motor is not reversed with respect to drive shaft
	 * and motor is a NeveRest 40
	 * @param motor the motor being wrapped
	 */
	public MotorWrapper(DcMotor motor) {
		this(motor, MotorType.NEVEREST_40);
	}

	/**
	 * Sets the power
	 * @param power the power to set (-1 to 1)
	 */
	public void setPower(double power) {
		if (Math.abs(power) > 1.0) power = Math.signum(power);
		if (this.reversed) power = -power;
		this.motor.setPower(power);
		this.lastPower = power;
	}
	/**
	 * Stops the motor (convenience method for setPower(0))
	 */
	public void stop() {
		this.setPower(STOPPED);
	}
	/**
	 * Gets the last set power
	 * @return the last set power
	 */
	public double getPower() {
		if (this.reversed) return -this.lastPower;
		else return this.lastPower;
	}
	/**
	 * Sets the desired action when stopping the motor
	 * If motor was stopped, will either brake or coast it immediately
	 * @param newMode the new stop mode
	 */
	public void setStopMode(ZeroPowerBehavior newMode) {
		this.motor.setZeroPowerBehavior(newMode);
		this.lastStopMode = newMode;
	}
	/**
	 * Sets the desired run mode
	 * @param runMode the new run mode
	 */
	public void setRunMode(RunMode runMode) {
		this.motor.setMode(runMode);
		this.lastRunMode = runMode;
	}
	/**
	 * Gets the last set run mode
	 * @return the last set run mode
	 */
	public RunMode getRunMode() {
		return this.lastRunMode;
	}
	/**
	 * Sets the encoder target for running to position.
	 * Make sure you also tell the motor to move at some power
	 * or it will never start moving to the target.
	 * You also need to switch to {@link RunMode#RUN_TO_POSITION}
	 * @param target the target to move to (relative to the encoder reset point)
	 */
	public void setTarget(int target) {
		if (this.reversed) target = -target;
		target = (int)(target / this.type.scaling);
		target += this.encoderResetPoint; //since the input target is relative to the reset point
		if (this.lastTarget == UNSET_TARGET || target != this.lastTarget) {
			this.motor.setTargetPosition(target);
			this.lastTarget = target;
		}
	}
	/**
	 * Gets the last set target
	 * @return the last set target
	 */
	public int getTarget() {
		return this.lastTarget;
	}
	/**
	 * Returns whether or not the motor is still trying to reach the target position
	 * @return whether or not the motor is still trying to reach the target position (wrapper for DcMotor)
	 */
	public boolean notAtTarget() {
		return this.motor.isBusy();
	}
	/**
	 * Limits the change in power from the previous setting (returns limited power).
	 * If trying to stop the motor, it will stop immediately.
	 * @param lastPower the power the last cycle
	 * @param desiredPower the power being requested
	 * @param maxDiff the maximum absolute value of power differences allowed (or {@link #NO_ACCEL_LIMIT})
	 * @return the acceleration-limited power
	 */
	public static double accelLimit(double lastPower, double desiredPower, double maxDiff) {
		if (desiredPower != STOPPED && maxDiff != NO_ACCEL_LIMIT && Math.abs(desiredPower - lastPower) > maxDiff) {
			return lastPower + maxDiff * Math.signum(desiredPower - lastPower);
		}
		else return desiredPower;
	}
	/**
	 * Sets the motor's power to an acceleration-limited value of desiredPower
	 * @param desiredPower the power being requested
	 * @param maxDiff the maximum absolute value of power differences allowed (or {@link #NO_ACCEL_LIMIT})
	 */
	public void accelLimit(double desiredPower, double maxDiff) {
		this.setPower(MotorWrapper.accelLimit(this.lastPower, desiredPower, maxDiff));
	}
	/**
	 * "Resets" the encoder by measuring all encoder values relative to the current value
	 */
	public void resetEncoder() {
		this.encoderResetPoint = this.motor.getCurrentPosition();
	}
	/**
	 * Gets the current value of the encoder relative to the reset point
	 * @return the current value of the encoder relative to the reset point
	 */
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