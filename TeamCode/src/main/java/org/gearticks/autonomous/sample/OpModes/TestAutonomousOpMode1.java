package org.gearticks.autonomous.sample.OpModes;

import java.util.ArrayList;
import java.util.List;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.opmode.VelocityBaseOpMode;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.GyroTurn;
import org.gearticks.autonomous.velocity.components.GyroDriveEncoder;

/**
 * Test OpMode with AutonomousComponents icw Stage and switch.
 * Using 'regular' AutonomousComponents classes
 */

public class TestAutonomousOpMode1 extends VelocityBaseOpMode {
    private static final double NEW_TARGET = 100.0;

    protected AutonomousComponent getComponent() {
        final List<AutonomousComponent> components = new ArrayList<>();
        components.add(new GyroDriveEncoder(0.0, 1.0, 2000, this.configuration, "drive1"));
        components.add(new GyroTurn(NEW_TARGET, this.configuration, "turn"));
        components.add(new GyroDriveEncoder(NEW_TARGET, 1.0, 2000, this.configuration, "drive2"));
        return new LinearStateMachine(components);
    }
}
