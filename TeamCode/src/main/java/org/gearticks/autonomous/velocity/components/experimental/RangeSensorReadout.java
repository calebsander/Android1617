package org.gearticks.autonomous.velocity.components.experimental;

import android.util.Log;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.dimsensors.i2c.GearticksMRRangeSensor;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.Utils;

public class RangeSensorReadout extends AutonomousComponentHardware<VelocityConfiguration, DefaultTransition> {
    private final double timeLimit;

    public RangeSensorReadout(double timeLimit, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, DefaultTransition.class, id);
        this.timeLimit = timeLimit;
    }

    @Override
    public void setup() {
        super.setup();
        final GearticksMRRangeSensor rangeSensor = this.configuration.rangeSensor;
        rangeSensor.ultrasonicRequest.startReading();
        rangeSensor.opticalRequest.startReading();
    }

    @Override
    public DefaultTransition run() {
        final DefaultTransition superTransition = super.run();
        if (superTransition != null) return superTransition;

        double ultrasonicDistance = this.configuration.rangeSensor.cmUltrasonic();
        double opticalDistance = this.configuration.rangeSensor.cmOptical();

        Log.d(Utils.TAG, "Optical distance = " + opticalDistance + ", Ultrasonic distance = " + ultrasonicDistance);

        if (this.stageTimer.seconds() > this.timeLimit) return DefaultTransition.DEFAULT;
        else return null;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        final GearticksMRRangeSensor rangeSensor = this.configuration.rangeSensor;
        rangeSensor.ultrasonicRequest.stopReading();
        rangeSensor.opticalRequest.stopReading();
    }
}
