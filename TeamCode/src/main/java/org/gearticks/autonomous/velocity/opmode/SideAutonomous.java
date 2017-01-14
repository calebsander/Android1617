package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveToRangeDistance;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.velocity.composite.SidePressBeaconButton;
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
        sm.addComponent(new GiroDriveEncoder(0.0, 0.7, 1000, this.configuration, "Drive forward for 1700 ticks"));
        sm.addComponent(new Wait(0.3, "Wait"));
        sm.addComponent(new GiroTurn(215.0, this.configuration, "Flip direction"));
        sm.addComponent(new GiroDriveEncoder(215.0, -0.7, 6500, this.configuration, "Drive to wall"));
        sm.addComponent(new Wait(0.3, "Wait"));
        sm.addComponent(new GiroTurn(200.0, this.configuration, "Angle less"));
        sm.addComponent(new Wait(0.3, "Wait"));
        sm.addComponent(new GiroDriveToRangeDistance(15, 200.0, -0.2, 3000, this.configuration, "Range sensor drive to wall"));
        sm.addComponent(new Wait(0.3, "Wait"));
        sm.addComponent(new GiroTurn(180.0, this.configuration, "Straighten out"));
        sm.addComponent(new GiroDriveAlongWallLine(15, 180.0, -0.15, 5000, this.configuration, "Range sensor drive along wall"));
        sm.addComponent(new GiroTurn(180.0, this.configuration, "Straighten out"));
        sm.addComponent(new GiroDriveToLine(180, 0.05, 15, this.configuration, "Adjust to white line"));
        sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));
        sm.addComponent(new SidePressBeaconButton(180.0, vuforiaConfiguration, this.configuration, "Press Button"));

        sm.addComponent(new Stopped(this.configuration));

        return sm;
    }
}
