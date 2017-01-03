package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration;

/**
 * Sample to create a composite autonomous component that will consist of a linear state-machine of 2 or more AutonomousComponents
 */
public class SidePressBeaconButton extends NetworkedStateMachine {

    public SidePressBeaconButton(@NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration, String id) {
        super();
        final AutonomousComponent selectSide = new SelectBeaconSide(vuforiaConfiguration, configuration, "Select beacon side");
        final AutonomousComponent driveForward = new GiroDriveEncoder(0.0, 0.25, 50, configuration, "To left button");
        final AutonomousComponent driveBackward = new GiroDriveEncoder(0.0, -0.25, 50, configuration, "To right button");
        final AutonomousComponent engageBeaconPusher = new EngageBeaconServo(configuration, "Release beacon presser");
        final AutonomousComponent pushButton = new GiroDriveEncoder(0.0, 0.05, 10, configuration, "Pressing button");
        final AutonomousComponent disengageBeaconPusher = new DisengageBeaconServo(configuration, "Close beacon presser");



        this.setInitialComponent(selectSide);
        this.addConnection(selectSide, SelectBeaconSide.LEFT_TRANSITION, driveForward);
        this.addConnection(selectSide, SelectBeaconSide.RIGHT_TRANSITION, driveBackward);
        this.addExitConnection(selectSide, SelectBeaconSide.UNKNOWN_TRANSITION);
        this.addConnection(driveForward, NEXT_STATE, engageBeaconPusher);
        this.addConnection(driveBackward, NEXT_STATE, engageBeaconPusher);
        this.addConnection(engageBeaconPusher, NEXT_STATE, pushButton);
        this.addConnection(pushButton, NEXT_STATE, disengageBeaconPusher);
        this.addExitConnection(disengageBeaconPusher, NEXT_STATE);
    }
}
