package fr.pederobien.minecraftrules.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

import fr.pederobien.minecraftmanagers.WorldManager;
import fr.pederobien.minecraftrules.EGameRuleMessageCode;
import fr.pederobien.minecraftrules.impl.EventGameRule;

public class MobsNotAllowedToSpawnGameRule extends EventGameRule<List<EntityType>> {

	@SuppressWarnings("unchecked")
	public MobsNotAllowedToSpawnGameRule() {
		super("mobsNotAllowedToSpawn", Arrays.asList(), (Class<List<EntityType>>) Collections.<EntityType>emptyList().getClass(),
				EGameRuleMessageCode.MOBS_NOT_ALLOWED_TO_SPAWN_GAME_RULE__EXPLANATION);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sendSynchro(sender, EGameRuleMessageCode.MOBS_NOT_ALLOWED_TO_SPAWN_GAME_RULE__ALL_MOBS_ALLOWED);
			return true;
		}

		StringJoiner entityTypeNames = new StringJoiner("\n");
		List<EntityType> mobsNotAllowedToSpawn = new ArrayList<EntityType>();
		for (String entityType : args) {
			EntityType type = EntityType.fromName(entityType);
			if (type == null) {
				sendNotSynchro(sender, EGameRuleMessageCode.MOBS_NOT_ALLOWED_TO_SPAWN_GAME_RULE__MOB_UNDEFINED, entityType);
				return false;
			}
			mobsNotAllowedToSpawn.add(type);
			entityTypeNames.add(entityType);
		}

		setValue(mobsNotAllowedToSpawn);

		if (mobsNotAllowedToSpawn.size() == 1)
			sendSynchro(sender, EGameRuleMessageCode.MOBS_NOT_ALLOWED_TO_SPAWN_GAME_RULE__ONE_MOB_NOT_ALLOWED, entityTypeNames);
		else
			sendSynchro(sender, EGameRuleMessageCode.MOBS_NOT_ALLOWED_TO_SPAWN_GAME_RULE__SEVERAL_MOBS_NOT_ALLOWED, entityTypeNames);
		return true;
	}

	@Override
	protected List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return filter(getFreeEntity(asList(args)), args);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (!isRunning() || !getValue().contains(event.getEntityType()))
			return;

		event.setCancelled(true);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected String getCurrent(CommandSender sender) {
		if (getValue().isEmpty())
			return getMessage(sender, EGameRuleMessageCode.MOBS_NOT_ALLOWED_TO_SPAWN_GAME_RULE__DEFAULT);

		StringJoiner joiner = new StringJoiner("\n");
		for (EntityType type : getValue())
			joiner.add(type.getName());
		return "\n".concat(joiner.toString()).concat("\n");
	}

	@Override
	protected String getDefault(CommandSender sender) {
		return getMessage(sender, EGameRuleMessageCode.MOBS_NOT_ALLOWED_TO_SPAWN_GAME_RULE__DEFAULT);
	}

	@SuppressWarnings("deprecation")
	private Stream<String> getFreeEntity(List<String> alreadyMentionnedEntity) {
		return WorldManager.MOBS.stream().map(type -> type.getName()).filter(name -> !alreadyMentionnedEntity.contains(name));
	}
}
