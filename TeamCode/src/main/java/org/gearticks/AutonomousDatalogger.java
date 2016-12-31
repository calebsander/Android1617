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

/**
 * A way of writing arbitrary information to a CSV file.
 * The file is stored on the phone and can be viewed
 * either with {@link org.gearticks.opmodes.utility.DisplayDatalogs}
 * or by pulling the file onto a computer.
 */
public class AutonomousDatalogger {
	/**
	 * The column separator used to make it a valid CSV file
	 */
	private static final char COMMA = ',';
	/**
	 * The directory where all datalogs are stored.=
	 * Becomes "/sdcard/Datalogs".
	 */
	private static final File DATALOG_DIR = new File(Environment.getExternalStorageDirectory(), "Datalogs");
	static {
		DATALOG_DIR.mkdir(); //ensure that the datalog directory exists
	}
	/**
	 * Gets a list of all the files in the datalog directory
	 * @return all the existing datalogs, in no particular order
	 */
	public static File[] listDatalogFiles() {
		return DATALOG_DIR.listFiles();
	}
	/**
	 * Gets the most recent datalog
	 * @return the most recently written datalog file
	 */
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
	/**
	 * Gets the text lines out of a datalog file
	 * @param datalogFile the file to open
	 * @return a list containing all the lines of the file
	 */
	public static List<String> openDatalog(File datalogFile) {
		try {
			final BufferedReader lines = new BufferedReader(new FileReader(datalogFile));
			final List<String> result = new ArrayList<>();
			for (String line; (line = lines.readLine()) != null;) result.add(line);
			lines.close();
			return result;
		}
		catch (IOException e) {
			throw new RuntimeException("Couldn't open file");
		}
	}

	/**
	 * The file stream to which all information is written
	 */
	private final PrintStream outStream;

	/**
	 * Creates a new datalog object associated with a file.
	 * The filename looks like YYYY-MM-DD+HH-MM-SS.log.
	 */
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
			padWithZeros(year, 4) + "-" +
			padWithZeros(month, 2) + "-" +
			padWithZeros(day, 2) + "+" +
			padWithZeros(hour, 2) + "-" +
			padWithZeros(minute, 2) + "-" +
			padWithZeros(second, 2) + ".log";
		try {
			this.outStream = new PrintStream(new File(DATALOG_DIR, fileName));
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException("Couldn't create file with name: " + fileName);
		}
	}

	/**
	 * Adds zeros to the front of a string representing a number
	 * so that it has the desired length
	 * @param intInput the number to pad (e.g. 12)
	 * @param characters the desired number of characters in the padded string (e.g. 4)
	 * @return the padded string, e.g. "0012"
	 */
	private static String padWithZeros(int intInput, int characters) {
		final String input = Integer.toString(intInput);
		final int charactersToAdd = characters - input.length();
		if (charactersToAdd <= 0) return input;
		final StringBuilder builder = new StringBuilder(characters);
		for (int i = 0; i < charactersToAdd; i++) builder.append('0');
		builder.append(input);
		return builder.toString();
	}
	/**
	 * Write a line to the datalog containing
	 * any number of comma-separated values.
	 * This can be called as many times as desired.
	 * @param objects the objects to write (will be converted into strings)
	 */
	public void writeLine(Object... objects) {
		for (int i = 0; i < objects.length; i++) {
			this.outStream.print(objects[i]);
			if (i + 1 < objects.length) this.outStream.print(COMMA);
		}
		this.outStream.println();
	}
	/**
	 * Close the underlying file stream.
	 * This should be called at the end of every OpMode
	 * that creates a datalog.
	 */
	public void close() {
		this.outStream.close();
	}
}