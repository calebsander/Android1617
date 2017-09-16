package org.gearticks.components.velocity.opmode.deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.hardwareagnostic.component.GiroDriveEncoder;
import org.gearticks.components.velocity.component.drive.GiroDriveToLine;
import org.gearticks.components.hardwareagnostic.component.GiroTurn;
import org.gearticks.components.hardwareagnostic.component.Stopped;
import org.gearticks.components.hardwareagnostic.component.Wait;
import org.gearticks.components.velocity.component.composite.NonLinearPressBeaconButton;
import org.gearticks.components.velocity.component.vuforia.FacePicture;
import org.gearticks.components.velocity.component.servo.LoadBall;
import org.gearticks.components.velocity.component.motor.MoveShooterDown;
import org.gearticks.components.velocity.component.servo.ResetSnake;
import org.gearticks.components.velocity.component.motor.ShootBall;
import org.gearticks.components.velocity.component.vuforia.VuforiaIn;
import org.gearticks.components.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
@Disabled
public class CantonBeaconAutonomousComp extends VelocityBaseOpMode {
    protected OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();
        //Shoot 2 balls
        sm.addComponent(new MoveShooterDown(opModeContext, "MoveShooterDown"));
        sm.addComponent(new Wait(0.3, "Wait for 0.5 sec"));
        sm.addComponent(new ShootBall(opModeContext, "Shoot 1st ball"));
        sm.addComponent(new MoveShooterDown(opModeContext, "Move Shooter Down"));
        sm.addComponent(new LoadBall(opModeContext, "Load 2nd ball"));
        sm.addComponent(new ResetSnake(true, opModeContext, "Reset Snake"));
        sm.addComponent(new ShootBall(opModeContext, "Shoot 2nd ball"));

        //Drive to beacon
        sm.addComponent(new GiroDriveEncoder(0.0, 0.7, 1700, opModeContext, "Drive off wall for 1700 ticks"));
        sm.addComponent(new Wait(0.3, "Wait before first turn"));
        sm.addComponent(new GiroTurn(40.0, opModeContext, "Turn to far target"));
        sm.addComponent(new GiroDriveEncoder(40.0, 0.7, 2900, opModeContext, "Drive in front of near target"));
        sm.addComponent(new Wait(0.3, "Wait before second turn"));
        sm.addComponent(new GiroTurn(90.0, opModeContext, "Face near target"));

        //Go to 1st beacon
        sm.addComponent(new VuforiaIn(500F, true, opModeContext, "Drive to near target"));
        sm.addComponent(new FacePicture(true, opModeContext, "Face near target"));
        sm.addComponent(new VuforiaIn(175F, true, opModeContext, "Drive closer to near target"));

        // Press correct button
        sm.addComponent(new NonLinearPressBeaconButton(opModeContext, "Press beacon button"));
        sm.addComponent(new GiroDriveEncoder(90.0, -0.5, 300, opModeContext, "Back up near"));

        // Drive to second beacon
        sm.addComponent(new GiroTurn(0.0, opModeContext, "Turn to far target"));
        sm.addComponent(new GiroDriveEncoder(0.0, 0.5, 500, opModeContext, "Drive off 1st white line"));
        sm.addComponent(new GiroDriveToLine(0.0, 0.20, 5000, opModeContext, "Drive to 2nd white line"));
        sm.addComponent(new GiroTurn(90.0, opModeContext, "Turn to 2nd beacon"));

        // Press second beacon
        sm.addComponent(new GiroDriveEncoder(90.0, -0.25, 500, opModeContext, "Back off target"));
        sm.addComponent(new VuforiaIn(500F, false, opModeContext, "Drive to far target"));
        sm.addComponent(new FacePicture(false, opModeContext, "Face far target"));
        sm.addComponent(new VuforiaIn(175F, false, opModeContext, "Drive to far target"));
        sm.addComponent(new NonLinearPressBeaconButton(opModeContext, "Press beacon button"));
        sm.addComponent(new GiroDriveEncoder(90.0, -0.5, 300, opModeContext, "Back up far"));


        sm.addComponent(new Stopped(opModeContext));

        return sm;
    }
    protected boolean isV2() {
        return false;
    }
}
