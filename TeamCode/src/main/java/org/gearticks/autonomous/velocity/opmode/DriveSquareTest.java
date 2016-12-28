package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.opmode.VelocityBaseOpMode;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.GyroDriveEncoder;
import org.gearticks.autonomous.velocity.components.GyroTurn;
import org.gearticks.autonomous.velocity.components.Wait;

import java.util.ArrayList;
import java.util.List;

@Autonomous
@Disabled
public class DriveSquareTest extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent() {
        final List<AutonomousComponent> components = new ArrayList<>();
        for (int side = 0; side < 4; side++) {
            components.add(new GyroDriveEncoder(90.0 * side, 0.2, 4000, this.configuration, "Drive for 2000 ticks heading forward"));
            components.add(new Wait(0.5, "Wait for 0.5 sec"));
            components.add(new GyroTurn(90.0 * (side + 1), this.configuration, "Turn right 90"));
        }
        return new LinearStateMachine(components);
    }
}
