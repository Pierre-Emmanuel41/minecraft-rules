package fr.pederobien.minecraft.rules.event;

import fr.pederobien.minecraft.rules.interfaces.IRuleList;

public class RuleListEvent extends ProjectRuleEvent {
	private IRuleList list;

	/**
	 * Creates a rule list event.
	 * 
	 * @param list The list source involved in this event.
	 */
	public RuleListEvent(IRuleList list) {
		this.list = list;
	}

	/**
	 * @return The list involved in this event.
	 */
	public IRuleList getList() {
		return list;
	}
}
