/*Allows for controlling an I2C device with multiple read and write registers
	Read and write actions are queued and will occur as soon as is possible
	Classes that extend this (presumably for a specific sensor) should make all their ReadRequest fields public and their WriteRequest fields private
	Sets of registers to be read need to be explicitly told to begin reading and stop reading
	The most recent data from a read request will be stored in it
*/
package org.gearticks.dimsensors.i2c;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public abstract class I2CSensor implements I2cController.I2cPortReadyCallback {
	//The maximum number of registers that a ReadRequest or WriteRequest can have
	private static final int MAX_LENGTH = 26;

	//A request for a read or write
	public abstract class SensorRequest {
		//The first register being controlled
		private final int register;
		//The number of consecutive registers being controlled
		private final int length;
		//For example, controlling registers 5 to 15 would require register = 5, length = 11

		//The value of System.nanoTime() when the action was performed
		private long actionTimestamp;

		public SensorRequest(int register, int length) {
			if (length < 1) throw new IllegalArgumentException("Cannot have a 0 length request");
			if (length > MAX_LENGTH) throw new IllegalArgumentException("Length must be no more than " + Integer.toString(MAX_LENGTH));
			this.register = register;
			this.length = length;
			this.resetActionTimer();
		}

		protected int getRegister() {
			return this.register;
		}
		protected int getLength() {
			return this.length;
		}
		//Send the request to the I2C controller
		public abstract void sendRequest();
		//Add the request to a different queue if it isn't yet there
		protected void addToQueue(Queue<SensorRequest> queue) {
			if (!queue.contains(this)) queue.add(this);
		}
		//Add the request to the end of the queue if it isn't yet there
		protected void addToQueue() {
			this.addToQueue(I2CSensor.this.requests);
		}
		//Reset the amount of time since the last action
		protected void resetActionTimer() {
			this.actionTimestamp = System.nanoTime();
		}
		//Returns the number of nanoseconds since the last action
		public long nanosSinceAction() {
			return System.nanoTime() - this.actionTimestamp;
		}
		//Get request in string form (for debugging)
		public String toString() {
			return "0x" + Integer.toHexString(this.register) + ", 0x" + Integer.toHexString(this.length);
		}
	}
	//A request for a read
	public class SensorReadRequest extends SensorRequest {
		//The last data read (null if no data has been read yet)
		private byte[] readData;

		public SensorReadRequest(int register, int length) {
			super(register, length);
			this.readData = null;
		}

		//Begin constantly polling these registers
		public void startReading() {
			this.addToQueue();
		}
		//Stop constantly polling these registers
		public void stopReading() {
			I2CSensor.this.requests.remove(this);
		}
		public void sendRequest() {
			I2CSensor.this.device.enableI2cReadMode(I2CSensor.this.getAddress(), this.getRegister(), this.getLength());
		}
		//Record new data that has been received
		public void setReadData() {
			this.resetActionTimer();
			this.readData = I2CSensor.this.device.getCopyOfReadBuffer();
		}
		//Whether we have gotten read data at least once
		public boolean hasReadData() {
			return this.readData != null;
		}
		//Get the data that has been read
		public byte[] getReadData() {
			return this.readData;
		}
	}
	//A request for a write
	public class SensorWriteRequest extends SensorRequest {
		//The data to be written
		private byte[] writeData;
		//Whether or not the most recent data have been sent
		private boolean sent;

		public SensorWriteRequest(int register, int length) {
			super(register, length);
			this.writeData = null;
			this.sent = true;
		}

		//Set the data to be written (returns whether or not it was different (and set))
		public boolean setWriteData(byte[] newData) {
			if (this.getLength() != newData.length) throw new IllegalArgumentException("Expected length " + Integer.toString(this.getLength()) + ", got " + Integer.toString(newData.length));
			if (Arrays.equals(this.writeData, newData)) return false;
			else {
				this.writeData = newData;
				this.sent = false;
				this.addToQueue();
				return true;
			}
		}
		public void sendRequest() {
			this.resetActionTimer();
			I2CSensor.this.device.enableI2cWriteMode(I2CSensor.this.getAddress(), this.getRegister(), this.writeData.length);
			I2CSensor.this.device.copyBufferIntoWriteBuffer(this.writeData);
			this.sent = true;
		}
		//Whether the most recent changed value has been sent
		public boolean wasSent() {
			return this.sent;
		}
	}
	//Automatically calculates the length necessary to read all the registers between the two specified ones (inclusive)
	protected SensorReadRequest makeReadRequest(int minRegister, int maxRegister) {
		return new SensorReadRequest(minRegister, maxRegister - minRegister + 1);
	}

	//The device being controlled (wrapped)
	private final I2cDevice device;
	//The queue of requests to be processed
	//Read requests cycle to the back of the queue when processed, while write requests are taken out
	protected final Queue<SensorRequest> requests;
	//The read request processed in the last ready cycle
	private SensorReadRequest lastRead;
	//The I2C address
	protected abstract I2cAddr getAddress();

	//Initialize without setting the callback - for use with multiple devices on the same I2C bus
	public I2CSensor(I2cDevice device, Queue<SensorRequest> requests) {
		this.device = device;
		this.requests = requests;
		this.lastRead = null;
	}
	public I2CSensor(I2cDevice device) {
		this(device, new ArrayDeque<SensorRequest>());
		this.device.registerForI2cPortReadyCallback(this);
	}
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
	//Can be overridden in subclasses to provide a callback function when an I2C request is completed
	protected void readyCallback() {}
	//Stop communicating on the port
	public void terminate() {
		this.requests.clear();
		this.device.deregisterForPortReadyCallback();
	}
}