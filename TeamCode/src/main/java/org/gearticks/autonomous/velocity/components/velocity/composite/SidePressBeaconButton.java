package org.gearticks.autonomous.velocity.components.velocity.composite;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.EngageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration;

/**
 * Sample to create a composite autonomous component that will consist of a linear state-machine of 2 or more AutonomousComponents
 */
public class SidePressBeaconButton extends NetworkedStateMachine {

    public SidePressBeaconButton(double targetHeading, @NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration, String id) {
        super();
        final AutonomousComponent selectSide = new SelectBeaconSide(vuforiaConfiguration, configuration, "Select beacon side");
        final AutonomousComponent driveForward = new GiroDriveEncoder(targetHeading, 0.15, 300, configuration, "To right button");
        final AutonomousComponent driveBackward = new GiroDriveEncoder(targetHeading, -0.15, 300, configuration, "To left button");
        final AutonomousComponent engageBeaconPusherL = new EngageBeaconServo(configuration, "Release beacon presser");
        final AutonomousComponent engageBeaconPusherR = new EngageBeaconServo(configuration, "Release beacon presser");
        final AutonomousComponent disengageBeaconPusher = new DisengageBeaconServo(configuration, "Close beacon presser");

        this.setInitialComponent(selectSide);
        this.addConnection(selectSide, SelectBeaconSide.LEFT_TRANSITION, engageBeaconPusherL);
        this.addConnection(selectSide, SelectBeaconSide.RIGHT_TRANSITION, engageBeaconPusherR);
        this.addExitConnection(selectSide, SelectBeaconSide.UNKNOWN_TRANSITION);
        this.addConnection(engageBeaconPusherL, NEXT_STATE, driveBackward);
        this.addConnection(engageBeaconPusherR, NEXT_STATE, driveForward);
        this.addConnection(driveBackward, NEXT_STATE, disengageBeaconPusher);
        this.addConnection(driveForward, NEXT_STATE, disengageBeaconPusher);
        this.addExitConnection(disengageBeaconPusher, NEXT_STATE);
    }
}
