package org.gearticks.components.generic.component;

import android.util.Log;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.Utils;

/**
 * Allows for running any number of different components at once.
 * Won't quit until each internal component has quit.
 * If two components do conflicting actions, behavior is undefined.
 * This means, for example, that you can't do a drive and a turn at the same time,
 * but a drive and an intake and a servo movement at the same time are fine.
 */
public class ParallelComponent extends OpModeComponentAbstract<DefaultTransition> {
	private final Collection<OpModeComponent<?>> components;

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
	public ParallelComponent(Collection<OpModeComponent<?>> components) {
		super(DefaultTransition.class);
		this.components = components;
	}
	/**
	 * Creates a parallel component with the specified collection of components
	 * and the specified id
	 * @param components the components to run in parallel
	 * @param id the debugging id of the component
	 */
	public ParallelComponent(Collection<OpModeComponent<?>> components, String id) {
		super(DefaultTransition.class, id);
		this.components = components;
	}

	/**
	 * Adds a component to the collection of components to run in parallel
	 * @param component the component to add
	 */
	public void addComponent(OpModeComponent<?> component) {
		this.components.add(component);
	}

	@Override
	public void onMatchStart() {
		super.onMatchStart();
		this.components.forEach(OpModeComponent::onMatchStart);
	}
	@Override
	public void setup() {
		super.setup();
		this.components.forEach(OpModeComponent::setup);
	}
	@Override
	public DefaultTransition run() {
		final DefaultTransition superTransition = super.run();
		if (superTransition != null) return superTransition;

		final Iterator<OpModeComponent<?>> componentIterator = this.components.iterator();
		while (componentIterator.hasNext()) {
			final OpModeComponent<?> component = componentIterator.next();
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
		this.components.forEach(OpModeComponent::tearDown);
	}
	@Override
	public String getId() {
		final StringBuilder sb = new StringBuilder(super.getId());
		if (this.components.isEmpty()) sb.append(" - done");
		else {
			sb.append(" - in ");
			sb.append(
				this.components.stream()
					.map(OpModeComponent::getId)
					.collect(Collectors.joining(", "))
			);
		}
		return sb.toString();
	}
}