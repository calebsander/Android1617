package org.gearticks.autonomous.generic.component;

import android.util.Log;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.gearticks.opmodes.utility.Utils;

@SuppressWarnings("Convert2streamapi")
public class ParallelComponent extends AutonomousComponentAbstractImpl {
	private final Collection<AutonomousComponent> components;

	public ParallelComponent() {
		this.components = new HashSet<>();
	}
	public ParallelComponent(String id) {
		super(id);
		this.components = new HashSet<>();
	}
	public ParallelComponent(Collection<AutonomousComponent> components) {
		this.components = components;
	}
	public ParallelComponent(Collection<AutonomousComponent> components, String id) {
		super(id);
		this.components = components;
	}

	public void addComponent(AutonomousComponent component) {
		this.components.add(component);
	}

	@Override
	public void onMatchStart() {
		super.onMatchStart();
		for (final AutonomousComponent component : this.components) {
			component.onMatchStart();
		}
	}
	@Override
	public void setup() {
		super.setup();
		for (final AutonomousComponent component : this.components) {
			component.setup();
		}
	}
	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		final Iterator<AutonomousComponent> componentIterator = this.components.iterator();
		while (componentIterator.hasNext()) {
			final AutonomousComponent component = componentIterator.next();
			if (component.run() != null) { //component has finished
				Log.i(Utils.TAG, "Component running in parallel ended: \"" + component + "\"");
				component.tearDown();
				componentIterator.remove(); //stop running this state
			}
		}
		if (this.components.isEmpty()) return NEXT_STATE; //once we are done with all states, quit
		else return null;
	}
	@Override
	public void tearDown() {
		super.tearDown();
		for (final AutonomousComponent component : this.components) {
			component.tearDown();
		}
	}
	@Override
	public String getId() {
		final StringBuilder sb = new StringBuilder(super.getId());
		if (this.components.isEmpty()) sb.append(" - done");
		else {
			sb.append(" - in");
			final int nonLastComponents = this.components.size() - 1;
			int element = 0;
			for (final AutonomousComponent component : this.components) {
				sb.append(' ');
				sb.append(component.getId());
				if (element < nonLastComponents) sb.append(" ,");
				element++;
			}
		}
		return sb.toString();
	}
}