package fr.pederobien.minecraft.rules.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import org.bukkit.entity.EntityType;

import fr.pederobien.minecraft.game.exceptions.TeamAlreadyRegisteredException;
import fr.pederobien.minecraft.rules.event.EntityTypeAddPostEvent;
import fr.pederobien.minecraft.rules.event.EntityTypeRemovePostEvent;
import fr.pederobien.minecraft.rules.exceptions.EntityTypeAlreadyRegisteredException;
import fr.pederobien.minecraft.rules.interfaces.IEntityTypeList;
import fr.pederobien.utils.event.EventManager;

public class EntityTypeList implements IEntityTypeList {
	private String name;
	private Map<String, EntityType> entityTypes;
	private Lock lock;

	public EntityTypeList(String name) {
		this.name = name;
		entityTypes = new LinkedHashMap<String, EntityType>();
		lock = new ReentrantLock(true);
	}

	@Override
	public Iterator<EntityType> iterator() {
		return entityTypes.values().iterator();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void add(EntityType entityType) {
		addType(entityType);
		EventManager.callEvent(new EntityTypeAddPostEvent(this, entityType));
	}

	@Override
	public EntityType remove(String name) {
		EntityType team = removeType(name);
		if (team != null)
			EventManager.callEvent(new EntityTypeRemovePostEvent(this, team));
		return team;
	}

	@Override
	public boolean remove(EntityType team) {
		return remove(team.name()) != null;
	}

	@Override
	public void clear() {
		lock.lock();
		try {
			Set<String> names = new HashSet<String>(entityTypes.keySet());
			for (String name : names) {
				EntityType team = entityTypes.remove(name);
				EventManager.callEvent(new EntityTypeRemovePostEvent(this, team));
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Optional<EntityType> getEntityType(String name) {
		return Optional.ofNullable(entityTypes.get(name));
	}

	@Override
	public Stream<EntityType> stream() {
		return entityTypes.values().stream();
	}

	@Override
	public List<EntityType> toList() {
		return new ArrayList<EntityType>(entityTypes.values());
	}

	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "[", "]");
		forEach(type -> joiner.add(type.getName()));
		return joiner.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof IEntityTypeList))
			return false;

		IEntityTypeList other = (IEntityTypeList) obj;
		return toList().equals(other.toList());
	}

	/**
	 * Thread safe operation that adds an entity type to the list of types.
	 * 
	 * @param entityType The type to add.
	 * 
	 * @throws TeamAlreadyRegisteredException if a team is already registered for the team name.
	 */
	private void addType(EntityType entityType) {
		lock.lock();
		try {
			if (entityTypes.get(entityType.name()) != null)
				throw new EntityTypeAlreadyRegisteredException(this, entityType);

			entityTypes.put(entityType.name(), entityType);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Thread safe operation that removes an entity type from the list of types.
	 * 
	 * @param name The name of the entity type to remove.
	 * 
	 * @return The entity type associated to the given name if registered, null otherwise.
	 */
	private EntityType removeType(String name) {
		lock.lock();
		try {
			return entityTypes.remove(name);
		} finally {
			lock.unlock();
		}
	}
}
