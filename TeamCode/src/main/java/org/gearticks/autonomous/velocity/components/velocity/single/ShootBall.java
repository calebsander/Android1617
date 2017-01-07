package org.gearticks.autonomous.velocity.components.velocity.single;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class ShootBall extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public ShootBall(@NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.shooter.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.configuration.shooter.setTarget(VelocityConfiguration.MotorConstants.SHOOTER_TICKS_TO_SHOOTING);
        this.configuration.shooter.setPower(VelocityConfiguration.MotorConstants.SHOOTER_BACK);
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        if (this.configuration.isShooterAtTarget()) return NEXT_STATE;
        else return NOT_DONE;
    }
}
