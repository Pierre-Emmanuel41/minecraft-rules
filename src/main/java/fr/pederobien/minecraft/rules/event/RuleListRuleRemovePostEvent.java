package fr.pederobien.minecraft.rules.event;

import java.util.StringJoiner;

import fr.pederobien.minecraft.rules.interfaces.IRule;
import fr.pederobien.minecraft.rules.interfaces.IRuleList;

public class RuleListRuleRemovePostEvent extends RuleListEvent {
	private IRule<?> rule;

	/**
	 * Creates an event thrown when a rule is removed from a list.
	 * 
	 * @param list The list from which a rule has been removed.
	 * @param rule The removed rule.
	 */
	public RuleListRuleRemovePostEvent(IRuleList list, IRule<?> rule) {
		super(list);
		this.rule = rule;
	}

	/**
	 * @return The removed rule.
	 */
	public IRule<?> getRule() {
		return rule;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		joiner.add("list=" + getList().getName());
		joiner.add("rule=" + getRule().getName());
		return String.format("%s_%s", getName(), joiner);
	}
}
