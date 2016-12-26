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
 * Created by vterpstra on 12/26/2016.
 */
@Autonomous(name = "Color and Gyro Sensor Test Autonomous")
public class DriveSquareTest extends VelocityBaseOpMode {
    private LinearStateMachineFullImpl sm;

    protected void initialize() {
        super.initialize();
        this.sm.setup();
    }

    protected void loopBeforeStart() {
        super.loopBeforeStart();
    }

    protected void matchStart() {
        super.matchStart();
    }

    protected void loopAfterStart(){
        super.loopAfterStart();
        this.sm.run();
    }

    protected void matchEnd() {
        super.matchEnd();
    }

    protected void createComponents(){
        List<AutonomousComponent> components = new ArrayList<>();
//        components.add(new Wait(2000, null, "Wait for 2 sec"));
//        components.add(new GiroDriveEncoder(2000, 100, 2000, null, "Drive for 2 sec heading 100"));
//        components.add(new Wait(2000, null, "Wait for 2 sec"));
        sm = new LinearStateMachineFullImpl(components);
    }
}
