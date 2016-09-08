package org.gearticks;

import android.os.Environment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class AutonomousDatalogger {
	private static final char COMMA = ',';
	private static final File DATALOG_DIR = new File(Environment.getExternalStorageDirectory(), "Datalogs");
	static {
		DATALOG_DIR.mkdir(); //ensure that the datalog directory exists
	}
	private static File lastFile() {
		File newest = null;
		for (final File file : DATALOG_DIR.listFiles()) {
			if (
				newest == null ||
				new Date(file.lastModified()).after(new Date(newest.lastModified()))
			) newest = file;
		}
		if (newest == null) throw new RuntimeException("No datalogs saved");
		return newest;
	}
	public static String lastDatalogName() {
		return lastFile().getName();
	}
	public static ArrayList<String> lastDatalog() {
		final File newest = AutonomousDatalogger.lastFile();
		try {
			final BufferedReader lines = new BufferedReader(new FileReader(newest));
			final ArrayList<String> result = new ArrayList<>();
			for (String line; (line = lines.readLine()) != null;) result.add(line);
			lines.close();
			return result;
		}
		catch (IOException e) {
			throw new RuntimeException("Couldn't open file");
		}
	}

	private final PrintStream outStream;

	public AutonomousDatalogger() {
		final GregorianCalendar calendar = new GregorianCalendar();
		final int month = calendar.get(Calendar.MONTH) + 1;
		final int day = calendar.get(Calendar.DAY_OF_MONTH);
		final int year = calendar.get(Calendar.YEAR);
		final int hour = calendar.get(Calendar.HOUR_OF_DAY);
		final int minute = calendar.get(Calendar.MINUTE);
		final int randomInt = new Random().nextInt(100000);
		//e.g. 8-24-2016+13-01+ffbe.log
		final String fileName = Integer.toString(month) + "-" +
			Integer.toString(day) + "-" +
			Integer.toString(year) + "+" +
			Integer.toString(hour) + "-" +
			Integer.toString(minute) + "+" +
			Integer.toString(randomInt, 16) +
			".log";
		try {
			this.outStream = new PrintStream(new File(DATALOG_DIR, fileName));
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException("Couldn't create file with name: " + fileName);
		}
	}

	public void writeLine(Object... objects) {
		for (int i = 0; i < objects.length; i++) {
			this.outStream.print(objects[i]);
			if (i + 1 < objects.length) this.outStream.print(COMMA);
		}
		this.outStream.println();
	}
	public void close() {
		this.outStream.close();
	}
}