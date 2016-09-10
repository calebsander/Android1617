/*Uses the I2CSensor interface to control a BNO055 Sensor
	Desired read/write commands must be told to occur; read values can then be pulled whenever desired
*/
package org.gearticks.dimsensors.i2c;

import com.qualcomm.robotcore.hardware.I2cDevice;

import java.nio.ByteBuffer;

public class BNO055 extends I2CSensor {
	private static final int DEFAULT_ADDRESS = 0x28;
	private static final int SECOND_ADDRESS = DEFAULT_ADDRESS + 1;
	private final int address;
	protected final int getAddress() {
		return this.address;
	}
	//I2C Registers
	private enum Register {
		//PAGE0 REGISTER DEFINITION START
		CHIP_ID(0x00),
		ACCEL_REV_ID(0x01),
		MAG_REV_ID(0x02),
		GYRO_REV_ID(0x03),
		SW_REV_ID_LSB(0x04),
		SW_REV_ID_MSB(0x05),
		BL_REV_ID(0x06),

		//Page id register definition
		PAGE_ID(0x07),

		//Accel data registers
		ACCEL_DATA_X_LSB(0x08),
		ACCEL_DATA_X_MSB(0x09),
		ACCEL_DATA_Y_LSB(0x0A),
		ACCEL_DATA_Y_MSB(0x0B),
		ACCEL_DATA_Z_LSB(0x0C),
		ACCEL_DATA_Z_MSB(0x0D),

		//Mag data registers
		MAG_DATA_X_LSB(0x0E),
		MAG_DATA_X_MSB(0x0F),
		MAG_DATA_Y_LSB(0x10),
		MAG_DATA_Y_MSB(0x11),
		MAG_DATA_Z_LSB(0x12),
		MAG_DATA_Z_MSB(0x13),

		//Gyro data registers
		GYRO_DATA_X_LSB(0x14),
		GYRO_DATA_X_MSB(0x15),
		GYRO_DATA_Y_LSB(0x16),
		GYRO_DATA_Y_MSB(0x17),
		GYRO_DATA_Z_LSB(0x18),
		GYRO_DATA_Z_MSB(0x19),

		//Euler data registers
		EULER_H_LSB(0x1A),
		EULER_H_MSB(0x1B),
		EULER_R_LSB(0x1C),
		EULER_R_MSB(0x1D),
		EULER_P_LSB(0x1E),
		EULER_P_MSB(0x1F),

		//Quaternion data registers
		QUATERNION_DATA_W_LSB(0x20),
		QUATERNION_DATA_W_MSB(0x21),
		QUATERNION_DATA_X_LSB(0x22),
		QUATERNION_DATA_X_MSB(0x23),
		QUATERNION_DATA_Y_LSB(0x24),
		QUATERNION_DATA_Y_MSB(0x25),
		QUATERNION_DATA_Z_LSB(0x26),
		QUATERNION_DATA_Z_MSB(0x27),

		//Linear acceleration data registers
		LINEAR_ACCEL_DATA_X_LSB(0x28),
		LINEAR_ACCEL_DATA_X_MSB(0x29),
		LINEAR_ACCEL_DATA_Y_LSB(0x2A),
		LINEAR_ACCEL_DATA_Y_MSB(0x2B),
		LINEAR_ACCEL_DATA_Z_LSB(0x2C),
		LINEAR_ACCEL_DATA_Z_MSB(0x2D),

		//Gravity data registers
		GRAVITY_DATA_X_LSB(0x2E),
		GRAVITY_DATA_X_MSB(0x2F),
		GRAVITY_DATA_Y_LSB(0x30),
		GRAVITY_DATA_Y_MSB(0x31),
		GRAVITY_DATA_Z_LSB(0x32),
		GRAVITY_DATA_Z_MSB(0x33),

		//Temperature data register
		TEMP(0x34),

		//Status registers
		CALIB_STAT(0x35),
		SELF_TEST_RESULT(0x36),
		INTR_STAT(0x37),
		SYS_CLK_STAT(0x38),
		SYS_STAT(0x39),
		SYS_ERR(0x3A),

		//Unit selection register
		UNIT_SEL(0x3B),
		DATA_SELECT(0x3C),

		//Mode registers
		OPR_MODE(0x3D),
		PWR_MODE(0x3E),
		SYS_TRIGGER(0x3F),
		TEMP_SOURCE(0x40),

