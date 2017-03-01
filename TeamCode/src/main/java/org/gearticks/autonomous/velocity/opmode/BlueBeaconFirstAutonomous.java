package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.ParallelComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot3Balls;
import org.gearticks.autonomous.velocity.components.velocity.composite.SidePressBeaconButton;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.RaiseSideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.RunIntake;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
public class BlueBeaconFirstAutonomous extends InitializedAutonomous {
    private static final int DISTANCE_FROM_WALL = 10;

    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        //Get to far beacon
        sm.addComponent(new RunIntake(1.3, false, opModeContext, "Intake particle"));
        final ParallelComponent driveAndDeployRollers = new ParallelComponent();
        driveAndDeployRollers.addComponent(new GiroBananaTurnEncoder(225.0, 180.0, -0.7, 8000, opModeContext, "Banana turn to 180.0"));
        driveAndDeployRollers.addComponent(new DeploySideRollers(opModeContext, "Deploy side rollers"));
        driveAndDeployRollers.addComponent(new RunIntake(3, false, opModeContext, "Intake particle"));
        sm.addComponent(driveAndDeployRollers);
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.3, 4000, opModeContext, "Get to far beacon"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, 0.15, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new GiroTurn(180.0, 0.1, 5, opModeContext, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(true, opModeContext, "Press Button"));

        //Get to near beacon
        sm.addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 180.0, 1.0, 3000, opModeContext, "Range sensor drive along wall"));
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, 0.3, 3000, opModeContext, "Range sensor drive along wall to line"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.15, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new GiroTurn(180.0, 0.1, 5, opModeContext, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(true, opModeContext, "Press Button"));

        //Shoot
        sm.addComponent(new RaiseSideRollers(opModeContext, "Raise rollers"));
        sm.addComponent(new GiroBananaTurnEncoder(180.0, 187.0, -0.4, 400, opModeContext, "Banana turn to 187"));
        sm.addComponent(new GiroBananaTurnEncoder(187, 270.0, 0.5, 500, 0.1, 0.2, 1.0, opModeContext, "banana turn to shoot"));
        sm.addComponent(new GiroTurn(277.0, opModeContext, "Align to vortex"));
        sm.addComponent(new Shoot3Balls(true, opModeContext, "Shoot"));

        //Cap ball
        sm.addComponent(new GiroTurn(290.0, 0.1, 3, 2.0, opModeContext, "turn to cap ball"));
        sm.addComponent(new GiroBananaTurnEncoder(300.0, 230.0, 1.0, 4500, 0.25, 0.3, 3.0, opModeContext, "Hit cap ball"));


        sm.addComponent(new Stopped(opModeContext));

        return sm;
    }

    protected double targetHeading() {
        return 225.0;
    }
}
