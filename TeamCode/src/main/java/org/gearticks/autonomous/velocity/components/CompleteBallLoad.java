package org.gearticks.autonomous.velocity.components;

import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class CompleteBallLoad extends LinearStateMachine {
    public CompleteBallLoad(VelocityConfiguration configuration, String id) {
        super();
        this.addComponent(new LoadBall(configuration, id + "_Load Ball"));
        this.addComponent(new Wait(0.5, id + "_Wait for 0.5 sec"));
        //this.addComponent(new DebugPause(this.gamepads, this.telemetry, configuration, "Wait until X is pressed"));
        this.addComponent(new ResetSnake(configuration, id + "_Reset Snake"));
    }
}
