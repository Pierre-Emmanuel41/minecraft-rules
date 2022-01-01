package fr.pederobien.minecraft.rules.event;

import java.util.StringJoiner;

import fr.pederobien.minecraft.rules.interfaces.IRule;

public class RuleEnableChangePostEvent<T> extends RuleEvent<T> {
	private boolean oldEnable;

	/**
	 * Creates an event thrown when the rule enable property has changed.
	 * 
	 * @param rule      The rule whose the enable property has changed.
	 * @param oldEnable The old value of the enable property.
	 */
	public RuleEnableChangePostEvent(IRule<T> rule, boolean oldEnable) {
		super(rule);
		this.oldEnable = oldEnable;
	}

	/**
	 * @return THe old value of the enable property.
	 */
	public boolean getOldEnable() {
		return oldEnable;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		joiner.add("rule=" + getRule().getName());
		joiner.add("oldEnable=" + getOldEnable());
		joiner.add("newEnable" + getRule().isEnable());
		return String.format("%s_%s", getName(), joiner);
	}
}
