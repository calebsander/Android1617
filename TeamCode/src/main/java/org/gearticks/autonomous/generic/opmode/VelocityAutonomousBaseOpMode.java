package org.gearticks.autonomous.generic.opmode;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.opmode.VelocityBaseOpMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Only applies when using Stage enum.
 * Supports a generic Stage enum in combination with optional components.
 * The Stage enum is passed using generics.
 * @param <T>
 */
@Autonomous
public abstract class VelocityAutonomousBaseOpMode<T extends Enum<T>> extends VelocityBaseOpMode {
    protected ElapsedTime stageTimer;
    protected T stage;
	@NonNull
    Map<T, AutonomousComponent> componentOfStageMap = new HashMap<>();

	protected void setComponent(T stage, AutonomousComponent ac){
		this.componentOfStageMap.put(stage, ac);
	}

	/**
	 * Can return null
	 * @param stage
	 * @return Null if not registered for stage
	 */
	protected AutonomousComponent getComponent(T stage){
		return this.componentOfStageMap.get(stage);
	}

//	protected void initialize() {
//		super.initialize();
//	}
//	protected void loopBeforeStart() {
//		super.loopBeforeStart();
//	}
//	protected void matchStart() {
//       super.matchStart();
//	}
//	protected void loopAfterStart() {
//	}
//	protected void matchEnd() {
//		super.matchEnd();
//	}


    protected void runStage() {
        if (this.getComponent(this.stage).run() > 0){
            this.nextStage();
        }
    }
    protected void nextStage() {
        //tearDown current stage
        if (this.getComponent(this.stage) !=  null){
            this.getComponent(this.stage).tearDown();
        }
        //move to next stage
        this.stage = this.getStageValues()[this.stage.ordinal() + 1];
        //setup next stage
        if (this.getComponent(this.stage) != null){
            this.getComponent(this.stage).setup(1);
        }
        this.stageTimer.reset();
    }

    /**
     * Implement by copying the following in the OpMode that defines the Stage enum:
     * protected Stage[] getStageValues(){return Stage.values();}
     * This cannot be implemented generically
     * @return
     */
    protected abstract T[] getStageValues();

    /**
     * Below doesn't work: Java doesn't know the values() method on T
     * @return
     */
//    protected T[] getStageValues(){
//        return T.values();
//    }


    public T getStage(){
        return this.stage;
    }
    public void setStage(T stage){
        this.stage = stage;
    }

    protected void initializeComponents(){
        for (AutonomousComponent ac : this.componentOfStageMap.values()){
            ac.initialize();
        }
    }

}