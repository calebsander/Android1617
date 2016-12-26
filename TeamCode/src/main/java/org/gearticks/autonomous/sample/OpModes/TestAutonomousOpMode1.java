package org.gearticks.autonomous.sample.OpModes;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.opmode.VelocityAutonomousBaseOpMode;
import org.gearticks.autonomous.velocity.components.GiroDriveEncoder;

/**
 * Test OpMode with AutonomousComponents icw Stage and switch.
 * Using 'regular' AutonomousComponents classes
 */

public class TestAutonomousOpMode1 extends VelocityAutonomousBaseOpMode<TestAutonomousOpMode1.Stage> {
    protected ElapsedTime stageTimer;
    //private Stage stage;

    protected enum Stage {
        STAGE1,
        STAGE2;
    }
    protected Stage[] getStageValues(){
        return Stage.values();
    }

    protected void createComponents(){
        AutonomousComponent stage1ac = new GiroDriveEncoder(1,0,2000,this.configuration,"stage1");
        AutonomousComponent stage2ac = new GiroDriveEncoder(1,100,2000,this.configuration,"stage1");
        this.setComponent(Stage.STAGE1, stage1ac);
        this.setComponent(Stage.STAGE2, stage2ac);

    }

    protected void createAndInitializeComponents(){
        this.createComponents();
        this.initializeComponents();
    }

    protected void initialize() {
        this.stage = Stage.values()[0];
        this.createAndInitializeComponents();
    }

    protected void loopAfterStart() {
        switch (this.getStage()) {
            case  STAGE1:
                this.runStage();
                break;
            case STAGE2:
                this.runStage();
                break;
        }
    }

//    private void runStage() {
//        if (this.getComponent(this.stage).run() > 0){
//            this.nextStage();
//        }
//    }
//    private void nextStage() {
//        //tearDown current stage
//        if (this.getComponent(this.stage) !=  null){
//            this.getComponent(this.stage).tearDown();
//        }
//        //move to next stage
//        this.stage = Stage.values()[this.stage.ordinal() + 1];
//        //setup next stage
//        if (this.getComponent(this.stage) != null){
//            this.getComponent(this.stage).setup(1);
//        }
//        this.stageTimer.reset();
//    }
}
