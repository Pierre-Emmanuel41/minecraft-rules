package fr.pederobien.minecraft.rules.impl;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.interfaces.IEntityTypeList;
import fr.pederobien.utils.IPausable.PausableState;

public class MobsNotAllowedToSpawnGameRule extends EventGameRule<IEntityTypeList> {

	/**
	 * Creates a game rule to specify which mobs cannot spawn while the game is in progress.
	 * 
	 * @param game The game associated to this rule.
	 */
	public MobsNotAllowedToSpawnGameRule(IGame game) {
		super(game, "mobsNotAllowedToSpawn", new EntityTypeList(game.getName()), ERuleCode.GAME_RULE__MOBS_NOT_ALLOWED_TO_SPAWN__EXPLANATION);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onCreatureSpawn(CreatureSpawnEvent event) {
		if (!isEnable() || getGame().getState() == PausableState.NOT_STARTED || !getValue().toList().contains(event.getEntityType()))
			return;

		event.setCancelled(true);
	}
}
