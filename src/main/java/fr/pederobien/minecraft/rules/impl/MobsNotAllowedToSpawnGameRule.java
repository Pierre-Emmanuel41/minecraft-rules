package fr.pederobien.minecraft.rules.impl;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.interfaces.IEntityTypeList;

public class MobsNotAllowedToSpawnGameRule extends EventRule<IEntityTypeList> {
	private static final EntityTypeListParser PARSER = new EntityTypeListParser();

	/**
	 * Creates a game rule to specify which mobs cannot spawn while the game is in progress.
	 * 
	 * @param game The game associated to this rule.
	 */
	public MobsNotAllowedToSpawnGameRule(IGame game) {
		super(game, "mobsNotAllowedToSpawn", new EntityTypeList(game.getName()), ERuleCode.GAME_RULE__MOBS_NOT_ALLOWED_TO_SPAWN__EXPLANATION, PARSER);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onCreatureSpawn(CreatureSpawnEvent event) {
		if (!isRunning() || !getValue().toList().contains(event.getEntityType()))
			return;

		event.setCancelled(true);
	}

	private static class EntityTypeListParser extends Parser<IEntityTypeList> {

		public EntityTypeListParser() {
			super(value -> value.toString(), value -> EntityTypeList.parse("mobsNotAlloawedToSpawn", value));
		}
	}
}
