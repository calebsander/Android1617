package org.gearticks.autonomous.generic.component;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.logging.Logger;

/**
 * Adds:
 * * Timer
 * * getLogger()
 *
 */
public class AutonomousComponentBase extends AutonomousComponentAbstractImpl {

    private final ElapsedTime stageTimer = new ElapsedTime();

	public AutonomousComponentBase() {
		super();
	}

	public AutonomousComponentBase(String id) {
		super(id);
	}

    @Override
    public void initializeAtMatchStart() {
        super.initializeAtMatchStart();
        this.stageTimer.reset();//makes sense if somehow stageTimer is used without a call to setup()
    }

    /**
     * Default is empty so subclass doesn't have to implement if not necessary
     */
    @Override
    public void setup(int inputPort) {
        super.setup(inputPort);
        this.stageTimer.reset();
    }
	
	public @NonNull Logger getLogger(){
		return Logger.getLogger(this.getClass().getSimpleName());
	}

    protected @NonNull ElapsedTime getStageTimer() {
        return stageTimer;
    }
}
