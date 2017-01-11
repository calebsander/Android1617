package org.gearticks.autonomous.generic.component;

import android.support.annotation.NonNull;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.logging.Logger;

/**
 * Adds a timer on top of AutonomousComponentAbstractImpl
 */
public class AutonomousComponentBase extends AutonomousComponentAbstractImpl {
	protected final ElapsedTime stageTimer;

	public AutonomousComponentBase() {
		super();
		this.stageTimer = new ElapsedTime();
	}

	public AutonomousComponentBase(String id) {
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
