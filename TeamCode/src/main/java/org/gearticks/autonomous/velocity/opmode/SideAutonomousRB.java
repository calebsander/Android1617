package org.gearticks.autonomous.velocity.opmode;

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
import org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.RedSideAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
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
        final AutonomousComponent shoot2Balls = new Shoot2Balls(this.configuration, "Shoot 2 balls");
//        final AutonomousComponent moveShooterDown1 = new MoveShooterDown(this.configuration, "MoveShooterDown");
//        final AutonomousComponent wait1 = new Wait(0.3, "Wait for 0.5 sec");
//        final AutonomousComponent shootBall1 = new ShootBall(this.configuration, "Shoot ball");
//        final AutonomousComponent moveShooterDown2 = new MoveShooterDown(this.configuration, "MoveShooterDown");
//        final AutonomousComponent loadBall = new LoadBall(this.configuration, "Load 2nd ball");
//        final AutonomousComponent resetSnake = new ResetSnake(this.configuration, "Reset Snake");
//        final AutonomousComponent shootBall2 = new ShootBall(this.configuration, "Shoot ball");
//        final AutonomousComponent moveShooterDown3 = new MoveShooterDown(this.configuration, "MoveShooterDown");


        // Blue side driving components
        final AutonomousComponent blueSide = new BlueSideAutonomous(distanceFromWall, gamepads, telemetry, vuforiaConfiguration, this.configuration);

//        final AutonomousComponent firstBananaTurn = new GiroBananaTurnEncoder(0.0, 20.0, 0.25, 1000, this.configuration, "BTR 20 - 1000");
//        final AutonomousComponent waitB1 = new Wait(0.3, "Wait for 0.5 sec");
//        final AutonomousComponent secondBananaTurn = new GiroBananaTurnEncoder(20.0, 90.0, 0.5, 4000, this.configuration, "BTR 90 - 4000");
//        final AutonomousComponent straightenOutToWall = new GiroTurn(180.0, this.configuration, "Straighten out");
//        final AutonomousComponent driveForwardAlongWallToLine = new GiroDriveAlongWallLine(distanceFromWall, 180.0, -0.15, 6000, this.configuration, "Range sensor drive along wall");
//        final AutonomousComponent adjustToWhiteLine1 = new GiroDriveAlongWallLine(distanceFromWall, 180, 0.05, 100, this.configuration, "Adjust to white line");
//        final AutonomousComponent pressBeaconB1 = new SidePressBeaconButton(180.0, vuforiaConfiguration, this.configuration, "Press Button");
//        final AutonomousComponent waitB2 = new Wait(0.3, "Wait for 0.5 sec");
//        final AutonomousComponent driveBackAlongWallToEncoder = new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, 0.15, 1000, this.configuration, "Range sensor drive along wall");
//        final AutonomousComponent driveBackAlongWallToLine = new GiroDriveAlongWallLine(distanceFromWall, 180.0, 0.15, 7000, this.configuration, "Range sensor drive along wall to line");
//        final AutonomousComponent adjustToWhiteLine2 = new GiroDriveAlongWallLine(distanceFromWall, 180, -0.05, 100, this.configuration, "Adjust to white line");
//        final AutonomousComponent pressBeaconB2 = new SidePressBeaconButton(180.0, vuforiaConfiguration, this.configuration, "Press Button");

        //Red side driving components
        final AutonomousComponent redSide = new RedSideAutonomous(distanceFromWall, gamepads, telemetry, vuforiaConfiguration, this.configuration);


        //Shoot balls
        sm.setInitialComponent(disengageBeaconServo);
        sm.addConnection(disengageBeaconServo, NEXT_STATE, sideSelecter);
        //sm.addConnection(shoot2Balls, NEXT_STATE, sideSelecter);

//        sm.addConnection(moveShooterDown1, NEXT_STATE, wait1);
//        sm.addConnection(wait1, NEXT_STATE, shootBall1);
//        sm.addConnection(shootBall1, NEXT_STATE, moveShooterDown2);
//        sm.addConnection(moveShooterDown2, NEXT_STATE, loadBall);
//        sm.addConnection(loadBall, NEXT_STATE, resetSnake);
//        sm.addConnection(resetSnake, NEXT_STATE, shootBall2);
//        sm.addConnection(shootBall2, NEXT_STATE, moveShooterDown3);

        //Blue Side
        sm.addConnection(sideSelecter, AutonomousSideSelecter.BLUE, blueSide);
        sm.addConnection(blueSide, NEXT_STATE, stop);

//        sm.addConnection(firstBananaTurn, NEXT_STATE, waitB1);
//        sm.addConnection(waitB1, NEXT_STATE, secondBananaTurn);
//        sm.addConnection(secondBananaTurn, NEXT_STATE, straightenOutToWall);
//        sm.addConnection(straightenOutToWall, NEXT_STATE, driveForwardAlongWallToLine);
//        sm.addConnection(driveForwardAlongWallToLine, NEXT_STATE, adjustToWhiteLine1);
//        sm.addConnection(adjustToWhiteLine1, NEXT_STATE, pressBeaconB1);
//        sm.addConnection(pressBeaconB1, NEXT_STATE, waitB2);
//        sm.addConnection(waitB2, NEXT_STATE, driveBackAlongWallToEncoder);
//        sm.addConnection(driveBackAlongWallToEncoder, NEXT_STATE, driveBackAlongWallToLine);
//        sm.addConnection(driveBackAlongWallToLine, NEXT_STATE, adjustToWhiteLine2);
//        sm.addConnection(adjustToWhiteLine2, NEXT_STATE, pressBeaconB2);


        //Red side
        sm.addConnection(sideSelecter, AutonomousSideSelecter.RED, redSide);
        sm.addConnection(redSide, AutonomousSideSelecter.RED, stop);


        //End
        sm.addExitConnection(stop, NEXT_STATE);

        return sm;
    }
}
