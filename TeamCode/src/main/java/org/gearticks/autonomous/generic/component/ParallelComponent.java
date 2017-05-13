package org.gearticks.autonomous.generic.component;

import android.util.Log;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.Utils;

/**
 * Allows for running any number of different components at once.
 * Won't quit until each internal component has quit.
 * If two components do conflicting actions, behavior is undefined.
 * This means, for example, that you can't do a drive and a turn at the same time,
 * but a drive and an intake and a servo movement at the same time are fine.
 */
@SuppressWarnings("Convert2streamapi")
public class ParallelComponent extends AutonomousComponentAbstractImpl<DefaultTransition> {
	private final Collection<AutonomousComponent<?>> components;

	/**
	 * Creates a parallel component with an empty set of components
	 */
	public ParallelComponent() {
		this(new HashSet<>());
	}
	/**
	 * Creates a parallel component with an empty set of components
	 * and the specified id
	 * @param id the debugging id of the component
	 */
	public ParallelComponent(String id) {
		this(new HashSet<>(), id);
	}
	/**
	 * Creates a parallel component with the specified collection of components
	 * @param components the components to run in parallel
	 */
	public ParallelComponent(Collection<AutonomousComponent<?>> components) {
		super(DefaultTransition.class);
		this.components = components;
	}
	/**
	 * Creates a parallel component with the specified collection of components
	 * and the specified id
	 * @param components the components to run in parallel
	 * @param id the debugging id of the component
	 */
	public ParallelComponent(Collection<AutonomousComponent<?>> components, String id) {
		super(DefaultTransition.class, id);
		this.components = components;
	}

	/**
	 * Adds a component to the collection of components to run in parallel
	 * @param component the component to add
	 */
	public void addComponent(AutonomousComponent<?> component) {
		this.components.add(component);
	}

	@Override
	public void onMatchStart() {
		super.onMatchStart();
		for (final AutonomousComponent<?> component : this.components) {
			component.onMatchStart();
		}
	}
	@Override
	public void setup() {
		super.setup();
		for (final AutonomousComponent<?> component : this.components) {
			component.setup();
		}
	}
	@Override
	public DefaultTransition run() {
		final DefaultTransition superTransition = super.run();
		if (superTransition != null) return superTransition;

		final Iterator<AutonomousComponent<?>> componentIterator = this.components.iterator();
		while (componentIterator.hasNext()) {
			final AutonomousComponent<?> component = componentIterator.next();
			if (component.run() != null) { //component has finished
				Log.i(Utils.TAG, "Component running in parallel ended: \"" + component + "\"");
				component.tearDown();
				componentIterator.remove(); //stop running this state
			}
		}
		if (this.components.isEmpty()) return DefaultTransition.DEFAULT; //once we are done with all states, quit
		else return null;
	}
	@Override
	public void tearDown() {
		super.tearDown();
		for (final AutonomousComponent<?> component : this.components) {
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
			for (final AutonomousComponent<?> component : this.components) {
				sb.append(' ');
				sb.append(component.getId());
				if (element < nonLastComponents) sb.append(" ,");
				element++;
			}
		}
		return sb.toString();
	}
}