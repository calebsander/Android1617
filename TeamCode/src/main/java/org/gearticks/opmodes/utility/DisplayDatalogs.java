package org.gearticks.opmodes.utility;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.gearticks.AutonomousDatalogger;
import org.gearticks.joystickoptions.ValuesJoystickOption;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
public class DisplayDatalogs extends BaseOpMode {
	private static final int LINES = 20;
	private ValuesJoystickOption<Filename> datalogOption;
	private List<String> dataLines;
	private int lineNumber;

	private static class Filename {
		public final File file;

		public Filename(File file) {
			this.file = file;
		}

		public String toString() {
			return this.file.getName();
		}
	}

	private void scrollUpPage() {
		final int newLineNumber = this.lineNumber - LINES;
		this.lineNumber = Math.max(newLineNumber, 0);
	}
	private void scrollDownPage() {
		final int newLineNumber = this.lineNumber + LINES;
		this.lineNumber = Math.min(newLineNumber, this.getEndIndex());
	}
	private void goToStart() {
		this.lineNumber = 0;
	}
	private int getEndIndex() {
		return Math.max(this.dataLines.size() - LINES, 0);
	}
	private void goToEnd() {
		this.lineNumber = this.getEndIndex();
	}
	protected void initialize() {
		final File[] datalogFiles = AutonomousDatalogger.listDatalogFiles();
		if (datalogFiles.length == 0) Utils.throwException("No datalog files");
		final Filename[] datalogFilenames = new Filename[datalogFiles.length];
		for (int i = 0; i < datalogFiles.length; i++) datalogFilenames[i] = new Filename(datalogFiles[i]);
		Arrays.sort(datalogFilenames, (lhs, rhs) -> -lhs.toString().compareTo(rhs.toString()));
		this.datalogOption = new ValuesJoystickOption<>("Datalog", datalogFilenames);
		this.addOption(this.datalogOption);
		this.goToStart();
	}
	protected void matchStart() {
		this.dataLines = AutonomousDatalogger.openDatalog(this.datalogOption.getRawSelectedOption().file);
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