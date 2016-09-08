package org.gearticks.opmodes.utility;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.ArrayList;
import org.gearticks.AutonomousDatalogger;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous(name = "Display Datalog")
public class DisplayDatalogs extends BaseOpMode {
	private static final int LINES = 20;
	private String logName;
	private ArrayList<String> dataLines;
	private int lineNumber;

	private void scrollUpPage() {
		final int newLineNumber = this.lineNumber - LINES;
		if (newLineNumber > -1) this.lineNumber = newLineNumber;
	}
	private void scrollDownPage() {
		final int newLineNumber = this.lineNumber + LINES;
		if (newLineNumber < this.dataLines.size()) this.lineNumber = newLineNumber;
	}
	private void goToStart() {
		this.lineNumber = 0;
	}
	private void goToEnd() {
		this.lineNumber = Math.max(0, this.dataLines.size() - LINES);
	}
	protected void initialize() {
		this.logName = AutonomousDatalogger.lastDatalogName();
		this.dataLines = AutonomousDatalogger.lastDatalog();
		this.goToStart();
	}
	protected void loopBeforeStart() {
		this.telemetry.clear();
		this.telemetry.addLine(this.logName);
	}
	protected void loopAfterStart() {
		this.telemetry.clear();
		for (int i = 0; i < LINES; i++) {
			final int logLine = this.lineNumber + i;
			if (logLine >= this.dataLines.size()) break;
			this.telemetry.addData("Line " + Integer.toString(logLine + 1), this.dataLines.get(logLine));
		}
		if (this.gamepads[0].getY() && !this.gamepads[0].getLast().getY()) this.scrollUpPage();
		else if (this.gamepads[0].getA() && !this.gamepads[0].getLast().getA()) this.scrollDownPage();
		else if (this.gamepads[0].getX() && !this.gamepads[0].getLast().getX()) this.goToStart();
		else if (this.gamepads[0].getB() && !this.gamepads[0].getLast().getB()) this.goToEnd();
	}
}