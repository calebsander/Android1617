package org.gearticks.autonomous.velocity.components;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.autonomous.generic.component.VelocityLinearBaseAutonomousComponent;
import org.gearticks.hardware.configurations.VelocityConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by irene on 12/26/2016.
 * Composite component
 *      *does not complete load ball or reset snake but does wait*
 */

public class CompleteBallLoad extends VelocityLinearBaseAutonomousComponent {
    public CompleteBallLoad(VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    protected List<AutonomousComponent> createComponents(){
        List<AutonomousComponent> components = new ArrayList<>();
        components.add(new LoadBall(this.getConfiguration(), this.getId() + "_Load Ball"));
        components.add(new Wait(500, this.getConfiguration(), this.getId() + "_Wait for 0.5 sec"));
        //components.add(new DebugPause(this.gamepads, this.telemetry, this.configuration, "Wait until X is pressed"));

        components.add(new ResetSnake(this.getConfiguration(), this.getId() + "_Reset Snake"));
        return components;
    }
}