		//Axis remap registers
		AXIS_MAP_CONFIG(0x41),
		AXIS_MAP_SIGN(0x42),

		//SIC registers
		SIC_MATRIX_0_LSB(0x43),
		SIC_MATRIX_0_MSB(0x44),
		SIC_MATRIX_1_LSB(0x45),
		SIC_MATRIX_1_MSB(0x46),
		SIC_MATRIX_2_LSB(0x47),
		SIC_MATRIX_2_MSB(0x48),
		SIC_MATRIX_3_LSB(0x49),
		SIC_MATRIX_3_MSB(0x4A),
		SIC_MATRIX_4_LSB(0x4B),
		SIC_MATRIX_4_MSB(0x4C),
		SIC_MATRIX_5_LSB(0x4D),
		SIC_MATRIX_5_MSB(0x4E),
		SIC_MATRIX_6_LSB(0x4F),
		SIC_MATRIX_6_MSB(0x50),
		SIC_MATRIX_7_LSB(0x51),
		SIC_MATRIX_7_MSB(0x52),
		SIC_MATRIX_8_LSB(0x53),
		SIC_MATRIX_8_MSB(0x54),

		//Accelerometer Offset registers
		ACCEL_OFFSET_X_LSB(0x55),
		ACCEL_OFFSET_X_MSB(0x56),
		ACCEL_OFFSET_Y_LSB(0x57),
		ACCEL_OFFSET_Y_MSB(0x58),
		ACCEL_OFFSET_Z_LSB(0x59),
		ACCEL_OFFSET_Z_MSB(0x5A),

		//Magnetometer Offset registers
		MAG_OFFSET_X_LSB(0x5B),
		MAG_OFFSET_X_MSB(0x5C),
		MAG_OFFSET_Y_LSB(0x5D),
		MAG_OFFSET_Y_MSB(0x5E),
		MAG_OFFSET_Z_LSB(0x5F),
		MAG_OFFSET_Z_MSB(0x60),

		//Gyroscope Offset registers
		GYRO_OFFSET_X_LSB(0x61),
		GYRO_OFFSET_X_MSB(0x62),
		GYRO_OFFSET_Y_LSB(0x63),
		GYRO_OFFSET_Y_MSB(0x64),
		GYRO_OFFSET_Z_LSB(0x65),
		GYRO_OFFSET_Z_MSB(0x66),

		//Radius registers
		ACCEL_RADIUS_LSB(0x67),
		ACCEL_RADIUS_MSB(0x68),
		MAG_RADIUS_LSB(0x69),
		MAG_RADIUS_MSB(0x6A);

		private final int register;
		Register(int register) {
			this.register = register;
		}
		public int getRegister() {
			return this.register;
		}
	}
	//Values for the operation mode register
	private enum BNO055OperationMode {
		CONFIG(0x00),
		ACCONLY(0x01),
		MAGONLY(0x02),
		GYRONLY(0x03),
		ACCMAG(0x04),
		ACCGYRO(0x05),
		MAGGYRO(0x06),
		AMG(0x07),
		IMU(0x08),
		COMPASS(0x09),
		M4G(0x0A),
		NDOF_FMC_OFF(0x0B),
		NDOF(0x0C);

		private final byte mode;
		BNO055OperationMode(int mode) {
			this.mode = (byte)mode;
		}
		public byte getMode() {
			return this.mode;
		}
	}
	//Values for the power register
	private enum BNO055PowerMode {
		NORMAL(0x00),
		LOW_POWER(0x01),
		SUSPEND(0x02);

		private final byte mode;
		BNO055PowerMode(int mode) {
			this.mode = (byte)mode;
		}
		public byte getMode() {
			return this.mode;
		}
	}
	//A yaw, pitch, roll angle returned from the sensor
	//Objects of this class are immutable, so the fields are public instead of using a getter method
	public static class EulerAngle {
		public final double yaw, pitch, roll;

		public EulerAngle(double yaw, double pitch, double roll) {
			this.yaw = yaw;
			this.pitch = pitch;
			this.roll = roll;
		}

