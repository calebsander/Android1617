package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.composite.FixBeacon;
import org.gearticks.autonomous.velocity.components.velocity.composite.SidePressBeaconButton;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.LoadBall;
import org.gearticks.autonomous.velocity.components.velocity.single.MoveShooterDown;
import org.gearticks.autonomous.velocity.components.velocity.single.ResetSnake;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide;
import org.gearticks.autonomous.velocity.components.velocity.single.ShootBall;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration;

@Autonomous
public class SideAutonomousBeaconTester extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent() {
        final VuforiaConfiguration vuforiaConfiguration = new VuforiaConfiguration();
        final LinearStateMachine sm = new LinearStateMachine();
        SelectBeaconSide.PictureResult pictureResult = new SelectBeaconSide.PictureResult();

        int distanceFromWall = 9;
        //sm.addComponent(new DisengageBeaconServo(this.configuration, "Disengage beacon button"));

        //Drive to wall
        sm.addComponent(new GiroBananaTurnEncoder(0.0, 18.0, 0.7, 500, configuration, "BTR 18 - 1000"));
        sm.addComponent(new GiroBananaTurnEncoder(18, 90.0, 0.8, 7700, configuration, "BTR 90 - 7700"));
        sm.addComponent(new GiroTurn(180.0, 0.1, 20, configuration, "Straighten out"));
        sm.addComponent(new DeploySideRollers(configuration, "Deploy rollers"));
        //addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, -0.4, 2000, configuration, "Drive backwards"));
        sm.addComponent(new GiroDriveAlongWallLine(distanceFromWall-1, 180.0, 0.15, 6000, configuration, "Drive to beacon"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180.0, -0.17, 500, configuration, "Adjust to white line"));
        sm.addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, -0.17, 25, configuration, "Align"));
        //addComponent(new DebugPause(gamepads, telemetry, configuration, "pause"));
        sm.addComponent(new SidePressBeaconButton(vuforiaConfiguration, configuration, "Press Button"));
        sm.addComponent(new FixBeacon(pictureResult, this.configuration, vuforiaConfiguration));




        sm.addComponent(new Stopped(this.configuration));

        return sm;
    }
    protected boolean isV2() {
        return true;
    }
}
