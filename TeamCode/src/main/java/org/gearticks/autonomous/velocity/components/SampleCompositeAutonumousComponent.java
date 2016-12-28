package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.VelocityLinearBaseAutonomousComponent;
import org.gearticks.hardware.configurations.VelocityConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample to create a composite autonomous component that will consist of a linear state-machine of 2 or more AutonomousComponents
 */
public class SampleCompositeAutonumousComponent extends VelocityLinearBaseAutonomousComponent {
    public SampleCompositeAutonumousComponent(@NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @NonNull
    protected List<AutonomousComponent> createComponents(){
        List<AutonomousComponent> components = new ArrayList<>();
        components.add(new Wait(500, this.getConfiguration(), "Wait for 0.5 sec"));
        return components;
    }
}
