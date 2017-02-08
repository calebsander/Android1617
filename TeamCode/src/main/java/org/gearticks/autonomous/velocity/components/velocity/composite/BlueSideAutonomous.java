package org.gearticks.autonomous.velocity.components.velocity.composite;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageSideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.LoadBall;
import org.gearticks.autonomous.velocity.components.velocity.single.ResetSnake;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration;

public class BlueSideAutonomous extends LinearStateMachine {
    public BlueSideAutonomous(int distanceFromWall, @NonNull GamepadWrapper[] gamepads, Telemetry telemetry, @NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration) {
        super();

        //Drive to wall
        addComponent(new GiroBananaTurnEncoder(0.0, 30.0, 0.6, 1000, configuration, "BTR 30 - 1000"));
        addComponent(new GiroBananaTurnEncoder(30.0, 90.0, 0.7, 6000, configuration, "BTR 90 - 6000"));
        addComponent(new GiroTurn(180.0, 0.1, 20, configuration, "Straighten out"));
        addComponent(new DeploySideRollers(configuration, "Deploy rollers"));
        addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, -0.25, 2000, configuration, "Drive backwards"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180.0, 0.25, 4000, configuration, "Drive to beacon"));

        //Press beacon
        addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180.0, -0.17, 500, configuration, "Adjust to white line"));
        addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, -0.17, 25, configuration, "Align"));
        addComponent(new Wait(0.3, "Wait for 0.3 sec"));
        addComponent(new GiroTurn(180.0, 0.1, 20, configuration, "Straighten out"));
        addComponent(new SidePressBeaconButton(vuforiaConfiguration, configuration, "Press Button"));

        //Go to second beacon
        addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, -0.5, 3000, configuration, "Range sensor drive along wall"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180.0, -0.25, 2000, configuration, "Range sensor drive along wall to line"));
        //sm.addComponent(new GiroDriveToLine(180, 0.7, 8000, this.configuration, "Drive to white line"));

        //Press second beacon
        addComponent(new GiroDriveToLine(180, 0.05, 500, configuration, "Adjust to white line"));
        addComponent(new Wait(0.3, "Wait for 0.3 sec"));
        addComponent(new GiroTurn(180.0, configuration, "Straighten out"));
        addComponent(new SidePressBeaconButton(vuforiaConfiguration, configuration, "Press Button"));

        //Cap ball
        addComponent(new DisengageSideRollers(configuration, "Raise rollers"));
        addComponent(new GiroTurn(225.0, configuration, "Turn to cap ball"));
        addComponent(new GiroDriveEncoder(225.0, 0.7, 8000, configuration, "Hit cap ball and park"));
    }
}
