//Provides utility movement functions on top of a standard Servo object
package org.gearticks.hardware.drive;

import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;
import java.util.Map;

public class ServoWrapper {
	//A map of servos to servo positions
	private static final Map<Servo, Double> lastPositions = new HashMap<>();
	//The value stored in lastPos before any value has been set
	private static final double UNSET = Double.MIN_VALUE;

	//Stores a desired movement to a new position at a certain speed
	private class IncrementTask {
		//The number of seconds in a nanosecond
		private static final double SECONDS_PER_NANO = 1e-9;

		//The initial position (calculated on construction)
		public final double startPos;
		//The target servo position
		public final double targetPos;
		//The total position change to occur each second
		public final double changePerSecond;
		//The value of System.nanoTime() when the incrementation started
		public final long startNanos;

		public IncrementTask(double targetPos, double changePerSecond) {
			this.startPos = ServoWrapper.this.getPos();
			this.targetPos = targetPos;
			this.changePerSecond = changePerSecond;
			this.startNanos = System.nanoTime();
		}

		//Increments the servo position towards the target, returns whether or not it has completed
		public boolean move() {
			final double totalChange = this.elapsedTime() * this.changePerSecond;
			double nextPos;
			if (this.targetPos == this.startPos) return true; //don't need to be trying to calculate new positions
			else if (this.targetPos > this.startPos) nextPos = Math.min(this.startPos + totalChange, this.targetPos); //trying to move to higher positions
			else nextPos = Math.max(this.startPos - totalChange, this.targetPos); //trying to move to lower positions
			ServoWrapper.this.setPos(nextPos);
			return nextPos == this.targetPos;
		}
		//Number of seconds that have passed since the start
		private double elapsedTime() {
			return (System.nanoTime() - this.startNanos) * SECONDS_PER_NANO;
		}
	}

	//Servo being acted upon
	private final Servo servo;
	//Last position assigned to the servo
	private double lastPos;
	//The current target to increment to
	private IncrementTask incrementTask;

	//Pass the servo to be wrapped
	public ServoWrapper(Servo servo) {
		this.servo = servo;
		this.lastPos = UNSET;
		this.incrementTask = null;
		final Double lastPosition = lastPositions.get(servo);
		if (lastPosition != null) this.setPos(lastPosition);
	}

	//Sets the pos of the servo
	public void setPos(double pos) {
		if (pos < 0.0) pos = 0.0;
		else if (pos > 1.0) pos = 1.0;
		if (pos != this.lastPos) {
			this.servo.setPosition(pos);
			this.lastPos = pos;
			ServoWrapper.lastPositions.put(this.servo, pos);
		}
	}
	//Starts moving the servo towards a target at a set speed
	public void startIncrement(double targetPos, double changePerSecond) {
		if (this.incrementTask == null || (targetPos != this.incrementTask.targetPos || changePerSecond != this.incrementTask.changePerSecond)) { //if new increment task would be different
			this.incrementTask = new IncrementTask(targetPos, changePerSecond);
		}
	}
	//Recalculates the new position based on the current increment setting
	public void updateIncrement() {
		if (!this.incrementCompleted() && this.incrementTask.move()) this.clearIncrement();
	}
	//Stops the increment task
	public void clearIncrement() {
		this.incrementTask = null;
	}
	//Returns whether the last increment task was completed (or no task was ever created)
	public boolean incrementCompleted() {
		return this.incrementTask == null;
	}
	//Returns the last set pos
	public double getPos() {
		return this.lastPos;
	}
	//Limits the change in pos from the previous setting
	public void diffLimit(double desiredPos, double maxDiff) {
		if (Math.abs(desiredPos - this.getPos()) > maxDiff) this.setPos(this.getPos() + maxDiff * Math.signum(desiredPos - this.getPos()));
		else this.setPos(desiredPos);
	}
	//Add a certain amount to the position
	public void incrementPos(double posInc) {
		this.setPos(this.lastPos + posInc);
	}

	//Returns whether or not a value has yet been written to the servo
	public boolean hasBeenSet() {
		return this.lastPos != UNSET;
	}
	public boolean equals(Object o) {
		if (!(o instanceof ServoWrapper)) return false;
		ServoWrapper otherServo = (ServoWrapper)o;
		return otherServo.servo.equals(this.servo);
	}
	public int hashCode() {
		return this.servo.hashCode();
	}
}