package org.gearticks.dimsensors.i2c;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * Allows for controlling an I2C device with multiple read and write registers.
 * Read and write actions are queued and will occur as soon as possible.
 * Classes that extend this (presumably for a specific sensor) should
 * make all their ReadRequest fields public and their WriteRequest fields private.
 * There should be methods to convert bytes read into values and values into bytes written.
 * Sets of registers to be read need to be explicitly told to begin reading and stop reading.
 * The most recent data from a read request is stored in it.
 */
public abstract class I2CSensor implements I2cController.I2cPortReadyCallback {
	/**
	 * The maximum number of contiguous registers that a ReadRequest or WriteRequest can affect
	 */
	private static final int MAX_LENGTH = 26;

	/**
	 * A request for a read or write
	 */
	public abstract class SensorRequest {
		/**
		 * The first register being accessed
		 */
		private final int register;
		/**
		 * The number of contiguous registers being accessed
		 */
		private final int length;
		//For example, controlling registers 5 to 15 would require register = 5, length = 11

		/**
		 * The value of System.nanoTime() when the request was most recently performed
		 */
		private long actionTimestamp;

		/**
		 * Create a request window.
		 * Will access registers between <pre>register</pre> and <pre>register + length - 1</pre>.
		 * There must be between 1 and 26 registers in the request.
		 * @param register the lowest register to access
		 * @param length the number of contiguous registers to access
		 */
		public SensorRequest(int register, int length) {
			if (length < 1) throw new IllegalArgumentException("Cannot have a 0 length request");
			if (length > MAX_LENGTH) throw new IllegalArgumentException("Length must be no more than " + Integer.toString(MAX_LENGTH));
			this.register = register;
			this.length = length;
			this.resetActionTimer();
		}

		/**
		 * A getter method for register
		 * @return the value of register
		 * @see #register
		 */
		protected int getRegister() {
			return this.register;
		}
		/**
		 * A getter method for length
		 * @return the value of length
		 * @see #length
		 */
		protected int getLength() {
			return this.length;
		}
		/**
		 * Send the request to the I2C controller.
		 * Reads and writes do this differently, so they have separate implementations
		 */
		public abstract void sendRequest();
		/**
		 * Add the request to a request queue if it isn't yet there.
		 * Useful if referring to a different I2CSensor's queue.
		 * @param queue the queue to which to add the request
		 */
		protected void addToQueue(Queue<SensorRequest> queue) {
			if (!queue.contains(this)) queue.add(this);
		}
		/**
		 * Add the request to the end of this I2CSensor's queue if it isn't yet there
		 */
		protected void addToQueue() {
			this.addToQueue(I2CSensor.this.requests);
		}
		/**
		 * Reset the amount of elapsed time since the last action.
		 * Should be called whenever the request finishes.
		 */
		protected void resetActionTimer() {
			this.actionTimestamp = System.nanoTime();
		}
		/**
		 * Returns the number of nanoseconds since the last action
		 * @return the number of nanoseconds that have passed since the last request to these registers finished
		 */
		public long nanosSinceAction() {
			return System.nanoTime() - this.actionTimestamp;
		}
		public String toString() {
			return "0x" + Integer.toHexString(this.register) + ", 0x" + Integer.toHexString(this.length);
		}
	}
	/**
	 * A request for a read
	 */
	public class SensorReadRequest extends SensorRequest {
		/**
		 * The last data read (null if no data has been read yet).
		 * This is the only place where the results of I2C reads are stored.
		 */
		private byte[] readData;

		public SensorReadRequest(int register, int length) {
			super(register, length);
			this.readData = null;
		}

		/**
		 * Begin constantly polling these registers
		 */
		public void startReading() {
			this.addToQueue();
		}
		/**
		 * Stop constantly polling these registers
		 */
		public void stopReading() {
			I2CSensor.this.requests.remove(this);
		}
		public void sendRequest() {
			I2CSensor.this.device.enableI2cReadMode(I2CSensor.this.getAddress(), this.getRegister(), this.getLength());
		}
		/**
		 * Record new data that has been received.
		 * This must be done on the ready callback directly following
		 * the one on which the request was sent.
		 */
		public void setReadData() {
			this.resetActionTimer();
			this.readData = I2CSensor.this.device.getCopyOfReadBuffer();
		}
		/**
		 * Whether the request has returned data at least once
		 * @return whether cached data exists
		 */
		public boolean hasReadData() {
			return this.readData != null;
		}
		/**
		 * Get the data that has been read
		 * @return the bytes that were last read from these registers (byte 0 comes from the first register, and so on)
		 */
		public byte[] getReadData() {
			return this.readData;
		}
	}
	/**
	 * A request for a write
	 */
	public class SensorWriteRequest extends SensorRequest {
		/**
		 * The data to be written.
		 * Byte 0 will be written to the first register, and so on.
		 */
		private byte[] writeData;
		/**
		 * Whether the most recent data have been sent.
		 * Set to false when data is changed and back to true when the request is sent.
		 */
		private boolean sent;

