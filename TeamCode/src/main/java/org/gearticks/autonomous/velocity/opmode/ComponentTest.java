package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveToRangeDistance;
import org.gearticks.autonomous.velocity.components.experimental.RangeSensorReadout;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.composite.CompleteBallLoad;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.velocity.composite.NonLinearPressBeaconButton;
import org.gearticks.autonomous.velocity.components.velocity.composite.SidePressBeaconButton;
import org.gearticks.autonomous.velocity.components.velocity.single.FacePicture;
import org.gearticks.autonomous.velocity.components.velocity.single.LoadBall;
import org.gearticks.autonomous.velocity.components.velocity.single.MoveShooterDown;
import org.gearticks.autonomous.velocity.components.velocity.single.ResetSnake;
import org.gearticks.autonomous.velocity.components.velocity.single.ShootBall;
import org.gearticks.autonomous.velocity.components.velocity.single.VuforiaIn;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponent;

@Autonomous
public class ComponentTest extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent() {
        final VuforiaConfiguration vuforiaConfiguration = new VuforiaConfiguration();
        final LinearStateMachine sm = new LinearStateMachine();

//        sm.addComponent(new RangeSensorReadout(1.0, this.configuration, "Show range sensor val"));
//
//        sm.addComponent(new GiroTurn(-30.0, this.configuration, "Turn to wall"));
//        sm.addComponent(new GiroDriveToRangeDistance(50, -30.0, 0.15, 2000, this.configuration, "Range sensor drive to wall"));
//        sm.addComponent(new GiroTurn(0.0, this.configuration, "Straighten out"));
//        sm.addComponent(new GiroDriveAlongWallEncoder(50, 0.0, 0.15, 5000, this.configuration, "Range sensor drive along wall"));

        //sm.addComponent(new GiroDriveAlongWallLine(20, 0.0, 0.15, 5000, this.configuration, "Range sensor drive along wall"));
        sm.addComponent(new SidePressBeaconButton(0.0, vuforiaConfiguration,this.configuration, "Press Beacon Button"));

        sm.addComponent(new Stopped(this.configuration));

        return sm;
    }
}
