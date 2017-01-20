package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.opmodes.BaseOpMode;
import org.gearticks.vuforia.VuforiaConfiguration;

/**
 * Created by Kevin on 1/17/2017.
 */
@Autonomous
public class TicksToCentimeters extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent() {
        final LinearStateMachine sm = new LinearStateMachine();


        //sm.addComponent(new DebugPause(gamepads, telemetry ,this.configuration, "Press A to continue"));
        sm.addComponent(new GiroDriveEncoder(0.0, 0.4, 2000, this.configuration, "Drive Forward"));
        sm.addComponent(new Wait(3.0, "Wait"));

        sm.addComponent(new Stopped(this.configuration));

        return sm;



    }
}
