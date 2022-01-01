package fr.pederobien.minecraft.rules.event;

import java.util.StringJoiner;

import org.bukkit.entity.EntityType;

import fr.pederobien.minecraft.rules.interfaces.IEntityTypeList;

public class EntityTypeAddPostEvent extends EntityTypeListEvent {
	private EntityType type;

	/**
	 * Creates an event thrown when an entity type has been added to a entity type list.
	 * 
	 * @param list The list to which an entity type has been added.
	 * @param type The added type.
	 */
	public EntityTypeAddPostEvent(IEntityTypeList list, EntityType type) {
		super(list);
		this.type = type;
	}

	/**
	 * @return The added type.
	 */
	public EntityType getType() {
		return type;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		joiner.add("list=" + getList().getName());
		joiner.add("type=" + getType().name());
		return String.format("%s_%s", getName(), joiner);
	}
}
