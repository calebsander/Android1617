package org.gearticks.autonomous.generic.component;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Adds a timer on top of AutonomousComponentAbstractImpl
 */
public class AutonomousComponentTimer<TRANSITION_TYPE extends Enum<?>> extends AutonomousComponentAbstractImpl<TRANSITION_TYPE> {
	protected final ElapsedTime stageTimer;

	public AutonomousComponentTimer(Class<TRANSITION_TYPE> transitionClass) {
		super(transitionClass);
		this.stageTimer = new ElapsedTime();
	}

	public AutonomousComponentTimer(Class<TRANSITION_TYPE> transitionClass, String id) {
		super(transitionClass, id);
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
