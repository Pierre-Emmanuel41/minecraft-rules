package fr.pederobien.minecraft.rules.exceptions;

import fr.pederobien.minecraft.rules.interfaces.IEntityTypeList;

public class EntityTypeListException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private IEntityTypeList list;

	public EntityTypeListException(String message, IEntityTypeList list) {
		super(message);
		this.list = list;
	}

	/**
	 * @return The list involved in this exception.
	 */
	public IEntityTypeList getList() {
		return list;
	}
}
