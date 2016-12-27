package org.gearticks.autonomous.generic.statemachine.network;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.gearticks.autonomous.generic.component.AutonomousComponent;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * A StateMachineFullImpl itself implements an AutonomousComponent, i.e. this supports hierarchical state-machines.
 * Internally, a StateMachineFullImpl contains InputPorts, AutonomousComponents, StateMachineConnections and OutputPorts
 * (where Input- and OutputPorts are AutonomousComponents)
 * StateMachineConnections connect between an output-port of one AutonomousComponent to the input-port of another AutonomousComponent.
 * 
 * A StateMachineFullImpl uses the specialized Input- and OutputPort AutonomousComponents to implement the ports,
 * but in general for a custom AutonomousComponent this is not necessary.
 * 
 * The use of Input- and OutputPort AutonomousComponents allows the StateMachineFullImpl to have an internal network of AutonomousComponents.
 *
 */
public class StateMachineFullImpl implements AutonomousComponent{
	protected AutonomousComponent currentState = null;
	
	protected Map<Integer, InputPort> inputPorts = new HashMap<>();
	protected Map<Integer, OutputPort> outputPorts = new HashMap<>();
	
	protected Set<AutonomousComponent> components = new HashSet<>();
	//protected Map<AutonomousComponent, Map<Integer, AutonomousComponent>> outputConnections;
	protected Table<AutonomousComponent, Integer, StateMachineConnection> outputConnections = HashBasedTable.create();
	
	public StateMachineFullImpl(int numInputPorts, int numOutputPorts) {
		super();
		
		assert numInputPorts >= 1;
		assert numOutputPorts >= 1;
		
		for (int i = 1; i <= numInputPorts; i++){
			this.inputPorts.put(i, new InputPort(i));
		}
		
		for (int j = 1; j <= numOutputPorts; j++){
			this.outputPorts.put(j, new OutputPort(j));
		}
	}
	
	public void addComponent(AutonomousComponent ac){
		this.components.add(ac);
	}
	public void addComponents(Collection<AutonomousComponent> components){
		this.components.addAll(components);
	}
	
	public void addConnection(AutonomousComponent originComponent,
			int originPortNumber, AutonomousComponent destinationComponent,
			int destinationPortNumber){
		assert originComponent != null;
		assert destinationComponent != null;
		
		//TODO: check that ac in components set?
		
		StateMachineConnection connection = new StateMachineConnection(originComponent, originPortNumber, destinationComponent, destinationPortNumber);
		//TODO: check if there already exists a connection from the output port
		if (!this.outputConnections.contains(originComponent, originPortNumber)){
			this.outputConnections.put(originComponent, originPortNumber, connection);
		}
		else {
			//log warning/error?
		}
		
	}
	
	@Override
	public void initialize() {
		for (AutonomousComponent ac : this.components){
			ac.initialize();
		}
	}

	@Override
	public void setup(int inputPort) {
		InputPort input = this.inputPorts.get(inputPort);
		if (input == null){
			//throw some exception
			//TODO: define proper exception
			throw new RuntimeException("Unknown input port number");
		}
		this.currentState = input;
	}
	
	public void setup() {
		this.setup(1);
	}

	@Override
	public int run() {
		int outputPortNumber = 0;
		
		if (this.currentState == null){
			//throw some exception
			return 0;
		}
		
		
		//Exit StateMachineFullImpl if in output port
		if (this.currentState instanceof OutputPort){
			outputPortNumber = ((OutputPort) this.currentState).getPortNumber();
		}
		else {
			//regular 'run':
			int transition = this.currentState.run();

			//Check for transition:
			if (transition > 0){
				//Find next component
				StateMachineConnection connection = this.outputConnections.get(this.currentState, transition);
				if (connection != null){
					this.currentState.tearDown();
					this.getLogger().info("Transition from " + this.currentState + " to " + connection.getDestinationComponent() + " port " + connection.getDestinationPortNumber());
					this.currentState = connection.getDestinationComponent();
					this.currentState.setup(connection.getDestinationPortNumber());

				}
				else {
					//no connection: log warning?
					//set to default outputport? Or keep StateMachineFullImpl in this state forever?
					this.getLogger().warning("ERROR: Transition from " + this.currentState + " to transition # "+ transition + ". No connection found.");
				}
			}
		}
		
		return outputPortNumber;
	}

	@Override
	public void tearDown() {
		this.currentState = null;
	}
	
	public InputPort getInputPort(int portNumber){
		return this.inputPorts.get(portNumber);
	}
	
	public OutputPort getOutputPort(int portNumber){
		return this.outputPorts.get(portNumber);
	}
	
	public Logger getLogger(){
		return Logger.getLogger(this.getClass().getSimpleName());
	}
	
	
}
