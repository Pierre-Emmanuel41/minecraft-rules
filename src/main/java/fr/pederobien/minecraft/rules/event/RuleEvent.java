package fr.pederobien.minecraft.rules.event;

import fr.pederobien.minecraft.rules.interfaces.IRule;

public class RuleEvent<T> extends ProjectRuleEvent {
	private IRule<T> rule;

	/**
	 * Creates a rule event.
	 * 
	 * @param rule The rule source involved in this event.
	 */
	public RuleEvent(IRule<T> rule) {
		this.rule = rule;
	}

	/**
	 * @return The rule involved in this event.
	 */
	public IRule<T> getRule() {
		return rule;
	}
}
