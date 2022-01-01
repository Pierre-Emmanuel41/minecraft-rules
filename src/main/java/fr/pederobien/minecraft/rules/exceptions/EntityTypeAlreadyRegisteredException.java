package fr.pederobien.minecraft.rules.exceptions;

import org.bukkit.entity.EntityType;

import fr.pederobien.minecraft.rules.interfaces.IEntityTypeList;

public class EntityTypeAlreadyRegisteredException extends EntityTypeListException {
	private static final long serialVersionUID = 1L;
	private EntityType type;

	public EntityTypeAlreadyRegisteredException(IEntityTypeList list, EntityType type) {
		super(String.format("The entity type %s is already registered", type.name()), list);
		this.type = type;
	}

	/**
	 * @return The already registered entity type.
	 */
	public EntityType getType() {
		return type;
	}
}
