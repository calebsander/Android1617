package org.gearticks.autonomous.sample.OpModes;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.autonomous.generic.opmode.VelocityAutonomousBaseOpMode;
import org.gearticks.autonomous.velocity.components.GiroDriveEncoder;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;

/**
 * Test OpMode with AutonomousComponents icw Stage and switch.
 * Example with Inner Class and with Anonymous Class
 */

public class TestAutonomousOpMode2 extends VelocityAutonomousBaseOpMode<TestAutonomousOpMode2.Stage> {
    protected ElapsedTime stageTimer;
    //private Stage stage;
    protected DriveDirection direction;

    protected enum Stage {
        STAGE1,
        STAGE2,
        STAGE3
    }
    protected Stage[] getStageValues(){
        return Stage.values();
    }



    class GiroDriveEncoderInner extends AutonomousComponentVelocityBase{
        public GiroDriveEncoderInner(@NonNull VelocityConfiguration configuration, String id) {
            super(configuration, id);
        }
        @Override
        public int run() {
            int transition = 0;

            //control giro drive
            direction.drive(0.0, 0.7); //you can access the attribute of the enclosing class
            TestAutonomousOpMode2.this.direction.drive(0.0, 0.7); //the same but doing it more explicitly
            direction.gyroCorrect(0, 1.0, this.getConfiguration().imu.getRelativeYaw(), 0.05, 0.1);
            this.getConfiguration().move(direction, 0.06);

            if (this.getConfiguration().encoderPositive() > 2000) {
                transition = 1;
            }

            return transition;
        }

        @Override
        public void tearDown() {
            //stop motors
            direction.stopDrive();
            this.getConfiguration().move(direction, 0.06);
        }
    }

    protected void createComponents(){
        AutonomousComponent stage1ac = new GiroDriveEncoder(1,0,2000,this.configuration,"stage1"); //Regular
        AutonomousComponent stage2ac = this.new GiroDriveEncoderInner(this.configuration,"giro drive inner"); //Inner class
        AutonomousComponent stage3ac = new AutonomousComponentVelocityBase(this.configuration, "giro drive anonymous"){ //Anonymous class
            @Override
            public int run() {
                int transition = 0;

                //control giro drive
                direction.drive(0.0, 0.7); //you can access the attribute of the enclosing class
                TestAutonomousOpMode2.this.direction.drive(0.0, 0.7); //the same but doing it more explicitly
                direction.gyroCorrect(0, 1.0, this.getConfiguration().imu.getRelativeYaw(), 0.05, 0.1);
                this.getConfiguration().move(direction, 0.06);

                if (this.getConfiguration().encoderPositive() > 2000) {
                    transition = 1;
                }

                return transition;
            }

            @Override
            public void tearDown() {
                //stop motors
                direction.stopDrive();
                this.getConfiguration().move(direction, 0.06);
            }
        };
        this.setComponent(Stage.STAGE1, stage1ac);
        this.setComponent(Stage.STAGE2, stage2ac);
        this.setComponent(Stage.STAGE3, stage3ac);

    }

    protected void createAndInitializeComponents(){
        this.createComponents();
        this.initializeComponents();
    }

    protected void initialize() {
        this.direction = new DriveDirection();
        this.setStage(Stage.values()[0]);
        this.createAndInitializeComponents();
    }

    protected void loopAfterStart() {
        switch (this.getStage()) {
            case STAGE1:
                this.runStage();
                break;
            case STAGE2:
                this.runStage();
                break;
            case STAGE3:
                this.runStage();
                break;
        }
    }


}
