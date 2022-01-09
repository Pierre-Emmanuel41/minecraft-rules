package fr.pederobien.minecraft.rules.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.GameRule;
import org.bukkit.enchantments.Enchantment;

import fr.pederobien.minecraft.rules.impl.AnnounceAdvancementsGameRule;
import fr.pederobien.minecraft.rules.impl.DisplayTeamMatesLocationGameRule;
import fr.pederobien.minecraft.rules.impl.MaxProtectionOnDiamondsGameRule;
import fr.pederobien.minecraft.rules.impl.MobsNotAllowedToSpawnGameRule;
import fr.pederobien.minecraft.rules.impl.NaturalRegenerationGameRule;
import fr.pederobien.minecraft.rules.impl.PvpGameRule;
import fr.pederobien.minecraft.rules.impl.RevivePlayerNearTeamMatesGameRule;

public interface IRuleList {

	/**
	 * @return The name of this list.
	 */
	String getName();

	/**
	 * Appends the given team to this list.
	 * 
	 * @param team The team to add.
	 * 
	 * @throws RuleAlreadyRegisteredException If a team is already registered for the team name.
	 */
	<T> void add(IRule<T> team);

	/**
	 * Removes the team associated to the given name.
	 * 
	 * @param name The team name to remove.
	 * 
	 * @return The removed team if registered, null otherwise.
	 */
	<T> IRule<T> remove(String name);

	/**
	 * Removes the given team from this list.
	 * 
	 * @param team The team to remove.
	 * 
	 * @return True if the team was registered, false otherwise.
	 */
	<T> boolean remove(IRule<T> team);

	/**
	 * Removes all registered teams. It also clear each registered players.
	 */
	void clear();

	/**
	 * Get the team associated to the given name.
	 * 
	 * @param name The team name.
	 * 
	 * @return An optional that contains the team if registered, an empty optional otherwise.
	 */
	<T> Optional<IRule<T>> getRule(String name);

	/**
	 * @return a sequential {@code Stream} over the elements in this collection.
	 */
	Stream<IRule<?>> stream();

	/**
	 * @return A copy of the underlying list.
	 */
	List<IRule<?>> toList();

	/**
	 * @return The rule to enable or disable the PVP while the game is in progress.
	 */
	PvpGameRule getPvpGameRule();

	/**
	 * @return The rule that modifies the level max of the {@link Enchantment#PROTECTION_ENVIRONMENTAL} while the game is in progress.
	 */
	MaxProtectionOnDiamondsGameRule getMaxProtectionOnDiamondsGameRule();

	/**
	 * @return The rule that modifies where players revive while the game is in progress.
	 */
	RevivePlayerNearTeamMatesGameRule getRevivePlayerNearTeamMatesGameRule();

	/**
	 * @return The rule that modifies the {@link GameRule#ANNOUNCE_ADVANCEMENTS} value while the game is in progress.
	 */
	AnnounceAdvancementsGameRule getAnnounceAdvancementsGameRule();

	/**
	 * @return The rule that modifies the {@link GameRule#NATURAL_REGENERATION} value while the game is in progress.
	 */
	NaturalRegenerationGameRule getNaturalRegenerationGameRule();

	/**
	 * @return The rule that modifies the list of mobs that are not allowed to spawn while the game is in progress.
	 */
	MobsNotAllowedToSpawnGameRule getMobsNotAllowedToSpawnGameRule();

	/**
	 * @return The rule that specifies if player's team mates locations are displayed while the game is in progress.
	 */
	DisplayTeamMatesLocationGameRule getDisplayTeamMatesLocationGameRule();
}
