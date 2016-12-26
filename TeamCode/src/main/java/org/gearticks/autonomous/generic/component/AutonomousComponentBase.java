package org.gearticks.autonomous.generic.component;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;

import java.util.logging.Logger;

/**
 * Adds:
 * * Timer (TODO)
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
    public void initialize() {
        super.initialize();
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
	
	public Logger getLogger(){
		return Logger.getLogger(this.getClass().getSimpleName());
	}

    protected ElapsedTime getStageTimer() {
        return stageTimer;
    }
}
