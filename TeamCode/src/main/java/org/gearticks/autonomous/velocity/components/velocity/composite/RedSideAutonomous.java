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
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoderNoStop;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageSideRollers;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration;

public class RedSideAutonomous extends LinearStateMachine {
    public RedSideAutonomous(int distanceFromWall, @NonNull GamepadWrapper[] gamepads, Telemetry telemetry, @NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration) {
        super();

        //Drive to wall
        addComponent(new DeploySideRollers(configuration, "Deploy rollers"));
        addComponent(new GiroBananaTurnEncoder(0.0, 70.0, 0.7, 500, configuration, "BTL"));
        addComponent(new GiroBananaTurnEncoder(70.0, 0.0, 0.6, 7000, configuration, "BTR"));
        addComponent(new GiroTurn(0.0, configuration, "Straighten out"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall, 0.0, 0.4, 6000, configuration, "Range sensor drive along wall"));

        //Press beacon
        addComponent(new GiroDriveEncoder(0.0, -0.1, 300, configuration, "Center on beacon"));
        addComponent(new GiroTurn(0.0, configuration, "Straighten out"));
        addComponent(new SidePressBeaconButton(vuforiaConfiguration, configuration, "Press Button"));

        //Go to second beacon
        addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 0.0, -0.7, 3000, configuration, "Range sensor drive along wall"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall, 0.0, -0.25, 4000, configuration, "Range sensor drive along wall to line"));
        //sm.addComponent(new GiroDriveToLine(180, 0.7, 8000, this.configuration, "Drive to white line"));

        //Press beacon
        addComponent(new GiroDriveToLine(0, 0.05, 500, configuration, "Adjust to white line"));
        addComponent(new GiroTurn(0.0, configuration, "Straighten out"));
        addComponent(new SidePressBeaconButton(vuforiaConfiguration, configuration, "Press Button"));

        //Cap ball
        //addComponent(new DebugPause(gamepads, telemetry ,configuration, "Press A to continue"));
        addComponent(new DisengageSideRollers(configuration, "Disengage rollers"));
        addComponent(new GiroBananaTurnEncoder(0.0, -135.0, 0.7, 3500, configuration, "Banana to face Cap Ball"));
        addComponent(new GiroDriveEncoder(-135.0, 0.7, 5500, configuration, "Hit and park"));
    }
}