		public String toString() { //useful for logging to Telemetry
			return String.format("%.2f, %.2f, %.2f", this.yaw, this.pitch, this.roll);
		}
		//Returns whether all the have a weird value - indicative of a sensor connection issue
		public boolean seemsInvalid() {
			//0.0 occurs if all registers have the value 0x00 and -0.0625 occurs when they are all 0xFF
			return (this.yaw == 0.0 || this.yaw == -0.0625) && (this.pitch == 0.0 || this.pitch == -0.0625) && (this.roll == 0.0 || this.roll == -0.0625);
		}
	}
	//Values for the register that contains information about errors that have occurred
	public enum BNO055Error {
		//See https://github.com/adafruit/Adafruit_BNO055/blob/master/Adafruit_BNO055.cpp#L180 for a fuller description
		NONE(0),
		PERIPHERAL(1),
		INIT(2),
		SELF_TEST(3),
		VALUE_OUT_OF_RANGE(4),
		ADDRESS_OUT_OF_RANGE(5),
		WRITE(6),
		LOW_POWER_UNAVAILABLE(7),
		ACCELEROMETER_POWER(8),
		FUSION(9),
		CONFIG(10);

		private final byte errorCode;
		BNO055Error(int errorCode) {
			this.errorCode = (byte)errorCode;
		}
		public byte getErrorCode() {
			return this.errorCode;
		}
	}
	//ID fields returned by the sensor
	//Objects of this class are immutable, so the fields are public instead of using a getter method
	public static class SensorIDs {
		public final byte chipID;
		public final byte accelID, magID, gyroID;
		public final short softwareID;
		public final byte bootloaderID;

		public SensorIDs(byte chipID, byte accelID, byte magID, byte gyroID, short softwareID, byte bootloaderID) {
			this.chipID = chipID;
			this.accelID = accelID;
			this.magID = magID;
			this.gyroID = gyroID;
			this.softwareID = softwareID;
			this.bootloaderID = bootloaderID;
		}

		public String toString() {
			return String.format("0x%x, 0x%x, 0x%x, 0x%x, 0x%x, 0x%x", this.chipID, this.accelID, this.magID, this.gyroID, this.softwareID, this.bootloaderID);
		}
	}

	//The important read and write requests (stores registers and read/write data)
	public final SensorReadRequest idRequest;
	public final SensorReadRequest calibrationRequest;
	public final SensorReadRequest eulerRequest;
	public final SensorReadRequest errorRequest;
	public final SensorWriteRequest modeRequest;
	public final SensorWriteRequest powerModeRequest;
	//The value of the yaw when it was last reset
	private double resetYawPoint;

	public BNO055(I2cDevice device) {
		this(device, false);
	}
	public BNO055(I2cDevice device, boolean addrPin) {
		super(device);
		this.idRequest = this.makeReadRequest(Register.CHIP_ID.getRegister(), Register.BL_REV_ID.getRegister());
		this.calibrationRequest = this.makeReadRequest(Register.CALIB_STAT.getRegister(), Register.SELF_TEST_RESULT.getRegister());
		this.eulerRequest = this.makeReadRequest(Register.EULER_H_LSB.getRegister(), Register.EULER_P_MSB.getRegister());
		this.errorRequest = new SensorReadRequest(Register.SYS_ERR.getRegister(), 1);
		this.modeRequest = new SensorWriteRequest(Register.OPR_MODE.getRegister(), 1);
		this.powerModeRequest = new SensorWriteRequest(Register.PWR_MODE.getRegister(), 1);
		this.resetYawPoint = 0.0;
		if (addrPin) this.address = SECOND_ADDRESS;
		else this.address = DEFAULT_ADDRESS;

		//This mode will run fusion on the accelerometer and gyro data but not use the compass (for fear of interference)
		this.setMode(BNO055OperationMode.IMU);
	}

	//Queues a request to set the operation mode
	public void setMode(BNO055OperationMode mode) {
		this.modeRequest.setWriteData(new byte[]{mode.getMode()});
	}
	//Queues a request to set the power mode
	public void setPowerMode(BNO055PowerMode mode) {
		this.powerModeRequest.setWriteData(new byte[]{mode.getMode()});
	}

	//READ REQUEST METHODS - methods to extract data from most recent read

