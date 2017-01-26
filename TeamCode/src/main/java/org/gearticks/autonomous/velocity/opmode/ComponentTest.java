package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.velocity.composite.SidePressBeaconButton;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.LeftPressBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.RightPressBeaconServo;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponent;

@Autonomous
public class ComponentTest extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent() {
        final VuforiaConfiguration vuforiaConfiguration = new VuforiaConfiguration();
        final LinearStateMachine sm = new LinearStateMachine();

        //sm.addComponent(new RangeSensorReadout(1.0, this.configuration, "Show range sensor val"));

//        sm.addComponent(new GiroTurn(-30.0, this.configuration, "Turn to wall"));
//        sm.addComponent(new GiroDriveToRangeDistance(15, -30.0, 0.15, 4000, this.configuration, "Range sensor drive to wall"));
//        sm.addComponent(new GiroTurn(0.0, this.configuration, "Straighten out"));
        //sm.addComponent(new GiroDriveAlongWallEncoder(10, 0.0, -0.25, 10000, this.configuration, "Range sensor drive along wall"));

//        sm.addComponent(new GiroDriveToLine(0.0, -0.15, 5000, this.configuration, "Range sensor drive along wall"));
//
        sm.addComponent(new SidePressBeaconButton(0.0, vuforiaConfiguration, this.configuration, "Press Button"));

//        sm.addComponent(new GiroBananaTurnEncoder(0.0, 180.0, 0.15, 3000, this.configuration, "Banana Turn"));

        //sm.addComponent(new GiroTurn(400.0, this.configuration, "Straighten out"));

        //sm.addComponent(new GiroDriveToLine(0.0, -0.15, 5000, this.configuration, "drive to white line"));


        return sm;
    }
}
