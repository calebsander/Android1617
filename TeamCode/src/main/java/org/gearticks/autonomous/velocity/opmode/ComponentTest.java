package org.gearticks.autonomous.velocity.opmode;

import android.support.annotation.NonNull;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.Vuforia.VuforiaConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.opmode.VelocityLinearAutonomousBaseOpMode;
import org.gearticks.autonomous.velocity.components.CompleteBallLoad;
import org.gearticks.autonomous.velocity.components.DebugPause;
import org.gearticks.autonomous.velocity.components.FacePicture;
import org.gearticks.autonomous.velocity.components.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.GiroTurn;
import org.gearticks.autonomous.velocity.components.LoadBall;
import org.gearticks.autonomous.velocity.components.MoveShooterDown;
import org.gearticks.autonomous.velocity.components.ResetSnake;
import org.gearticks.autonomous.velocity.components.ShootBall;
import org.gearticks.autonomous.velocity.components.VuforiaIn;
import org.gearticks.autonomous.velocity.components.Wait;
import org.gearticks.GamepadWrapper;
import org.gearticks.opmodes.utility.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Autonomous(name = "Component Test")
public class ComponentTest extends VelocityLinearAutonomousBaseOpMode {
    private static final int CALVIN = 0, JACK = 1;
    private VuforiaConfiguration vuforiaConfiguration;

    @Override
    protected void initialize() {
        this.vuforiaConfiguration = new VuforiaConfiguration();
        super.initialize();
    }

    @Override
    protected void matchStart(){
        super.matchStart();
        this.vuforiaConfiguration.activate(); // TODO may want to activate/deactivate in aut component
    }

    @NonNull
    @Override
    protected List<AutonomousComponent> createComponents(){
        List<AutonomousComponent> components = new ArrayList<>();
        // Shoot 2 balls
        components.add(new MoveShooterDown(this.configuration, "MoveShooterDown"));
        components.add(new LoadBall(this.configuration, "Load 1st ball"));
        components.add(new ResetSnake(this.configuration, "Reset Snake"));
//        components.add(new CompleteBallLoad(this.configuration, "Load Ball + Reset"));
//        components.add(new DebugPause(this.gamepads, this.telemetry, this.configuration, "Wait until X is pressed"));
        components.add(new ShootBall(this.configuration, "Shoot 1st ball"));
        components.add(new DebugPause(this.gamepads, this.telemetry, this.configuration, "Wait until X is pressed"));
        components.add(new MoveShooterDown(this.configuration, "Move Shooter Down"));
        components.add(new LoadBall(this.configuration, "Load 2nd ball"));
        components.add(new ResetSnake(this.configuration, "Reset Snake"));
        components.add(new ShootBall(this.configuration, "Shoot 2nd ball"));
        components.add(new DebugPause(this.gamepads, this.telemetry, this.configuration, "Wait until X is pressed"));

        // Drive to beacon
        components.add(new GiroDriveEncoder(0.0, 0.7, 1700, this.configuration, "Drive off wall for 1700 ticks"));
        components.add(new Wait(500, this.configuration, "Wait before first turn"));
        components.add(new GiroTurn(40.0, this.configuration, "Turn to far target"));
        components.add(new GiroDriveEncoder(40.0, 0.7, 2900, this.configuration, "Drive in front of near target"));
        components.add(new Wait(500, this.configuration, "Wait before second turn"));
        components.add(new GiroTurn(90.0, this.configuration, "Face near target"));
        components.add(new DebugPause(this.gamepads, this.telemetry, this.configuration, "Wait until X is pressed"));

        // Go to 1st beacon
        Log.i(Utils.TAG, "This is a test");
//        components.add(new GiroDriveEncoder(0.0, 0.7, 10, this.configuration, "Drive off wall for 1700 ticks"));
        components.add(new DebugPause(this.gamepads, this.telemetry, this.configuration, "Wait until X is pressed"));
        components.add(new VuforiaIn(500F, this.vuforiaConfiguration, this.configuration, "Drive to near target"));
        components.add(new DebugPause(this.gamepads, this.telemetry, this.configuration, "Wait until X is pressed"));
        components.add(new FacePicture(this.vuforiaConfiguration, this.configuration, "Face near target"));
        components.add(new DebugPause(this.gamepads, this.telemetry, this.configuration, "Wait until X is pressed"));
        components.add(new VuforiaIn(175F, this.vuforiaConfiguration, this.configuration, "Drive closer to near target"));

        // Press correct button

        return components;
    }
}
