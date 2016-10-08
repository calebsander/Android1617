package org.gearticks.dimsensors.i2c;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;

import java.util.Arrays;

public class MonochromeDisplay extends I2CSensor {
	protected I2cAddr getAddress() {
		return I2cAddr.create7bit(0x3C);
	}

	public static final int WIDTH = 128;
	public static final int HEIGHT = 64;
	private static final int BITS = WIDTH * HEIGHT;
	private static final int BYTES = BITS / 8;
	private static final int WRITE_BYTE_LENGTH = 16;
	private static final int WRITE_RUNS = BYTES / WRITE_BYTE_LENGTH;

	private enum Register {
		COMMAND(0x00),
		DATA(0x40);

		public final int register;
		Register(int register) {
			this.register = register;
		}
	}
	private enum Command {
		MEMORY_MODE(0x20),
		COLUMN_ADDRESS(0x21),
		PAGE_ADDRESS(0x22),
		SET_START_LINE(0x40),
		SET_CONTRAST(0x81),
		CHARGE_PUMP(0x8D),
		SEGRE_MAP(0xA0),
		DISPLAY_ALL_ON_RESUME(0xA4),
		NORMAL_DISPLAY(0xA6),
		SET_MULTIPLEX(0xA8),
		DISPLAY_OFF(0xAE),
		DISPLAY_ON(0xAF),
		COM_SCAN_DEC(0xC8),
		SET_DISPLAY_OFFSET(0xD3),
		SET_DISPLAY_CLOCK_DIV(0xD5),
		SET_VCOM_DETECT(0xDB),
		SET_PRECHARGE(0xD9),
		SET_COMP_INS(0xDA);

		public final int data;
		Command(int data) {
			this.data = data;
		}
	}

	private byte[] pixels;
	private SensorWriteRequest[] pixelRunRequests;

	public MonochromeDisplay(I2cDevice device) {
		super(device);
		this.pixels = new byte[WIDTH * HEIGHT];
		this.pixelRunRequests = new SensorWriteRequest[WRITE_RUNS];
		for (int run = 0; run < WRITE_RUNS; run++) this.pixelRunRequests[run] = new SensorWriteRequest(Register.DATA.register, WRITE_BYTE_LENGTH);
		this.sendCommand(Command.DISPLAY_OFF.data);

		this.sendCommand(Command.SET_DISPLAY_CLOCK_DIV.data);
		this.sendCommand(0x80); //the suggested ratio 0x80

		this.sendCommand(Command.SET_MULTIPLEX.data);
		this.sendCommand(0x3F);

		this.sendCommand(Command.SET_DISPLAY_OFFSET.data);
		this.sendCommand(0); //no offset

		this.sendCommand(Command.SET_START_LINE.data | 0); //line #0

		this.sendCommand(Command.CHARGE_PUMP.data);
		this.sendCommand(0x14); //non-external VCC

		this.sendCommand(Command.MEMORY_MODE.data);
		this.sendCommand(0);

		this.sendCommand(Command.SEGRE_MAP.data | 1);

		this.sendCommand(Command.COM_SCAN_DEC.data);

		this.sendCommand(Command.SET_COMP_INS.data);
		this.sendCommand(0x12);

		this.sendCommand(Command.SET_CONTRAST.data);
		this.sendCommand(0xCF);

		this.sendCommand(Command.SET_PRECHARGE.data);
		this.sendCommand(0xF1);

		this.sendCommand(Command.SET_VCOM_DETECT.data);
		this.sendCommand(Register.DATA.register);

		this.sendCommand(Command.DISPLAY_ALL_ON_RESUME.data);
		this.sendCommand(Command.NORMAL_DISPLAY.data);
		this.sendCommand(Command.DISPLAY_ON.data);
	}

	public synchronized void drawPixel(int x, int y, boolean on) {
		if (x < 0 || x >= WIDTH) throw new IllegalArgumentException(Integer.toString(x) + " is an invalid x");
		if (y < 0 || y >= HEIGHT) throw new IllegalArgumentException(Integer.toString(y) + " is an invalid y");
		final int selectedBit = 1 << (y & 7);
		if (on) this.pixels[x + (y >>> 3) * WIDTH] |= selectedBit;
		else this.pixels[x + (y >>> 3) * WIDTH] &= ~selectedBit;
	}
	public synchronized void clear() {
		Arrays.fill(this.pixels, (byte)0);
	}
	private synchronized void sendCommand(int command) {
		final SensorWriteRequest request = new SensorWriteRequest(Register.COMMAND.register, 1); //make a new request each time so multiple can be queued
		request.setWriteData(new byte[]{(byte)command});
	}
	public synchronized void display() {
		this.sendCommand(Command.COLUMN_ADDRESS.data);
		this.sendCommand(0); //start with 0th column
		this.sendCommand(WIDTH - 1); //end on last column
		this.sendCommand(Command.PAGE_ADDRESS.data);
		this.sendCommand(0); //start on lowest page
		this.sendCommand(7); //end on page 7 (has to do with height of display)
		for (int run = 0; run < WRITE_RUNS; run++) {
			final byte[] bytes = new byte[WRITE_BYTE_LENGTH];
			System.arraycopy(this.pixels, run * WRITE_BYTE_LENGTH, bytes, 0, WRITE_BYTE_LENGTH);
			final SensorWriteRequest pixelRunRequest = this.pixelRunRequests[run];
			pixelRunRequest.setWriteData(bytes);
			pixelRunRequest.addToQueue(); //send even if data hasn't changed
		}
	}
}