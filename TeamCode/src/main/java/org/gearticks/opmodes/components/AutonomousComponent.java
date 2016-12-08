package org.gearticks.opmodes.components;

public interface AutonomousComponent<OP_MODE_CLASS> {
	void init(OP_MODE_CLASS opMode);
	void loop(OP_MODE_CLASS opMode);
	boolean isDone(OP_MODE_CLASS opMode);
	AutonomousComponent<OP_MODE_CLASS> getNextComponent(OP_MODE_CLASS opMode);
	void stop(OP_MODE_CLASS opMode);
}