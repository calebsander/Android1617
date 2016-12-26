package org.gearticks.autonomous.sample;

import java.util.concurrent.TimeUnit;

/**
 * A base class to 'emulate' a OpmOde outside of the FTC library
 *
 */
public abstract class OpModeTest {
	
	public void initialize(){
		
	}
	
	public void setup(){
		
	}
	
	public void loop(){
		
	}
	
	public void tearDown(){
		
	}
	
	public static void runOpMode(OpModeTest opMode, int maxRunTimeSeconds) {
		
		System.out.println("Start");
		opMode.initialize();
		opMode.setup();
		long maxRunTime = maxRunTimeSeconds * 1000;// in milliseconds
		long endTime = System.currentTimeMillis() + maxRunTime;
		while (true){
			opMode.loop();
			if (System.currentTimeMillis() > endTime){
				break;
			}
			try {
			    TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
			    //Handle exception
			}
		}
		opMode.tearDown();
		System.out.println("Done");
	}

}
