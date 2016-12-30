package org.gearticks.autonomous.velocity.opmode;

import android.support.annotation.NonNull;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.Vuforia.VuforiaConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.opmode.VelocityLinearAutonomousBaseOpMode;
import org.gearticks.autonomous.velocity.components.DebugPause;
import org.gearticks.autonomous.velocity.components.FacePicture;
import org.gearticks.autonomous.velocity.components.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.GiroTurn;
import org.gearticks.autonomous.velocity.components.PressBeaconButton;
import org.gearticks.autonomous.velocity.components.SimpleNonLinearComposite;
import org.gearticks.autonomous.velocity.components.VuforiaIn;
import org.gearticks.autonomous.velocity.components.Wait;
import org.gearticks.opmodes.utility.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Autonomous(name = "Full StateMachine Test")
public class FullStateMachineTest extends VelocityLinearAutonomousBaseOpMode {
    private static final int CALVIN = 0, JACK = 1;
    //private VuforiaConfiguration vuforiaConfiguration;

    @Override
    protected void initialize() {
//        Log.i(Utils.TAG, "Begin vuforia config init");
//        this.vuforiaConfiguration = new VuforiaConfiguration();
//        Log.i(Utils.TAG, "end vuforia config init");
        Log.d(Utils.TAG, "begin state machine init");
        super.initialize();
        Log.d(Utils.TAG, "end state machine init");

    }

    @Override
    protected void matchStart(){
        super.matchStart();
        //this.vuforiaConfiguration.activate(); //activate/deactivate in aut component
    }

    @NonNull
    @Override
    protected List<AutonomousComponent> createComponents(){
        List<AutonomousComponent> components = new ArrayList<>();

        Telemetry telemetry = Utils.assertNotNull(this.telemetry, "OpMode.telemetry is null");
        components.add(new DebugPause(this.gamepads, telemetry, this.configuration, "Wait until X is pressed"));
        components.add(new Wait(500, this.configuration, "Wait for 0.5 sec"));
        components.add(new SimpleNonLinearComposite(this.gamepads, telemetry, this.configuration, "Run decision"));


        return components;
    }
}
