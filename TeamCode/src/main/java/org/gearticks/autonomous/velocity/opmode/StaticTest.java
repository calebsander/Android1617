package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.velocity.composite.CompleteBallLoad;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.vuforia.VuforiaConfiguration;

@Autonomous
public class StaticTest extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent() {
        final VuforiaConfiguration vuforiaConfiguration = new VuforiaConfiguration();
        final LinearStateMachine sm = new LinearStateMachine();
        //Shoot 2 balls

        sm.addComponent(new CompleteBallLoad(this.configuration, "Load ball"));
        sm.addComponent(new GiroDriveEncoder(0.0, 0.7, 3000, this.configuration, "Drive"));
        sm.addComponent(new GiroDriveEncoder(0.0, -0.7, 3000, this.configuration, "Drive"));
        sm.addComponent(new GiroDriveEncoder(0.0, 0.7, 3000, this.configuration, "Drive"));
        sm.addComponent(new GiroDriveEncoder(0.0, -0.7, 3000, this.configuration, "Drive"));
        sm.addComponent(new GiroTurn(180.0, this.configuration, "Turning"));
        sm.addComponent(new GiroTurn(0.0, this.configuration, "Turning"));
        sm.addComponent(new GiroTurn(180.0, this.configuration, "Turning"));
        sm.addComponent(new GiroTurn(0.0, this.configuration, "Turning"));
        sm.addComponent(new GiroTurn(180.0, this.configuration, "Turning"));


        sm.addComponent(new Stopped(this.configuration));

        return sm;
    }
}
