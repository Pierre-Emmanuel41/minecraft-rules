package fr.pederobien.minecraft.rules.exceptions;

import fr.pederobien.minecraft.rules.interfaces.IRuleList;

public class RuleListException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private IRuleList list;

	public RuleListException(String message, IRuleList list) {
		super(message);
		this.list = list;
	}

	/**
	 * @return The list involved in this exception.
	 */
	public IRuleList getList() {
		return list;
	}
}
