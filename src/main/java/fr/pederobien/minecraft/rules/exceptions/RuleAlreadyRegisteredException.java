package fr.pederobien.minecraft.rules.exceptions;

import fr.pederobien.minecraft.rules.interfaces.IRule;
import fr.pederobien.minecraft.rules.interfaces.IRuleList;

public class RuleAlreadyRegisteredException extends RuleListException {
	private static final long serialVersionUID = 1L;
	private IRule<?> rule;

	public RuleAlreadyRegisteredException(IRuleList list, IRule<?> rule) {
		super(String.format("The rule %s is already registered in %s", rule.getName(), list.getName()), list);
		this.rule = rule;
	}

	/**
	 * @return The already registered rule.
	 */
	public IRule<?> getRule() {
		return rule;
	}
}
