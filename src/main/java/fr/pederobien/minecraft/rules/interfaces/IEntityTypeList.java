package fr.pederobien.minecraft.rules.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.entity.EntityType;

public interface IEntityTypeList extends Iterable<EntityType> {

	/**
	 * @return The name of this list.
	 */
	String getName();

	/**
	 * Appends the given entity type to this list.
	 * 
	 * @param entity type The entity type to add.
	 * 
	 * @throws EntityTypeAlreadyRegisteredException If an entity type is already registered for the entity type.
	 */
	void add(EntityType entityType);

	/**
	 * Removes the entity type associated to the given name.
	 * 
	 * @param name The entity type name to remove.
	 * 
	 * @return The removed entity type if registered, null otherwise.
	 */
	EntityType remove(String name);

	/**
	 * Removes the given entity type from this list.
	 * 
	 * @param entity type The entity type to remove.
	 * 
	 * @return True if the entity type was registered, false otherwise.
	 */
	boolean remove(EntityType entityType);

	/**
	 * Removes all registered types.
	 */
	void clear();

	/**
	 * Get the entity type associated to the given name.
	 * 
	 * @param name The entity type name.
	 * 
	 * @return An optional that contains the entity type if registered, an empty optional otherwise.
	 */
	Optional<EntityType> getEntityType(String name);

	/**
	 * @return a sequential {@code Stream} over the elements in this collection.
	 */
	Stream<EntityType> stream();

	/**
	 * @return A copy of the underlying list.
	 */
	List<EntityType> toList();
}
