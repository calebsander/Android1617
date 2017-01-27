package org.gearticks.autonomous.velocity.opmode.sample;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.AutonomousSideSelecter;
import org.gearticks.autonomous.velocity.components.experimental.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.composite.SidePressBeaconButton;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.LoadBall;
import org.gearticks.autonomous.velocity.components.velocity.single.MoveShooterDown;
import org.gearticks.autonomous.velocity.components.velocity.single.ResetSnake;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide;
import org.gearticks.autonomous.velocity.components.velocity.single.ShootBall;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.vuforia.VuforiaConfiguration;

import static org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl.NEXT_STATE;

@Autonomous
public class SideAutonomousRB extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent() {
        final VuforiaConfiguration vuforiaConfiguration = new VuforiaConfiguration();
        final NetworkedStateMachine sm = new NetworkedStateMachine();



        int distanceFromWall = 10;

        // basic components
        final AutonomousComponent stop = new Stopped(this.configuration);
        final AutonomousComponent sideSelecter = new AutonomousSideSelecter(this.configuration);
        final AutonomousComponent disengageBeaconServo = new DisengageBeaconServo(this.configuration, "Disengage beacon button");
        final AutonomousComponent wait = new Wait(0.3, "Wait for 0.5 sec");
        final AutonomousComponent debugPause = new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue");
        final AutonomousComponent pressBeacon = new SidePressBeaconButton(180.0, vuforiaConfiguration, this.configuration, "Press Button");


        //shooter components
        final AutonomousComponent moveShooterDown = new MoveShooterDown(this.configuration, "MoveShooterDown");
        final AutonomousComponent shootBall = new ShootBall(this.configuration, "Shoot ball");
        final AutonomousComponent loadBall = new LoadBall(this.configuration, "Load 2nd ball");
        final AutonomousComponent resetSnake = new ResetSnake(this.configuration, "Reset Snake");

        // Blue side driving components
        final AutonomousComponent firstBananaTurn = new GiroBananaTurnEncoder(0.0, 20.0, 0.25, 1000, this.configuration, "BTR 20 - 1000");
        final AutonomousComponent secondBananaTurn = new GiroBananaTurnEncoder(20.0, 90.0, 0.5, 4000, this.configuration, "BTR 90 - 4000");
        final AutonomousComponent straightenOutToWall = new GiroTurn(180.0, this.configuration, "Straighten out");
        final AutonomousComponent driveForwardAlongWallToLine = new GiroDriveAlongWallLine(distanceFromWall, 180.0, -0.15, 6000, this.configuration, "Range sensor drive along wall");
        final AutonomousComponent adjustToWhiteLine1 = new GiroDriveAlongWallLine(distanceFromWall, 180, 0.05, 100, this.configuration, "Adjust to white line");
        final AutonomousComponent driveBackAlongWallToEncoder = new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, 0.15, 1000, this.configuration, "Range sensor drive along wall");
        final AutonomousComponent driveBackAlongWallToLine = new GiroDriveAlongWallLine(distanceFromWall, 180.0, 0.15, 7000, this.configuration, "Range sensor drive along wall to line");
        final AutonomousComponent adjustToWhiteLine2 = new GiroDriveAlongWallLine(distanceFromWall, 180, -0.05, 100, this.configuration, "Adjust to white line");

        //Red side driving components

        //Start
        sm.setInitialComponent(disengageBeaconServo);
        sm.addConnection(disengageBeaconServo, NEXT_STATE, moveShooterDown);

        //Shoot ball
        sm.addConnection(moveShooterDown, NEXT_STATE, wait);
        sm.addConnection(wait, NEXT_STATE, shootBall);
        sm.addConnection(shootBall, NEXT_STATE, moveShooterDown);
        sm.addConnection(moveShooterDown, NEXT_STATE, loadBall);
        sm.addConnection(loadBall, NEXT_STATE, resetSnake);
        sm.addConnection(resetSnake, NEXT_STATE, shootBall);
        sm.addConnection(shootBall, NEXT_STATE, moveShooterDown);
        sm.addConnection(moveShooterDown, NEXT_STATE, sideSelecter);

        //Blue Side
        sm.addConnection(sideSelecter, AutonomousSideSelecter.BLUE, firstBananaTurn);
        sm.addConnection(firstBananaTurn, NEXT_STATE, wait);
        sm.addConnection(wait, NEXT_STATE, secondBananaTurn);
        sm.addConnection(secondBananaTurn, NEXT_STATE, straightenOutToWall);
        sm.addConnection(straightenOutToWall, NEXT_STATE, driveForwardAlongWallToLine);
        sm.addConnection(driveForwardAlongWallToLine, NEXT_STATE, adjustToWhiteLine1);
        sm.addConnection(adjustToWhiteLine1, NEXT_STATE, pressBeacon);
        sm.addConnection(pressBeacon, NEXT_STATE, wait);
        sm.addConnection(wait, NEXT_STATE, driveBackAlongWallToEncoder);
        sm.addConnection(driveBackAlongWallToEncoder, NEXT_STATE, driveBackAlongWallToLine);
        sm.addConnection(driveBackAlongWallToLine, NEXT_STATE, adjustToWhiteLine2);
        sm.addConnection(adjustToWhiteLine2, NEXT_STATE, pressBeacon);
        sm.addConnection(pressBeacon, NEXT_STATE, stop);


        //Red side
        sm.addConnection(sideSelecter, AutonomousSideSelecter.RED, stop);
        //sm.addConnection(moveShooterDown, NEXT_STATE, sideSelecter);


        //End
        sm.addExitConnection(stop, NEXT_STATE);

        return sm;
    }
}
