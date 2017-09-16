package org.gearticks.components.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.component.ParallelComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.hardwareagnostic.component.Stopped;
import org.gearticks.components.hardwareagnostic.component.Wait;
import org.gearticks.components.velocity.component.composite.BlueSideAutonomous;
import org.gearticks.components.velocity.component.composite.Shoot2Balls;
import org.gearticks.components.velocity.component.servo.DeploySideRollers;
import org.gearticks.components.velocity.opmode.generic.InitializedAutonomous;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
public class BlueParticleFirstAutonomous extends InitializedAutonomous {
    protected OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        //Initialization components
        final ParallelComponent shootAndDeployRollers = new ParallelComponent("Shoot and rollers");
        shootAndDeployRollers.addComponent(new DeploySideRollers(opModeContext, "Deploy rollers"));
        shootAndDeployRollers.addComponent(new Shoot2Balls(true, opModeContext, "Shoot 2 balls"));
        sm.addComponent(shootAndDeployRollers);

        // Blue side driving components
        sm.addComponent(new Wait(0.5, "Wait for top latch to get out of way"));
        sm.addComponent(new BlueSideAutonomous(opModeContext));

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
