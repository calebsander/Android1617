package org.gearticks.autonomous.sample.opmodes;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.autonomous.generic.opmode.VelocityBaseOpMode;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.GyroDriveEncoder;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;

public class TestAutonomousOpMode2 extends VelocityBaseOpMode {
    private class GyroDriveEncoderInner extends AutonomousComponentVelocityBase {
        private final DriveDirection direction;

        public GyroDriveEncoderInner(VelocityConfiguration configuration, String id) {
            super(configuration, id);
            this.direction = new DriveDirection();
        }

        @Override
        public int run() {
            final int superTransition = super.run();
            if (superTransition != NOT_DONE) return superTransition;

            //control gyro drive
            this.direction.drive(0.0, 0.7); //you can access the attribute of the enclosing class
            this.direction.gyroCorrect(0.0, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
            this.configuration.move(this.direction, 0.06);

            if (this.configuration.encoderPositive() > 2000) return NEXT_STATE;
            else return NOT_DONE;
        }
    }

    protected AutonomousComponent getComponent() {
        final LinearStateMachine sm = new LinearStateMachine();
        sm.addComponent(new GyroDriveEncoder(0.0, 1.0, 2000, this.configuration, "stage1"));
        sm.addComponent(new GyroDriveEncoderInner(this.configuration, "gyro drive inner"));
        sm.addComponent(new AutonomousComponentVelocityBase(this.configuration, "giro drive anonymous") { //Anonymous class
            private final DriveDirection direction = new DriveDirection();

            @Override
            public int run() {
                final int superTransition = super.run();
                if (superTransition != NOT_DONE) return superTransition;

                //control gyro drive
                this.direction.drive(0.0, 0.7); //you can access the attribute of the enclosing class
                this.direction.gyroCorrect(0.0, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
                this.configuration.move(this.direction, 0.06);

                if (this.configuration.encoderPositive() > 2000) return NEXT_STATE;
                else return NOT_DONE;
            }
        });
        return sm;
    }
}