package org.gearticks.autonomous.velocity.components.experimental;

import android.util.Log;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.utility.Utils;

public class RangeSensorReadout extends AutonomousComponentHardware<VelocityConfiguration> {
    public final double timeLimit;

    public RangeSensorReadout(double timeLimit, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext.configuration, id);
        this.timeLimit = timeLimit;
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.rangeSensor.ultrasonicRequest.startReading();
        this.configuration.rangeSensor.opticalRequest.startReading();
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        double ultrasonicDistance = this.configuration.rangeSensor.cmUltrasonic();
        double opticalDistance = this.configuration.rangeSensor.cmOptical();

        Log.d(Utils.TAG, "Optical distance = " + opticalDistance + ", Ultrasonic distance = " + ultrasonicDistance);

        if (this.stageTimer.seconds() > this.timeLimit) return NEXT_STATE;
        else return NOT_DONE;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        //Custom code here
    }
}
