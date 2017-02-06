package org.gearticks.autonomous.velocity.opmode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.OpenCVBeacon;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.opencv.OpenCvConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration;

import static org.gearticks.opmodes.utility.Utils.TAG;

@Autonomous
public class OpenCVTest extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent() {
        final VuforiaConfiguration vuforiaConfiguration = new VuforiaConfiguration();

        Log.d(TAG, "Creating new OpenCvConfiguration from OpenCVTest OpMode");
        final OpenCvConfiguration openCvConfiguration = new OpenCvConfiguration(this.hardwareMap);
        final LinearStateMachine sm = new LinearStateMachine();

        //sm.addComponent(new RangeSensorReadout(1.0, this.configuration, "Show range sensor val"));

//        sm.addComponent(new GiroTurn(-30.0, this.configuration, "Turn to wall"));
//        sm.addComponent(new GiroDriveToRangeDistance(15, -30.0, 0.15, 4000, this.configuration, "Range sensor drive to wall"));
//        sm.addComponent(new GiroTurn(0.0, this.configuration, "Straighten out"));
        //sm.addComponent(new GiroDriveAlongWallEncoder(10, 0.0, -0.25, 10000, this.configuration, "Range sensor drive along wall"));

//        sm.addComponent(new GiroDriveToLine(0.0, -0.15, 5000, this.configuration, "Range sensor drive along wall"));
//
        //sm.addComponent(new SidePressBeaconButton(0.0, vuforiaConfiguration, this.configuration, "Press Button"));

//        sm.addComponent(new GiroBananaTurnEncoder(0.0, 180.0, 0.15, 3000, this.configuration, "Banana Turn"));

        //sm.addComponent(new GiroTurn(400.0, this.configuration, "Straighten out"));

        sm.addComponent(new OpenCVBeacon(openCvConfiguration, vuforiaConfiguration, this.configuration, "OpenCVBeacon1"));
        sm.addComponent(new Wait(2, "Wait1"));
        sm.addComponent(new OpenCVBeacon(openCvConfiguration, vuforiaConfiguration, this.configuration, "OpenCVBeacon2"));
        sm.addComponent(new Wait(2, "Wait2"));
        sm.addComponent(new OpenCVBeacon(openCvConfiguration, vuforiaConfiguration, this.configuration, "OpenCVBeacon3"));


        return sm;
    }
}
