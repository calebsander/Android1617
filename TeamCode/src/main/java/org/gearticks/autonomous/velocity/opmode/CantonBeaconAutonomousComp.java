package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveToRangeDistance;
import org.gearticks.autonomous.velocity.components.experimental.RangeSensorReadout;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.composite.CompleteBallLoad;
import org.gearticks.autonomous.velocity.components.velocity.composite.NonLinearPressBeaconButton;
import org.gearticks.autonomous.velocity.components.velocity.single.FacePicture;
import org.gearticks.autonomous.velocity.components.velocity.single.LoadBall;
import org.gearticks.autonomous.velocity.components.velocity.single.MoveShooterDown;
import org.gearticks.autonomous.velocity.components.velocity.single.ResetSnake;
import org.gearticks.autonomous.velocity.components.velocity.single.ShootBall;
import org.gearticks.autonomous.velocity.components.velocity.single.VuforiaIn;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.vuforia.VuforiaConfiguration;

@Autonomous
public class CantonBeaconAutonomousComp extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent() {
        final VuforiaConfiguration vuforiaConfiguration = new VuforiaConfiguration();
        final LinearStateMachine sm = new LinearStateMachine();
        //Shoot 2 balls
        sm.addComponent(new MoveShooterDown(this.configuration, "MoveShooterDown"));
        sm.addComponent(new Wait(0.3, "Wait for 0.5 sec"));
        sm.addComponent(new ShootBall(this.configuration, "Shoot 1st ball"));
        sm.addComponent(new MoveShooterDown(this.configuration, "Move Shooter Down"));
        sm.addComponent(new LoadBall(this.configuration, "Load 2nd ball"));
        sm.addComponent(new ResetSnake(this.configuration, "Reset Snake"));
        sm.addComponent(new ShootBall(this.configuration, "Shoot 2nd ball"));

        //Drive to beacon
        sm.addComponent(new GiroDriveEncoder(0.0, 0.7, 1700, this.configuration, "Drive off wall for 1700 ticks"));
        sm.addComponent(new Wait(0.3, "Wait before first turn"));
        sm.addComponent(new GiroTurn(40.0, this.configuration, "Turn to far target"));
        sm.addComponent(new GiroDriveEncoder(40.0, 0.7, 2900, this.configuration, "Drive in front of near target"));
        sm.addComponent(new Wait(0.3, "Wait before second turn"));
        sm.addComponent(new GiroTurn(90.0, this.configuration, "Face near target"));

        //Go to 1st beacon
        sm.addComponent(new VuforiaIn(500F, true, vuforiaConfiguration, this.configuration, "Drive to near target"));
        sm.addComponent(new FacePicture(true, vuforiaConfiguration, this.configuration, "Face near target"));
        sm.addComponent(new VuforiaIn(175F, true, vuforiaConfiguration, this.configuration, "Drive closer to near target"));

        // Press correct button
        sm.addComponent(new NonLinearPressBeaconButton(vuforiaConfiguration, this.configuration, "Press beacon button"));
        sm.addComponent(new GiroDriveEncoder(90.0, -0.5, 300, this.configuration, "Back up near"));

        // Drive to second beacon
        sm.addComponent(new GiroTurn(0.0, this.configuration, "Turn to far target"));
        sm.addComponent(new GiroDriveEncoder(0.0, 0.5, 500, this.configuration, "Drive off 1st white line"));
        sm.addComponent(new GiroDriveToLine(0.0, 0.20, 5000, this.configuration, "Drive to 2nd white line"));
        sm.addComponent(new GiroTurn(90.0, this.configuration, "Turn to 2nd beacon"));

        // Press second beacon
        sm.addComponent(new GiroDriveEncoder(90.0, -0.25, 500, this.configuration, "Back off target"));
        sm.addComponent(new VuforiaIn(500F, false, vuforiaConfiguration, this.configuration, "Drive to far target"));
        sm.addComponent(new FacePicture(false, vuforiaConfiguration, this.configuration, "Face far target"));
        sm.addComponent(new VuforiaIn(175F, false, vuforiaConfiguration, this.configuration, "Drive to far target"));
        sm.addComponent(new NonLinearPressBeaconButton(vuforiaConfiguration, this.configuration, "Press beacon button"));
        sm.addComponent(new GiroDriveEncoder(90.0, -0.5, 300, this.configuration, "Back up far"));


        sm.addComponent(new Stopped(this.configuration));

        return sm;
    }
}
