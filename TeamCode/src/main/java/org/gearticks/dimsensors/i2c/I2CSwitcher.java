package org.gearticks.dimsensors.i2c;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

public class I2CSwitcher extends I2CSensor {
	//The number of ports that a device can be attached to
	private final static int PORTS = 8;

	protected I2cAddr getAddress() {
		return I2cAddr.create7bit(0x70);
	}

	protected final Queue<SensorRequest>[] portRequests;
	private final SensorWriteRequest[] switchRequests;
	private I2CSensor[] sensors;
	//Queue of ports to use; ports are cycled to the back once selected
	private Deque<Integer> portsInUse;
	private boolean hasSentSwitchRequest;
	//An object put in the requests queue to represent the end of requests for the current port
	private SensorRequest END_OF_PORT_REQUESTS;

	@SuppressWarnings("unchecked")
	public I2CSwitcher(I2cDevice device) {
		super(device);
		this.portRequests = new Queue[PORTS];
		this.switchRequests = new SensorWriteRequest[PORTS];
		this.sensors = new I2CSensor[PORTS];
		for (int i = 0; i < PORTS; i++) {
			this.portRequests[i] = new ArrayDeque<>();
			final int selectByte = 1 << i;
			final SensorWriteRequest switchRequest = new SensorWriteRequest(selectByte, 1); //might have to write a separate byte
			switchRequest.setWriteData(new byte[]{(byte)selectByte});
			this.requests.poll(); //undo adding switch request to the request queue
			this.switchRequests[i] = switchRequest;
		}
		this.portsInUse = new ArrayDeque<>();
		this.END_OF_PORT_REQUESTS = new SensorReadRequest(-1, -1);
	}

	private synchronized void switchPort() {
		final int port = this.portsInUse.poll();
		this.portsInUse.add(port);
		this.switchRequests[port].addToQueue();
		for (SensorRequest request : this.portRequests[port]) request.addToQueue(this.requests);
		this.requests.add(END_OF_PORT_REQUESTS); //marks the end of the commands for this port
		this.hasSentSwitchRequest = false;
	}
	protected synchronized void addDevice(int port, I2CSensor sensor) {
		this.sensors[port] = sensor;
		this.portsInUse.add(port);
		if (this.portsInUse.size() == 1) this.switchPort(); //if no port had been used yet, switch to the new one
	}
	protected synchronized void readyCallback() {
		System.out.print(this.requests.peek());
		System.out.print(" ");
		System.out.println(this.requests.size());
		final SensorRequest nextRequest = this.requests.peek();
		if (nextRequest == END_OF_PORT_REQUESTS) { //if we are done with this port, try to switch to the next one
			this.requests.poll(); //remove dummy request
			this.switchPort();
		}
		if (this.hasSentSwitchRequest) this.sensors[this.portsInUse.peekLast()].readyCallback(); //just communicated with the sub-device, so alert it
		else this.hasSentSwitchRequest = true; //finished sending switch request; onto sub-device requests
	}
}