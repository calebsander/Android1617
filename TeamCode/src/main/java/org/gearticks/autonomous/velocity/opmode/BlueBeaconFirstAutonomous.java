package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.AutonomousSideSelector;
import org.gearticks.autonomous.velocity.components.generic.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.velocity.composite.BlueThreeBallAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.RedSideCornerAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot3Balls;
import org.gearticks.autonomous.velocity.components.velocity.composite.SidePressBeaconButton;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.RaiseSideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.RunIntake;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

import static org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl.NEXT_STATE;

@Autonomous
public class BlueBeaconFirstAutonomous extends VelocityBaseOpMode {
    private static final int DISTANCE_FROM_WALL = 10;

    @Override
    protected void loopBeforeStart() {
        super.loopBeforeStart();
        this.configuration.safeShooterStopper(VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_UP);
        this.configuration.advanceShooterToDown();
        this.configuration.beaconPresserFrontIn();
        this.configuration.beaconPresserBackIn();
    }

    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        sm.addComponent(new RunIntake(1.3, true, opModeContext, "Intake particle"));

        //Get to far beacon
        sm.addComponent(new DeploySideRollers(opModeContext, "Deploy side rollers")); //Todo: fix deployment
        sm.addComponent(new GiroBananaTurnEncoder(225.0, 180.0, -0.7, 8000, opModeContext, "Banana turn to 180.0"));
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.3, 4000, opModeContext, "Get to far beacon"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, 0.15, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new GiroTurn(180.0, 0.1, 5, opModeContext, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

        //Get to near beacon
        sm.addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 180.0, 1.0, 3000, opModeContext, "Range sensor drive along wall"));
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, 0.3, 3000, opModeContext, "Range sensor drive along wall to line"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.15, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new GiroTurn(180.0, 0.1, 5, opModeContext, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

        //Shoot
        sm.addComponent(new RaiseSideRollers(opModeContext, "Raise rollers"));
        sm.addComponent(new GiroBananaTurnEncoder(180.0, 187.0, -0.4, 400, opModeContext, "Banana turn to 187"));
        sm.addComponent(new GiroBananaTurnEncoder(187, 270.0, 0.5, 500, 0.1, 0.2, 1.0, opModeContext, "banana turn to shoot"));
        sm.addComponent(new Shoot3Balls(true, opModeContext, "Shoot"));
        //Cap ball
        sm.addComponent(new GiroTurn(290.0, 0.1, 3, 2.0, opModeContext, "turn to cap ball"));
        sm.addComponent(new GiroBananaTurnEncoder(300.0, 230.0, 1.0, 5000, 0.25, 0.3, 3.0, opModeContext, "Hit cap ball"));


        sm.addComponent(new Stopped(opModeContext));

        return sm;
    }
    protected boolean isV2() {
        return true;
    }

    protected double targetHeading() {
        return 26.0;
    }
}