package org.gearticks.autonomous.velocity.opmode;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.opmode.VelocityLinearAutonomousBaseOpMode;
import org.gearticks.autonomous.velocity.components.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.GiroTurn;
import org.gearticks.autonomous.velocity.components.Wait;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Autonomous(name = "DriveSquareTest2")
@Disabled

public class DriveSquareTest2 extends VelocityLinearAutonomousBaseOpMode {

    @NonNull
    protected List<AutonomousComponent> createComponents(){
        List<AutonomousComponent> components = new ArrayList<>();
        components.add(new GiroDriveEncoder(0.0, 0.2, 4000, this.configuration, "Drive for 2000 ticks heading forward"));
        components.add(new Wait(500, this.configuration, "Wait for 0.5 sec"));
        components.add(new GiroTurn(90.0, this.configuration, "Turn Right 90"));
        components.add(new Wait(500, this.configuration, "Wait for 0.5 sec"));
        components.add(new GiroDriveEncoder(90.0, 0.2, 4000, this.configuration, "Drive for 2000 ticks heading forward"));
        components.add(new Wait(500, this.configuration, "Wait for 0.5 sec"));
        components.add(new GiroTurn(180.0, this.configuration, "Turn Right 90"));
        components.add(new Wait(500, this.configuration, "Wait for 0.5 sec"));
        components.add(new GiroDriveEncoder(180.0, 0.2, 4000, this.configuration, "Drive for 2000 ticks heading forward"));
        components.add(new Wait(500, this.configuration, "Wait for 0.5 sec"));
        components.add(new GiroTurn(270.0, this.configuration, "Turn Right 90"));
        components.add(new Wait(500, this.configuration, "Wait for 0.5 sec"));
        components.add(new GiroDriveEncoder(270.0, 0.2, 4000, this.configuration, "Drive for 2000 ticks heading forward"));
        components.add(new Wait(500, this.configuration, "Wait for 0.5 sec"));
        components.add(new GiroTurn(0.0, this.configuration, "Turn Right 90"));
        return components;
    }
}
