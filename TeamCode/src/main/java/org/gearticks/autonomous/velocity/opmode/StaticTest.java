package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.velocity.composite.SidePressBeaconButton;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.vuforia.VuforiaConfiguration;

@Autonomous
@Disabled
public class StaticTest extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent() {
        final VuforiaConfiguration vuforiaConfiguration = new VuforiaConfiguration();
        final LinearStateMachine sm = new LinearStateMachine();


        sm.addComponent(new GiroDriveEncoder(0.0, 0.7, 1000, this.configuration, "Drive Forward"));
        sm.addComponent(new GiroTurn(180.0, this.configuration, "Turn"));
        sm.addComponent(new GiroDriveEncoder(180.0, 0.7, 1000, this.configuration, "Drive Forward"));
        sm.addComponent(new GiroTurn(0.0, this.configuration, "Turn"));
        sm.addComponent(new GiroDriveEncoder(0.0, 0.7, 1000, this.configuration, "Drive Forward"));
        sm.addComponent(new GiroTurn(180.0, this.configuration, "Turn"));
        sm.addComponent(new GiroDriveEncoder(180.0, 0.7, 1000, this.configuration, "Drive Forward"));
        sm.addComponent(new GiroTurn(0.0, this.configuration, "Turn"));
        sm.addComponent(new GiroDriveEncoder(0.0, 0.7, 1000, this.configuration, "Drive Forward"));
        sm.addComponent(new GiroTurn(180.0, this.configuration, "Turn"));
        sm.addComponent(new GiroDriveEncoder(180.0, 0.7, 1000, this.configuration, "Drive Forward"));
        sm.addComponent(new GiroTurn(0.0, this.configuration, "Turn"));
        sm.addComponent(new GiroDriveEncoder(0.0, 0.7, 1000, this.configuration, "Drive Forward"));
        sm.addComponent(new GiroTurn(180.0, this.configuration, "Turn"));
        sm.addComponent(new GiroDriveEncoder(180.0, 0.7, 1000, this.configuration, "Drive Forward"));
        sm.addComponent(new GiroTurn(0.0, this.configuration, "Turn"));
        sm.addComponent(new GiroDriveEncoder(0.0, 0.7, 1000, this.configuration, "Drive Forward"));
        sm.addComponent(new GiroTurn(180.0, this.configuration, "Turn"));
        sm.addComponent(new GiroDriveEncoder(180.0, 0.7, 1000, this.configuration, "Drive Forward"));
        sm.addComponent(new GiroTurn(0.0, this.configuration, "Turn"));


        sm.addComponent(new Stopped(this.configuration));

        return sm;
    }
    protected boolean isV2() {
        return false;
    }
}
