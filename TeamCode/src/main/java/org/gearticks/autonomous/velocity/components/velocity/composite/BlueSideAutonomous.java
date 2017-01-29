package org.gearticks.autonomous.velocity.components.velocity.composite;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.single.LoadBall;
import org.gearticks.autonomous.velocity.components.velocity.single.ResetSnake;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration;

public class BlueSideAutonomous extends LinearStateMachine {
    public BlueSideAutonomous(int distanceFromWall, @NonNull GamepadWrapper[] gamepads, Telemetry telemetry, @NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration) {
        super();

        //Drive to wall
        addComponent(new GiroBananaTurnEncoder(0.0, 20.0, 0.25, 1000, configuration, "BTR 20 - 1000"));
        addComponent(new Wait(0.3, "Wait for 0.5 sec"));
        addComponent(new GiroBananaTurnEncoder(20.0, 90.0, 0.5, 5000, configuration, "BTR 90 - 4000"));
        addComponent(new GiroTurn(180.0, configuration, "Straighten out"));
        addComponent(new DebugPause(gamepads, telemetry ,configuration, "Press A to continue"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180.0, -0.20, 6000, configuration, "Range sensor drive along wall"));

        //Press beacon
        addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180, 0.05, 500, configuration, "Adjust to white line"));
        addComponent(new Wait(0.3, "Wait for 0.5 sec"));
        //sm.addComponent(new GiroTurn(180.0, this.configuration, "Straighten out"));
        addComponent(new SidePressBeaconButton(180.0, vuforiaConfiguration, configuration, "Press Button"));

        //Go to second beacon
        addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, 0.25, 3000, configuration, "Range sensor drive along wall"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180.0, 0.25, 4000, configuration, "Range sensor drive along wall to line"));
        //sm.addComponent(new GiroDriveToLine(180, 0.7, 8000, this.configuration, "Drive to white line"));

        //Press beacon
        addComponent(new GiroDriveToLine(180, -0.05, 500, configuration, "Adjust to white line"));
        addComponent(new Wait(0.3, "Wait for 0.5 sec"));
        addComponent(new GiroTurn(180.0, configuration, "Straighten out"));
        addComponent(new SidePressBeaconButton(180.0, vuforiaConfiguration, configuration, "Press Button"));
    }
}
