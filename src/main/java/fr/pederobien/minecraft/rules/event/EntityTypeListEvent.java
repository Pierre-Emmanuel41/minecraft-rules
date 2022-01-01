package fr.pederobien.minecraft.rules.event;

import fr.pederobien.minecraft.rules.interfaces.IEntityTypeList;

public class EntityTypeListEvent extends ProjectRuleEvent {
	private IEntityTypeList list;

	/**
	 * Creates an entity type list event.
	 * 
	 * @param list The list source involved in this event.
	 */
	public EntityTypeListEvent(IEntityTypeList list) {
		this.list = list;
	}

	/**
	 * @return The list involved in this event.
	 */
	public IEntityTypeList getList() {
		return list;
	}
}