		public SensorWriteRequest(int register, int length) {
			super(register, length);
			this.writeData = null;
			this.sent = true;
		}

		/**
		 * Set the data to be written.
		 * If it is unchanged, the request will not be added to the queue.
		 * @param newData the bytes to write (byte 0 will be written to the first register, and so on)
		 * @return returns whether or not it was different (and the request added)
		 */
		public boolean setWriteData(byte[] newData) {
			if (this.getLength() != newData.length) throw new IllegalArgumentException("Expected length " + Integer.toString(this.getLength()) + ", got " + Integer.toString(newData.length));
			if (Arrays.equals(this.writeData, newData)) return false;

			this.writeData = newData;
			this.sent = false;
			this.addToQueue();
			return true;
		}
		public void sendRequest() {
			this.resetActionTimer();
			I2CSensor.this.device.enableI2cWriteMode(I2CSensor.this.getAddress(), this.getRegister(), this.writeData.length);
			I2CSensor.this.device.copyBufferIntoWriteBuffer(this.writeData);
			this.sent = true;
		}

		/**
		 * Returns whether the most recent changed value has been sent
		 * @return whether the most recent changed value has been sent
		 */
		public boolean wasSent() {
			return this.sent;
		}
	}

	/**
	 * Automatically calculates the length necessary to read
	 * all the registers between the two specified ones (inclusive)
	 * @param minRegister the lowest register to read
	 * @param maxRegister the highest register to read
	 * @return a read request object that will read all registers between the two specified registers
	 */
	protected SensorReadRequest makeReadRequest(int minRegister, int maxRegister) {
		return new SensorReadRequest(minRegister, maxRegister - minRegister + 1);
	}

	/**
	 * The device being controlled.
	 * All communications are done through this device.
	 */
	private final I2cDevice device;
	/**
	 * The queue of requests to be processed
	 * Read requests cycle to the back of the queue when processed, while write requests are taken out
	 */
	protected final Queue<SensorRequest> requests;
	/**
	 * The read request processed in the last ready cycle.
	 * This is needed so the result of the read can be stored in it.
	 * If no read request was sent in the last cycle, this will be null.
	 */
	private SensorReadRequest lastRead;
	/**
	 * The I2C address of the sensor
	 * @return the I2C address to which to send all requests
	 */
	protected abstract I2cAddr getAddress();

	/**
	 * Initialize without setting the callback.
	 * Requests are put in an existing queue.
	 * This allows multiple I2CSensors to use the same I2cDevice.
	 * @param device the I2cDevice to send requests over
	 * @param requests the request queue to add all requests to
	 */
	public I2CSensor(I2cDevice device, Queue<SensorRequest> requests) {
		this.device = device;
		this.requests = requests;
		this.lastRead = null;
	}
	/**
	 * Initialize and register this I2CSensor as the sole receiver of ready callbacks
	 * @param device the I2cDevice to send requests over
	 */
	public I2CSensor(I2cDevice device) {
		this(device, new ArrayDeque<SensorRequest>());
		this.device.registerForI2cPortReadyCallback(this);
	}
	/**
	 * Initialize as a sensor attached to an I2CSwitcher.
	 * Uses the request queue provided by the I2CSwitcher
	 * and adds the device to the switcher.
	 * @param device the I2cDevice to send requests over
	 * @param switcher the I2C switcher to which this sensor is physically attached
	 * @param port the port on the switcher to which the sensor is attached
	 */
	public I2CSensor(I2cDevice device, I2CSwitcher switcher, int port) {
		this(device, switcher.portRequests[port]);
		switcher.addDevice(port, this);
	}

	public synchronized void portIsReady(int port) {
		if (this.lastRead != null) this.lastRead.setReadData(); //record the read data
		if (!this.requests.isEmpty()) { //if there are commands to process
			final SensorRequest nextRequest = this.requests.poll(); //get the request from the top of the queue
			nextRequest.sendRequest(); //run the code necessary to process the request
			this.device.setI2cPortActionFlag(); //we always want to do the action
			this.device.writeI2cCacheToController(); //set new desired register
			if (nextRequest instanceof SensorReadRequest) {
				nextRequest.addToQueue(); //reads need to be issued repeatedly
				this.device.readI2cCacheFromController(); //don't waste time performing a read if it isn't necessary
				this.lastRead = (SensorReadRequest)nextRequest; //a read was issued, so keep track of it in order to store the response data
			}
			else this.lastRead = null; //a write was issued, so no response has to be stored
		}
		this.readyCallback();
	}
	/**
	 * Called when an I2C request is completed.
	 * Can be overridden in subclasses to run some code when a request finishes.
	 */
	protected void readyCallback() {}
	/**
	 * Stop communicating on the port.
	 * Clears the request queue and deregisters from I2C ready callbacks.
	 */
	public void terminate() {
		this.requests.clear();
		if (this.device.getI2cPortReadyCallback() == this) {
			this.device.deregisterForPortReadyCallback();
		}
	}
}