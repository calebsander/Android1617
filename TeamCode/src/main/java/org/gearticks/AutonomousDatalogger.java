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
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AutonomousDatalogger {
	private static final char COMMA = ',';
	private static final File DATALOG_DIR = new File(Environment.getExternalStorageDirectory(), "Datalogs");
	static {
		DATALOG_DIR.mkdir(); //ensure that the datalog directory exists
	}
	public static File[] listDatalogFiles() {
		return DATALOG_DIR.listFiles();
	}
	public static File lastFile() {
		File newest = null;
		for (final File file : listDatalogFiles()) {
			if (
				newest == null ||
				new Date(file.lastModified()).after(new Date(newest.lastModified()))
			) newest = file;
		}
		if (newest == null) throw new RuntimeException("No datalogs saved");
		return newest;
	}
	public static List<String> openDatalog(File datalogFile) {
		try {
			final BufferedReader lines = new BufferedReader(new FileReader(datalogFile));
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
		final int second = calendar.get(Calendar.SECOND);
		//e.g. 2016-08-24+13-01-20.log
		final String fileName =
			padWithZeros(Integer.toString(year), 4) + "-" +
			padWithZeros(Integer.toString(month), 2) + "-" +
			padWithZeros(Integer.toString(day), 2) + "+" +
			padWithZeros(Integer.toString(hour), 2) + "-" +
			padWithZeros(Integer.toString(minute), 2) + "-" +
			padWithZeros(Integer.toString(second), 2) +
			".log";
		try {
			this.outStream = new PrintStream(new File(DATALOG_DIR, fileName));
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException("Couldn't create file with name: " + fileName);
		}
	}

	private static String padWithZeros(String input, int characters) {
		final int charactersToAdd = characters - input.length();
		if (charactersToAdd <= 0) return input;
		final StringBuilder builder = new StringBuilder(characters);
		for (int i = 0; i < charactersToAdd; i++) builder.append('0');
		builder.append(input);
		return builder.toString();
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