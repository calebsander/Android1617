package org.gearticks.dimsensors.i2c;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/*
General implementation:
Attached (child) sensors use mostly the normal I2CSensor interface.
The switcher and all child sensors use the same I2cDevice, however
only the switcher listens to the I2C port-ready callbacks.
When switching to a different port, the switch request is added to the switcher's queue
and the target child's requests are copied into the switcher's queue.
A dummy request is added to the queue to mark the end of the child's communication.
When the children's requests are sent or read into, they automatically work
as though they were issued straight to the child without the switcher as intermediary.
*/
public class I2CSwitcher extends I2CSensor {
	//The number of ports that a device can be attached to
	private final static int PORTS = 8;

	protected I2cAddr getAddress() {
		return I2cAddr.create7bit(0x70);
	}

	//Queues for each child sensor to add their requests to
	protected final Queue<SensorRequest>[] portRequests;
	//Contains a request for each of the PORTS ports that will connect it and no others to the main bus
	private final SensorWriteRequest[] switchRequests;
	//The child sensors attached to the switcher
	private final I2CSensor[] sensors;
	//Queue of ports to use; ports are cycled to the back once selected
	private final Deque<Integer> portsInUse;
	//Whether the DIM has responded that the switcher has switched
	private boolean hasSentSwitchRequest;
	//An object put in the requests queue to represent the end of requests for the current port
	//Never actually sent as a request
	private final SensorRequest END_OF_PORT_REQUESTS;

	@SuppressWarnings("unchecked")
	public I2CSwitcher(I2cDevice device) {
		super(device);
		this.portRequests = new Queue[PORTS];
		this.switchRequests = new SensorWriteRequest[PORTS];
		this.sensors = new I2CSensor[PORTS];
		for (int i = 0; i < PORTS; i++) {
			this.portRequests[i] = new ArrayDeque<>();
			final int selectByte = 1 << i; //bitmask to enable only port i
			final SensorWriteRequest switchRequest = new SensorWriteRequest(selectByte, 1);
			switchRequest.setWriteData(new byte[]{(byte)selectByte});
			this.requests.poll(); //undo adding switch request to the request queue
			this.switchRequests[i] = switchRequest;
		}
		this.portsInUse = new ArrayDeque<>();
		this.END_OF_PORT_REQUESTS = new SensorReadRequest(-1, 1); //no real significance
	}

	//Switch to the next requested port
	//Add the switch request and all the child sensor's requests to the main queue
	private synchronized void switchPort() {
		final int port = this.portsInUse.poll();
		this.portsInUse.add(port); //add port back to end of queue so it will get switched to eventually
		this.switchRequests[port].addToQueue();
		final Queue<SensorRequest> portRequests = this.portRequests[port];
		//Add each of child's requests to the main queue, then clear it
		portRequests.forEach(request -> request.addToQueue(this.requests));
		portRequests.clear();
		this.requests.add(END_OF_PORT_REQUESTS); //marks the end of the commands for this port
		this.hasSentSwitchRequest = false; //switch has not yet been issued
	}
	//Register a new attached device so it will get switched to periodically
	protected synchronized void addDevice(int port, I2CSensor sensor) {
		this.sensors[port] = sensor;
		this.portsInUse.add(port);
		if (this.portsInUse.size() == 1) this.switchPort(); //if no port had been used yet, switch to the new one
	}
	protected synchronized void readyCallback() {
		if (this.hasSentSwitchRequest) this.sensors[this.portsInUse.peekLast()].readyCallback(); //just communicated with a child sensor, so alert it
		else this.hasSentSwitchRequest = true; //finished sending switch request; onto child requests
		final SensorRequest nextRequest = this.requests.peek();
		if (nextRequest == END_OF_PORT_REQUESTS) { //if we are done with this port, try to switch to the next one
			this.requests.poll(); //remove dummy request
			this.switchPort();
		}
	}
}