package org.gearticks.autonomous.sample.components;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Sample of an AutonomousComponent that makes a decision and has 2 output ports
 * Transition 1 for red, transition 2 for blue
 *
 */
public class ObserveColor extends AutonomousComponentVelocityBase {
	
	public ObserveColor(@NonNull VelocityConfiguration configuration, String id) {
		super(configuration, id);
	}

	

	@Override
	public void setup(int inputPort) {
		super.setup(inputPort);
		
	}

	@Override
	public int run() {
		int transition = 0;
		
		/*
		 * 30 % probability blue = transition 2
		 * 30 % probability red = transition 1
		 * 40 % Probability that no color is detected
		 */
		double random = Math.random();
		if (random > 0.7){
			transition = 2;
		}
		else if (random > 0.4){
			transition = 1;
		}
		return transition;
	}

	@Override
	public void tearDown() {
		super.tearDown();
	}
	
	
	

}
