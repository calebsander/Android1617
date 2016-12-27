package org.gearticks.autonomous.velocity.components;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.autonomous.generic.component.VelocityLinearBaseAutonomousComponent;
import org.gearticks.hardware.configurations.VelocityConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by irene on 12/26/2016.
 */

public class CompleteBallLoad extends VelocityLinearBaseAutonomousComponent {
    public CompleteBallLoad(VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    protected List<AutonomousComponent> createComponents(){
        List<AutonomousComponent> components = new ArrayList<>();
        components.add(new LoadBall(this.getConfiguration(), "Load Ball"));
        components.add(new ResetSnake(this.getConfiguration(), "Reset Snake"));
        return components;
    }
}
