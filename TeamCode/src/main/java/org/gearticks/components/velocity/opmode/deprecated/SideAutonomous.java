package org.gearticks.components.velocity.opmode.deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.hardwareagnostic.component.GiroBananaTurnEncoder;
import org.gearticks.components.velocity.component.drive.GiroDriveAlongWallEncoder;
import org.gearticks.components.velocity.component.drive.GiroDriveAlongWallLine;
import org.gearticks.components.velocity.component.debug.DebugPause;
import org.gearticks.components.velocity.component.composite.SidePressBeaconButton;
import org.gearticks.components.velocity.component.drive.GiroDriveToLine;
import org.gearticks.components.hardwareagnostic.component.GiroTurn;
import org.gearticks.components.hardwareagnostic.component.Stopped;
import org.gearticks.components.hardwareagnostic.component.Wait;
import org.gearticks.components.velocity.component.servo.DisengageBeaconServo;
import org.gearticks.components.velocity.component.servo.LoadBall;
import org.gearticks.components.velocity.component.motor.MoveShooterDown;
import org.gearticks.components.velocity.component.servo.ResetSnake;
import org.gearticks.components.velocity.component.motor.ShootBall;
import org.gearticks.components.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
@Disabled
public class SideAutonomous extends VelocityBaseOpMode {
    protected OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        int distanceFromWall = 10;
        sm.addComponent(new DisengageBeaconServo(opModeContext, "Disengage beacon button"));


        //Shoot 2 balls
        sm.addComponent(new MoveShooterDown(opModeContext, "MoveShooterDown"));
        sm.addComponent(new Wait(0.3, "Wait for 0.5 sec"));
        sm.addComponent(new ShootBall(opModeContext, "Shoot 1st ball"));
        sm.addComponent(new MoveShooterDown(opModeContext, "Move Shooter Down"));
        sm.addComponent(new LoadBall(opModeContext, "Load 2nd ball"));
        sm.addComponent(new ResetSnake(true, opModeContext, "Reset Snake"));
        sm.addComponent(new ShootBall(opModeContext, "Shoot 2nd ball"));

        //Blue side

        //Drive to wall
        //sm.addComponent(new GiroDriveEncoder(0.0, 0.25, 500, this.configuration, "Drive forward for 500 ticks"));
        //sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));
        sm.addComponent(new GiroBananaTurnEncoder(0.0, 20.0, 0.25, 1000, opModeContext, "Banana Turn right"));
        sm.addComponent(new Wait(0.3, "Wait for 0.5 sec"));
        sm.addComponent(new GiroBananaTurnEncoder(20.0, 90.0, 0.5, 5000, opModeContext, "Banana Turn right"));
        //sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));
        sm.addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
        //sm.addComponent(new GiroBananaTurnEncoder(90.0, 180.0, 0.15, 1000, this.configuration, "Banana Turn left"));

        sm.addComponent(new DebugPause(opModeContext));

        sm.addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180.0, -0.20, 6000, opModeContext, "Range sensor drive along wall"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180, 0.05, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new Wait(0.3, "Wait for 0.5 sec"));
        //sm.addComponent(new GiroTurn(180.0, this.configuration, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(true, opModeContext, "Press Button"));
        sm.addComponent(new DebugPause(opModeContext));


        //sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));

        //Go to second beacon
        sm.addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, 0.25, 3000, opModeContext, "Range sensor drive along wall"));
        sm.addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180.0, 0.25, 4000, opModeContext, "Range sensor drive along wall to line"));
        //sm.addComponent(new GiroDriveToLine(180, 0.7, 8000, this.configuration, "Drive to white line"));

        //Press beacon
        sm.addComponent(new GiroDriveToLine(180, -0.05, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new Wait(0.3, "Wait for 0.5 sec"));
        sm.addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(true, opModeContext, "Press Button"));
        sm.addComponent(new DebugPause(opModeContext));



        sm.addComponent(new Stopped(opModeContext));

        return sm;
    }
    protected boolean isV2() {
        return true;
    }
}
