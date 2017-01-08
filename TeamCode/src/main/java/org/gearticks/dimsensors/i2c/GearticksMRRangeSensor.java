package org.gearticks.dimsensors.i2c;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;

public class GearticksMRRangeSensor extends I2CSensor {
	public static final double NO_DATA = -1.0;
	private static final int MIN_VALID_OPTICAL_READING = 10;
	private static final double P_CONST = -1.02001;
	private static final double Q_CONST = 0.00311326;
	private static final double R_CONST = -8.39366;

	protected I2cAddr getAddress() {
		return I2cAddr.create8bit(0x28);
	}
	private enum Register {
		ULTRASONIC(0x04),
		OPTICAL(0x05);

		public final int register;
		Register(int register) {
			this.register = register;
		}
	}

	public final SensorReadRequest ultrasonicRequest, opticalRequest;

	public GearticksMRRangeSensor(I2cDevice device) {
		super(device);
		this.ultrasonicRequest = new SensorReadRequest(Register.ULTRASONIC.register, 1);
		this.opticalRequest = new SensorReadRequest(Register.OPTICAL.register, 1);
	}

	public double cmUltrasonic() {
		if (this.ultrasonicRequest.hasReadData()) {
			final byte ultrasonicByte = this.ultrasonicRequest.getReadData()[0];
			return ultrasonicByte & 0xFF;
		}
		else return NO_DATA;
	}
	public double cmOptical() {
		if (this.opticalRequest.hasReadData()) {
			final int opticalByte = this.opticalRequest.getReadData()[0] & 0xFF;
			if (opticalByte < MIN_VALID_OPTICAL_READING) return NO_DATA;
			return P_CONST * Math.log(Q_CONST * (R_CONST + opticalByte));
		}
		else return NO_DATA;
	}
}