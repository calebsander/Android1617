package org.gearticks.autonomous.sample;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.velocity.components.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.Wait;
import org.gearticks.hardware.configurations.VelocityConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a sample of a 'traditional' switch-based staging when using AutonomousComponents for some stages
 *
 */
public class SampleOpModeSwitch extends OpModeTest {
    VelocityConfiguration configuration;
	
//	private enum Stage{
//		WAIT1 (new Wait(2000, "Wait for 2 sec")),
//		DRIVE1 (new GiroDriveEncoder(2000, 100, this.configuration, "Drive for 2 sec heading 100")),
//		DRIVE2 (null),
//		WAIT2 (new Wait(2000, "Wait for 2 sec"));
//
//		private AutonomousComponent component;
//
//		private Stage(AutonomousComponent ac) {
//			this.component = ac;
//		}
//		public AutonomousComponent getComponent(){
//			return this.component;
//		}
//        public void setComponent(AutonomousComponent ac){
//            this.component = ac;
//        }
//	}

    private enum Stage{
        WAIT1 ,
        DRIVE1,
        DRIVE2,
        WAIT2
    }
	
	private Stage stage;
    @NonNull
    Map<Stage, AutonomousComponent> componentOfStageMap = new HashMap<>();

    protected void setComponent(Stage stage, AutonomousComponent ac){
        this.componentOfStageMap.put(stage, ac);
    }

    /**
     * Can return null
     * @param stage
     * @return Null if not registered for stage
     */
    protected AutonomousComponent getComponent(Stage stage){
        return this.componentOfStageMap.get(stage);
    }

	@Override
	public void initialize(){
        this.setComponent(Stage.WAIT1, new Wait(2000, this.configuration, "Wait for 2 sec"));
        this.setComponent(Stage.DRIVE1, new GiroDriveEncoder(2000, 100, 2000, this.configuration, "Drive for 2 sec heading 100"));
        this.setComponent(Stage.WAIT2, new Wait(2000, this.configuration, "Wait for 2 sec"));

		for (AutonomousComponent ac : this.componentOfStageMap.values()){
			ac.initialize();
		}
	}
	
	public void setup(){
		this.stage = Stage.WAIT1;
		//this.stage.getComponent().setup(1);
        this.getComponent(this.stage).setup(1);
	}
	
	public void loop(){
		
		switch (this.stage) {
		case WAIT1:
			if (this.getComponent(this.stage).run() > 0){
				this.nextStage();
			}
			break;
		case DRIVE1:
			if (this.getComponent(this.stage).run() > 0){
				this.nextStage();
			}
			break;
		case DRIVE2:
			/*
			 * Not necessary to always use an AutonomousComponent
			 */
			if (true){
				this.nextStage();
			}
			break;
		case WAIT2:
			if (this.getComponent(this.stage).run() > 0){
				//stop?
			}
			break;
		default:
			break;
		}
		
	}
	
	private void nextStage() {
		//tearDown current stage
		if (this.getComponent(this.stage) !=  null){
            this.getComponent(this.stage).tearDown();
		}
		//move to next stage
		this.stage = Stage.values()[this.stage.ordinal() + 1];
		//setup next stage
		if (this.getComponent(this.stage) != null){
            this.getComponent(this.stage).setup(1);
		}
	}
	
	public static void main(String[] args) {
		SampleOpModeSwitch opMode = new SampleOpModeSwitch();
		
		runOpMode(opMode, 10);
	}

}
