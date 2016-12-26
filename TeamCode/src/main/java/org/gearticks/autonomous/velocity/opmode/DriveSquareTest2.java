package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.opmode.VelocityLinearAutonomousBaseOpMode;
import org.gearticks.autonomous.velocity.components.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.Wait;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Autonomous(name = "DriveSquareTest2")
public class DriveSquareTest2 extends VelocityLinearAutonomousBaseOpMode {

    protected List<AutonomousComponent> createComponents(){
        List<AutonomousComponent> components = new ArrayList<>();
//        components.add(new Wait(2000, null, "Wait for 2 sec"));
        components.add(new GiroDriveEncoder(0.0, 0.5, 2000, this.configuration, "Drive for 2000 ticks heading forward"));
        components.add(new Wait(2000, this.configuration, "Wait for 2 sec"));
        components.add(new GiroDriveEncoder(0.0, 0.5, 2000, this.configuration, "Drive for 2000 ticks heading forward"));
        components.add(new Wait(2000, this.configuration, "Wait for 2 sec"));
        return components;
    }
}
