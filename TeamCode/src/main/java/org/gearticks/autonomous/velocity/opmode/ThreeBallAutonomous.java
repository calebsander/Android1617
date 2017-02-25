package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.AutonomousSideSelector;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.velocity.composite.BlueThreeBallAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.RedSideCornerAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot3Balls;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

import static org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl.NEXT_STATE;

@Autonomous
public class ThreeBallAutonomous extends VelocityBaseOpMode {
    private static final int DISTANCE_FROM_WALL = 9;

    @Override
    protected void loopBeforeStart() {
        super.loopBeforeStart();
        this.configuration.safeShooterStopper(VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_UP);
        this.configuration.clutch.setPosition(VelocityConfiguration.MotorConstants.CLUTCH_V2_CLUTCHED);
        this.configuration.advanceShooterToDownWithEncoder(true);
    }

    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final NetworkedStateMachine sm = new NetworkedStateMachine();

        //int distanceFromWall = 10;

        final AutonomousComponent shoot3Balls = new Shoot3Balls(true, opModeContext, "Shoot 3 balls");
        final AutonomousComponent sideSelector = new AutonomousSideSelector(opModeContext);
        final AutonomousComponent blueSide = new BlueThreeBallAutonomous(opModeContext);
        final AutonomousComponent redSide = new RedSideCornerAutonomous(opModeContext);
        final LinearStateMachine teardown = new LinearStateMachine("Teardown");
        teardown.addComponent(new Stopped(opModeContext));

        //Run
        sm.setInitialComponent(shoot3Balls);
        sm.addConnection(shoot3Balls, NEXT_STATE, sideSelector);
        sm.addConnection(sideSelector, AutonomousSideSelector.BLUE, blueSide);
        sm.addConnection(sideSelector, AutonomousSideSelector.RED, redSide);
        sm.addConnection(blueSide, NEXT_STATE, teardown);
        sm.addConnection(redSide, NEXT_STATE, teardown);




        //sm.addComponent(new Stopped(opModeContext));

        return sm;
    }
    protected boolean isV2() {
        return true;
    }

    protected double targetHeading() {
        return 26.0;
    }
}
