package org.gearticks.components.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.hardwareagnostic.component.Stopped;
import org.gearticks.components.velocity.component.composite.RedSideAutonomous;
import org.gearticks.components.velocity.component.composite.Shoot2Balls;
import org.gearticks.components.velocity.component.servo.DisengageBeaconServo;
import org.gearticks.components.velocity.opmode.generic.InitializedAutonomous;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
public class RedParticleFirstAutonomous extends InitializedAutonomous {
    private static final int DISTANCE_FROM_WALL = 9;

    protected OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        //Initialization components
        sm.addComponent(new DisengageBeaconServo(opModeContext, "Disengage beacon button"));
        //shooter components
        sm.addComponent(new Shoot2Balls(true, opModeContext, "Shoot 2 balls"));

        //Red side driving components
        sm.addComponent(new RedSideAutonomous(DISTANCE_FROM_WALL, opModeContext));

        //End component
        final LinearStateMachine teardown = new LinearStateMachine("Teardown");
        teardown.addComponent(new Stopped(opModeContext));
        sm.addComponent(teardown);

        return sm;
    }
    protected boolean isV2() {
        return true;
    }
}
