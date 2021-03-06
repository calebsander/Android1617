//Our version of a driver for the Adafruit TCS34725 color sensor
package org.gearticks.dimsensors.i2c;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import java.nio.ByteBuffer;

public class TCS34725 extends I2CSensor {
	protected final I2cAddr getAddress() {
		return I2cAddr.create7bit(0x29);
	}
	private static final int COMMAND_BIT = 0x80;
	private enum Register {
		ENABLE(0x00),
		INTEGRATION_TIME(0x01),
		CLEAR_LOW(0x14),
		CLEAR_HIGH(0x15),
		RED_LOW(0x16),
		RED_HIGH(0x17),
		GREEN_LOW(0x18),
		GREEN_HIGH(0x19),
		BLUE_LOW(0x1A),
		BLUE_HIGH(0x1B);

		public final int register;
		Register(int register) {
			this.register = register | COMMAND_BIT;
		}
	}
	private static final byte POWER_ON = 0x01;
	private static final byte ADC_ON = 0x02;
	private static final byte FULL_ON = POWER_ON | ADC_ON;
	private static final byte INTERRUPT_ON = 0x10;
	private enum StartupState {
		POWER,
		ADC_ENABLE,
		DONE
	}
	private StartupState state;
	private static final int SHORT_MASK = 0xFFFF;
	private static final double INTEGRATION_INTERVAL = 2.4; //milliseconds
	private static final double DEFAULT_INTEGRATION_INTERVAL = INTEGRATION_INTERVAL * 10;

	private SensorWriteRequest enableRequest;
	private SensorReadRequest clearRequest;
	private SensorReadRequest colorRequest;
	private SensorWriteRequest integrationTime;

	private static final int NO_DATA = -1;
	private int redReset, greenReset, blueReset, clearReset;

	public TCS34725(I2cDevice device) {
		super(device);
		this.initialize();
	}
	public TCS34725(I2cDevice device, I2CSwitcher switcher, int port) {
		super(device, switcher, port);
		this.initialize();
	}
	private void initialize() {
		this.enableRequest = new SensorWriteRequest(Register.ENABLE.register, 1);
		this.enableRequest.setWriteData(new byte[]{POWER_ON});
		this.state = StartupState.POWER;
		this.clearRequest = this.makeReadRequest(Register.CLEAR_LOW.register, Register.CLEAR_HIGH.register);
		this.colorRequest = this.makeReadRequest(Register.CLEAR_LOW.register, Register.BLUE_HIGH.register);
		this.integrationTime = new SensorWriteRequest(Register.INTEGRATION_TIME.register, 1);
		this.redReset = this.greenReset = this.blueReset = this.clearReset = 0;
	}

