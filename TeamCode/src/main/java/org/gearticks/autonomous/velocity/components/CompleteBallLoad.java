package org.gearticks.autonomous.velocity.components;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import java.util.ArrayList;
import java.util.List;

public class CompleteBallLoad extends LinearStateMachine {
    public CompleteBallLoad(VelocityConfiguration configuration, String id) {
        super(getComponents(configuration, id));
    }

    private static List<AutonomousComponent> getComponents(VelocityConfiguration configuration, String id) {
        final List<AutonomousComponent> components = new ArrayList<>();
        components.add(new LoadBall(configuration, id + "_Load Ball"));
        components.add(new Wait(0.5, id + "_Wait for 0.5 sec"));
        //components.add(new DebugPause(this.gamepads, this.telemetry, configuration, "Wait until X is pressed"));
        components.add(new ResetSnake(configuration, id + "_Reset Snake"));
        return components;
    }
}
