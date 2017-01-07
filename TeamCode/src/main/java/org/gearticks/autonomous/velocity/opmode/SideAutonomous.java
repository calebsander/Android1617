package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.EngageBeaconServo;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.vuforia.VuforiaConfiguration;

@Autonomous
public class SideAutonomous extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent() {
        final VuforiaConfiguration vuforiaConfiguration = new VuforiaConfiguration();
        final LinearStateMachine sm = new LinearStateMachine();
        //Shoot 2 balls
//        sm.addComponent(new MoveShooterDown(this.configuration, "MoveShooterDown"));
//        sm.addComponent(new Wait(0.3, "Wait for 0.5 sec"));
//        sm.addComponent(new ShootBall(this.configuration, "Shoot 1st ball"));
//        sm.addComponent(new MoveShooterDown(this.configuration, "Move Shooter Down"));
//        sm.addComponent(new LoadBall(this.configuration, "Load 2nd ball"));
//        sm.addComponent(new ResetSnake(this.configuration, "Reset Snake"));
//        sm.addComponent(new ShootBall(this.configuration, "Shoot 2nd ball"));

        //Drive to beacon
        sm.addComponent(new GiroDriveEncoder(90.0, 0.7, 1700, this.configuration, "Banana turn off wall for 1700 ticks"));
        sm.addComponent(new Wait(0.3, "Wait"));
        sm.addComponent(new GiroTurn(90.0, this.configuration, "Correct heading"));
        sm.addComponent(new GiroDriveEncoder(0.0, 0.7, 2900, this.configuration, "Banana turn to run parallel to wall"));
        sm.addComponent(new Wait(0.3, "Wait"));
        sm.addComponent(new GiroTurn(90.0, this.configuration, "Correct heading"));
        sm.addComponent(new GiroDriveToLine(0.0, 0.20, 5000, this.configuration, "Drive to 1st white line"));

        //Go to 1st beacon
//        sm.addComponent(new VuforiaIn(500F, true, vuforiaConfiguration, this.configuration, "Drive to near target"));
//        sm.addComponent(new FacePicture(true, vuforiaConfiguration, this.configuration, "Face near target"));
//        sm.addComponent(new VuforiaIn(175F, true, vuforiaConfiguration, this.configuration, "Drive closer to near target"));

        // Press 1st beacon
//        sm.addComponent(new SidePressBeaconButton(vuforiaConfiguration, this.configuration, "Press beacon button"));

        // Drive to second beacon
//        sm.addComponent(new GiroDriveEncoder(0.0, 0.25, 2000, this.configuration, "Drive off 1st white line"));
//        sm.addComponent(new GiroDriveToLine(0.0, 0.20, 3000, this.configuration, "Drive to 2nd white line"));

        // Press 2nd beacon
//        sm.addComponent(new SidePressBeaconButton(vuforiaConfiguration, this.configuration, "Press beacon button"));

        sm.addComponent(new EngageBeaconServo(this.configuration, "Engage"));
        sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));
        sm.addComponent(new DisengageBeaconServo(this.configuration, "Disengage"));
        sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));
        sm.addComponent(new EngageBeaconServo(this.configuration, "Engage"));
        sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));
        sm.addComponent(new DisengageBeaconServo(this.configuration, "Disengage"));
        sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));
        sm.addComponent(new EngageBeaconServo(this.configuration, "Engage"));


        sm.addComponent(new Stopped(this.configuration));

        return sm;
    }
}
