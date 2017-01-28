package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveToRangeDistance;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.velocity.composite.SidePressBeaconButton;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.vuforia.VuforiaConfiguration;

@Autonomous
public class SideAutonomous extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent() {
        final VuforiaConfiguration vuforiaConfiguration = new VuforiaConfiguration();
        final LinearStateMachine sm = new LinearStateMachine();

        int distanceFromWall = 10;
        sm.addComponent(new DisengageBeaconServo(this.configuration, "Disengage beacon button"));


        //Shoot 2 balls
//        sm.addComponent(new MoveShooterDown(this.configuration, "MoveShooterDown"));
//        sm.addComponent(new Wait(0.3, "Wait for 0.5 sec"));
//        sm.addComponent(new ShootBall(this.configuration, "Shoot 1st ball"));
//        sm.addComponent(new MoveShooterDown(this.configuration, "Move Shooter Down"));
//        sm.addComponent(new LoadBall(this.configuration, "Load 2nd ball"));
//        sm.addComponent(new ResetSnake(this.configuration, "Reset Snake"));
//        sm.addComponent(new ShootBall(this.configuration, "Shoot 2nd ball"));

        //Blue side

        //Drive to wall
        //sm.addComponent(new GiroDriveEncoder(0.0, 0.25, 500, this.configuration, "Drive forward for 500 ticks"));
        //sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));
        sm.addComponent(new GiroBananaTurnEncoder(0.0, 20.0, 0.25, 1000, this.configuration, "Banana Turn right"));
        sm.addComponent(new Wait(0.3, "Wait for 0.5 sec"));
        sm.addComponent(new GiroBananaTurnEncoder(20.0, 90.0, 0.5, 5000, this.configuration, "Banana Turn right"));
        //sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));
        sm.addComponent(new GiroTurn(180.0, this.configuration, "Straighten out"));
        //sm.addComponent(new GiroBananaTurnEncoder(90.0, 180.0, 0.15, 1000, this.configuration, "Banana Turn left"));

        sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));

        sm.addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180.0, -0.20, 6000, this.configuration, "Range sensor drive along wall"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180, 0.05, 500, this.configuration, "Adjust to white line"));
        sm.addComponent(new Wait(0.3, "Wait for 0.5 sec"));
        //sm.addComponent(new GiroTurn(180.0, this.configuration, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(180.0, vuforiaConfiguration, this.configuration, "Press Button"));
        sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));


        //sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));

        //Go to second beacon
        sm.addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, 0.25, 3000, this.configuration, "Range sensor drive along wall"));
        sm.addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180.0, 0.25, 4000, this.configuration, "Range sensor drive along wall to line"));
        //sm.addComponent(new GiroDriveToLine(180, 0.7, 8000, this.configuration, "Drive to white line"));

        //Press beacon
        sm.addComponent(new GiroDriveToLine(180, -0.05, 500, this.configuration, "Adjust to white line"));
        sm.addComponent(new Wait(0.3, "Wait for 0.5 sec"));
        sm.addComponent(new GiroTurn(180.0, this.configuration, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(180.0, vuforiaConfiguration, this.configuration, "Press Button"));
        sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));



        sm.addComponent(new Stopped(this.configuration));

        return sm;
    }
}
