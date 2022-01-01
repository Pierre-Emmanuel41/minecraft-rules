package fr.pederobien.minecraft.rules.event;

import java.util.StringJoiner;

import fr.pederobien.minecraft.rules.interfaces.IRule;

public class RuleChangePostEvent<T> extends RuleEvent<T> {
	private T oldValue;

	/**
	 * Creates an event thrown when the value of a game rule has changed.
	 * 
	 * @param rule     The rule whose the current value has changed.
	 * @param oldValue The old game rule value.
	 */
	public RuleChangePostEvent(IRule<T> rule, T oldValue) {
		super(rule);
		this.oldValue = oldValue;
	}

	/**
	 * @return The old game rule value.
	 */
	public T getOldValue() {
		return oldValue;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		joiner.add("rule=" + getRule().getName());
		joiner.add("oldValue=" + getOldValue());
		joiner.add("newValue=" + getRule().getValue());
		return String.format("%s_%s", getName(), joiner);
	}
}