	//For IDs/version numbers of the components
	public SensorIDs getIDs() {
		if (this.idRequest.hasReadData()) {
			final byte[] DATA = this.idRequest.getReadData();
			final byte chipID = DATA[Register.CHIP_ID.getRegister() - this.idRequest.getRegister()];
			final byte accelID = DATA[Register.ACCEL_REV_ID.getRegister() - this.idRequest.getRegister()];
			final byte magID = DATA[Register.MAG_REV_ID.getRegister() - this.idRequest.getRegister()];
			final byte gyroID = DATA[Register.GYRO_REV_ID.getRegister() - this.idRequest.getRegister()];
			final ByteBuffer softwareID = ByteBuffer.wrap(new byte[]{DATA[Register.SW_REV_ID_MSB.getRegister() - this.idRequest.getRegister()], DATA[Register.SW_REV_ID_LSB.getRegister() - this.idRequest.getRegister()]});
			final byte bootloaderID = DATA[Register.BL_REV_ID.getRegister() - this.idRequest.getRegister()];
			return new SensorIDs(chipID, accelID, magID, gyroID, softwareID.getShort(), bootloaderID);
		}
		else return null;
	}

	//For whether the sensor has calibrated
	//Returns whether or not the gyro reports being calibrated
	//Can be used to determine whether the sensor as a whole is calibrated
	public boolean gyroCalibrated() {
		return this.calibrationRequest.hasReadData() &&
						((this.calibrationRequest.getReadData()[Register.CALIB_STAT.getRegister() - this.calibrationRequest.getRegister()] >> 4) & 0x03) == 0x03;
	}
	public boolean selfTestResult() {
		return this.calibrationRequest.hasReadData() &&
						(this.calibrationRequest.getReadData()[Register.SELF_TEST_RESULT.getRegister() - this.calibrationRequest.getRegister()] & 0x0F) == 0x0F;
	}

	//For the heading reported by the sensor - make sure heading data has been read, or you risk a NullPointerException
	public EulerAngle getHeading() {
		if (this.eulerRequest.hasReadData()) {
			final int REGISTER_OFFSET = this.eulerRequest.getRegister();
			final int Y_L = Register.EULER_H_LSB.getRegister() - REGISTER_OFFSET;
			final int Y_M = Register.EULER_H_MSB.getRegister() - REGISTER_OFFSET;
			final int P_L = Register.EULER_P_LSB.getRegister() - REGISTER_OFFSET;
			final int P_M = Register.EULER_P_MSB.getRegister() - REGISTER_OFFSET;
			final int R_L = Register.EULER_R_LSB.getRegister() - REGISTER_OFFSET;
			final int R_M = Register.EULER_R_MSB.getRegister() - REGISTER_OFFSET;
			final byte[] data = this.eulerRequest.getReadData();
			if (data.length == 0) return new EulerAngle(0.0, 0.0, 0.0);
			else {
				final ByteBuffer yawBuffer = ByteBuffer.wrap(new byte[]{data[Y_M], data[Y_L]});
				final ByteBuffer pitchBuffer = ByteBuffer.wrap(new byte[]{data[P_M], data[P_L]});
				final ByteBuffer rollBuffer = ByteBuffer.wrap(new byte[]{data[R_M], data[R_L]});
				return new EulerAngle(
								yawBuffer.getShort() / 16.0,
								pitchBuffer.getShort() / 16.0,
								rollBuffer.getShort() / 16.0
				);
			}
		}
		else return null;
	}
	//Returns the yaw relative to the reset point
	public double getRelativeYaw() {
		final EulerAngle heading = this.getHeading();
		if (heading == null) return 0.0;
		else return (heading.yaw - this.resetYawPoint + 360.0) % 360.0;
	}
	//Resets the 0 yaw point to the current heading
	//Will throw a NullPointerException if this.eulerRequest.hasReadData() == false
	public void resetHeading() {
		this.resetYawPoint = this.getHeading().yaw;
	}
	//Returns which (if any) error has occurred on the sensor
	//Having an error doesn't seem to prevent it from continuing to work fine
	public BNO055Error getError() {
		final byte code;
		if (this.errorRequest.hasReadData()) code = this.errorRequest.getReadData()[0];
		else code = 0x00;
		for (BNO055Error error : BNO055Error.values()) {
			if (error.getErrorCode() == code) return error;
		}
		return BNO055Error.NONE;
	}
	//Gets the value of the yaw reset point
	public double getResetYawPoint() {
		return this.resetYawPoint;
	}
	public void setResetYawPoint(double resetYawPoint) {
		this.resetYawPoint = resetYawPoint;
	}
}