	protected void readyCallback() {
		if (this.state == null) return;
		switch (this.state) {
			case POWER:
				this.state = StartupState.ADC_ENABLE;
				this.enableRequest.setWriteData(new byte[]{FULL_ON});
				break;
			case ADC_ENABLE:
				this.state = StartupState.DONE;
				this.setIntegrationTime(DEFAULT_INTEGRATION_INTERVAL);
		}
	}
	//Start reading the clear channel
	public void startReadingClear() {
		this.clearRequest.startReading();
	}
	//Start reading all four channels
	public void startReadingColor() {
		this.colorRequest.startReading();
	}
	//Stop reading colors
	public void stopReading() {
		this.clearRequest.stopReading();
		this.colorRequest.stopReading();
	}
	//Get the value of the clear channel
	public int getClear() {
		synchronized (this) {
			if (!(this.clearRequest.hasReadData() || this.colorRequest.hasReadData())) return NO_DATA;
			final byte[] data;
			//Use the most recent data available
			if (this.clearRequest.nanosSinceAction() < this.colorRequest.nanosSinceAction()) data = this.clearRequest.getReadData();
			else data = this.colorRequest.getReadData();
			if (data.length == 0) return NO_DATA;
			return ByteBuffer.wrap(new byte[]{data[Register.CLEAR_HIGH.register - this.clearRequest.getRegister()], data[Register.CLEAR_LOW.register - this.clearRequest.getRegister()]}).getShort() & SHORT_MASK;
		}
	}
	/**
	 * Get the value of the clear channel, relative to the reset point.
	 * Returns a number from -65535 to 65535, or NO_DATA if no value exists.
	 * @return the relative value of the clear channel, or NO_DATA if no value exists
	 */
	public int getRelativeClear() {
		final int absoluteClear = this.getClear();
		if (absoluteClear == NO_DATA) return NO_DATA;
		return absoluteClear - this.clearReset;
	}
	//Get the value of the red channel
	public int getRed() {
		if (!this.colorRequest.hasReadData()) return NO_DATA;
		final byte[] data = this.colorRequest.getReadData();
		if (data.length == 0) return NO_DATA;
		return ByteBuffer.wrap(new byte[]{data[Register.RED_HIGH.register - this.colorRequest.getRegister()], data[Register.RED_LOW.register - this.colorRequest.getRegister()]}).getShort() & SHORT_MASK;
	}
	/**
	 * Get the value of the red channel, relative to the reset point.
	 * Returns a number from -65535 to 65535, or NO_DATA if no value exists.
	 * @return the relative value of the red channel, or NO_DATA if no value exists
	 */
	public int getRelativeRed() {
		final int absoluteRed = this.getRed();
		if (absoluteRed == NO_DATA) return NO_DATA;
		return absoluteRed - this.redReset;
	}
	//Get the value of the green channel
	public int getGreen() {
		if (!this.colorRequest.hasReadData()) return NO_DATA;
		final byte[] data = this.colorRequest.getReadData();
		if (data.length == 0) return NO_DATA;
		return ByteBuffer.wrap(new byte[]{data[Register.GREEN_HIGH.register - this.colorRequest.getRegister()], data[Register.GREEN_LOW.register - this.colorRequest.getRegister()]}).getShort() & SHORT_MASK;
	}
	/**
	 * Get the value of the green channel, relative to the reset point.
	 * Returns a number from -65535 to 65535, or NO_DATA if no value exists.
	 * @return the relative value of the green channel, or NO_DATA if no value exists
	 */
	public int getRelativeGreen() {
		final int absoluteGreen = this.getGreen();
		if (absoluteGreen == NO_DATA) return NO_DATA;
		return absoluteGreen - this.greenReset;
	}
	//Get the value of the blue channel
	public int getBlue() {
		if (!this.colorRequest.hasReadData()) return NO_DATA;
		final byte[] data = this.colorRequest.getReadData();
		if (data.length == 0) return NO_DATA;
		return ByteBuffer.wrap(new byte[]{data[Register.BLUE_HIGH.register - this.colorRequest.getRegister()], data[Register.BLUE_LOW.register - this.colorRequest.getRegister()]}).getShort() & SHORT_MASK;
	}
	/**
	 * Get the value of the blue channel, relative to the reset point.
	 * Returns a number from -65535 to 65535, or NO_DATA if no value exists.
	 * @return the relative value of the blue channel, or NO_DATA if no value exists
	 */
	public int getRelativeBlue() {
		final int absoluteBlue = this.getBlue();
		if (absoluteBlue == NO_DATA) return NO_DATA;
		return absoluteBlue - this.blueReset;
	}
	/**
	 * Calibrates the sensor so the relative methods
	 * will return values relative to the current values.
	 * If no data has been read on a channel, it won't get calibrated.
	 */
	public void calibrate() {
		final int absoluteClear = this.getClear();
		if (absoluteClear != NO_DATA) this.clearReset = absoluteClear;
		final int absoluteRed = this.getRed();
		if (absoluteRed != NO_DATA) this.redReset = absoluteRed;
		final int absoluteGreen = this.getGreen();
		if (absoluteGreen != NO_DATA) this.greenReset = absoluteGreen;
		final int absoluteBlue = this.getBlue();
		if (absoluteBlue != NO_DATA) this.blueReset = absoluteBlue;
	}
	//Calculates the byte value necessary to get the closest to the desired integration time
	private static byte getIntegrationRequestValue(double integrationMillis) {
		final int intervals = Math.min(Math.max((int)Math.round(integrationMillis / INTEGRATION_INTERVAL), 0x01), 0x100);
		return (byte)(0x100 - intervals);
	}
	//Sets the integration time
	public void setIntegrationTime(double time) {
		this.integrationTime.setWriteData(new byte[]{TCS34725.getIntegrationRequestValue(time)});
	}
	//Enables/disabled LED on Flora models
	public void setFloraLED(boolean enabled) {
		byte writeByte = FULL_ON;
		if (!enabled) writeByte |= INTERRUPT_ON;
		this.enableRequest.setWriteData(new byte[]{writeByte});
	}
}