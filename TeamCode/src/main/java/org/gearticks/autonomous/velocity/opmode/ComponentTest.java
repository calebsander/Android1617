package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
import org.gearticks.autonomous.velocity.components.velocity.composite.SidePressBeaconButton;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.LeftPressBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.ResetSnake;
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

        //sm.addComponent(new DebugPause(gamepads, telemetry, this.configuration, "Press A to continue"));

//
//        sm.addComponent(new ResetSnake(this.configuration, "Reset Snake"));
//
//        sm.addComponent(new Shoot2Balls(gamepads, telemetry, this.configuration, "Shoot 2 balls"));

//        sm.addComponent(new GiroDriveToLine(0, 0.25, 4000, configuration, "Adjust to white line"));

        sm.addComponent(new SidePressBeaconButton(180.0, vuforiaConfiguration, this.configuration, "Press Button"));


        return sm;
    }
}
