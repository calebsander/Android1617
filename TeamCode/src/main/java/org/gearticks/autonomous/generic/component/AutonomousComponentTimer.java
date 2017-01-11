package org.gearticks.autonomous.generic.component;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Adds a timer on top of AutonomousComponentAbstractImpl
 */
public class AutonomousComponentTimer extends AutonomousComponentAbstractImpl {
	protected final ElapsedTime stageTimer;

	public AutonomousComponentTimer() {
		super();
		this.stageTimer = new ElapsedTime();
	}

	public AutonomousComponentTimer(String id) {
		super(id);
		this.stageTimer = new ElapsedTime();
	}

	@Override
	public void onMatchStart() {
		super.onMatchStart();
		this.stageTimer.reset(); //makes sense if somehow stageTimer is used without a call to setup()
	}

	@Override
	public void setup() {
		super.setup();
		this.stageTimer.reset();
	}
}