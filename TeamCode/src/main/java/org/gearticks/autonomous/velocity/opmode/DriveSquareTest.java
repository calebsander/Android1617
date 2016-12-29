package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.opmode.VelocityBaseOpMode;
import org.gearticks.autonomous.generic.statemachine.linear.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.linear.LinearStateMachineSimpleImpl;
import org.gearticks.autonomous.velocity.components.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.GiroTurn;
import org.gearticks.autonomous.velocity.components.Wait;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Autonomous(name = "Drive Square Test Autonomous")
@Disabled
public class DriveSquareTest extends VelocityBaseOpMode {
    private LinearStateMachine sm;

    protected void initialize() {
        super.initialize();
        this.createComponents();
        this.sm.initializeAtMatchStart();
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
        sm = new LinearStateMachineSimpleImpl(components);
    }
}
