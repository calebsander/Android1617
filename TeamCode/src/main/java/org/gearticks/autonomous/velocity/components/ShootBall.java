package org.gearticks.autonomous.velocity.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Created by irene on 12/26/2016.
 */

public class ShootBall extends AutonomousComponentVelocityBase {


    /**
     *
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public ShootBall(VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup(int inputPort) {
        super.setup(inputPort);
        this.getConfiguration().shooter.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.getConfiguration().shooter.setTarget(VelocityConfiguration.MotorConstants.SHOOTER_TICKS_TO_SHOOTING);
        this.getConfiguration().shooter.setPower(VelocityConfiguration.MotorConstants.SHOOTER_BACK);
    }

    @Override
    public int run() {
        int transition = super.run();

        if (!this.getConfiguration().shooter.notAtTarget()){
            transition = 1;
        }

        return transition;
    }

    @Override
    public void tearDown() {
        super.tearDown();
    }
}
