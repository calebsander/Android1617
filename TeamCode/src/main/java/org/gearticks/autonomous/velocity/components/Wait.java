package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class Wait extends AutonomousComponentVelocityBase {
	private final int waitTime;//msec

	/**
	 *
	 * @param waitTime - wait time
	 * @param configuration - config file
	 * @param id - descriptive name for logging
	 */
	public Wait(int waitTime, @NonNull VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.waitTime = waitTime;
	}

//	/**
//	 *
//	 * @param waitTime - in milliseconds
//	 */
//	public Wait(int waitTime) {
//		super(configuration, id);
//		this.waitTime = waitTime;
//	}

//	@Override
//	public void setup(int inputPort) {
//		super.setup(inputPort);
//	}

	@Override
	public int run() {
		int transition = super.run();

        if (this.getStageTimer().milliseconds() >= this.waitTime){
            transition = 1;
        }
		return transition;
	}

//	@Override
//	public void tearDown() {
//		super.tearDown();
//	}
	
	
	

}
