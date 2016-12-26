package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.opmode.VelocityBaseOpMode;
import org.gearticks.autonomous.generic.statemachine.network.LinearStateMachineFullImpl;
import org.gearticks.autonomous.velocity.components.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.Wait;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Autonomous(name = "Color and Gyro Sensor Test Autonomous")
public class DriveSquareTest extends VelocityBaseOpMode {
    private LinearStateMachineFullImpl sm;

    protected void initialize() {
        super.initialize();
        this.createComponents();
        this.sm.initialize();
    }

    protected void loopBeforeStart() {
        super.loopBeforeStart();
    }

    protected void matchStart() {
        super.matchStart();
        this.sm.setup();
    }

    protected void loopAfterStart(){
        super.loopAfterStart();
        this.sm.run();
    }

    protected void matchEnd() {
        super.matchEnd();
        this.sm.tearDown();
    }

    protected void createComponents(){
        List<AutonomousComponent> components = new ArrayList<>();
//        components.add(new Wait(2000, null, "Wait for 2 sec"));
        components.add(new GiroDriveEncoder(0.0, 0.5, 2000, null, "Drive for 2000 ticks heading forward"));
        components.add(new Wait(2000, null, "Wait for 2 sec"));
        components.add(new GiroDriveEncoder(0.0, 0.5, 2000, null, "Drive for 2000 ticks heading forward"));
        components.add(new Wait(2000, null, "Wait for 2 sec"));
        sm = new LinearStateMachineFullImpl(components);
    }
}
