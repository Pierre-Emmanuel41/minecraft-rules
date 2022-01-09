package fr.pederobien.minecraft.rules.event;

import java.util.StringJoiner;

import fr.pederobien.minecraft.rules.interfaces.IRule;
import fr.pederobien.minecraft.rules.interfaces.IRuleList;

public class RuleListRuleAddPostEvent extends RuleListEvent {
	private IRule<?> rule;

	/**
	 * Creates an event thrown when a rule is added to a list.
	 * 
	 * @param list The list to which a rule has been added.
	 * @param rule The added rule.
	 */
	public RuleListRuleAddPostEvent(IRuleList list, IRule<?> rule) {
		super(list);
		this.rule = rule;
	}

	/**
	 * @return The added rule.
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
