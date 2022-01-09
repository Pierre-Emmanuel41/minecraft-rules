package fr.pederobien.minecraft.rules.commands;

import java.util.List;
import java.util.function.Supplier;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntitySpawnEvent;

import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.managers.WorldManager;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.impl.EntityTypeList;
import fr.pederobien.minecraft.rules.impl.MobsNotAllowedToSpawnGameRule;
import fr.pederobien.minecraft.rules.interfaces.IEntityTypeList;

public class MobsNotAllowedToSpawnRuleNode extends RuleNode<MobsNotAllowedToSpawnGameRule> {
	private SetMobsNotAllowedToSpawnRuleNode setterNode;

	/**
	 * Creates a node in order to get the {@link MobsNotAllowedToSpawnGameRule} value and to modify the game rule value.
	 * 
	 * @param rule The rule to specify which mobs cannot spawn while the game is in progress.
	 */
	protected MobsNotAllowedToSpawnRuleNode(Supplier<MobsNotAllowedToSpawnGameRule> rule) {
		super(rule, "mobsNotAllowedToSpawn", ERuleCode.GAME_RULE__MOBS_NOT_ALLOWED_TO_SPAWN__EXPLANATION, r -> r != null);
		add(setterNode = new SetMobsNotAllowedToSpawnRuleNode(rule));
	}

	/**
	 * @return The node that modifies the type of mobs allowed to spawn while the game is in progress.
	 */
	public SetMobsNotAllowedToSpawnRuleNode getSetterNode() {
		return setterNode;
	}

	@SuppressWarnings("deprecation")
	public class SetMobsNotAllowedToSpawnRuleNode extends RuleNodeBase<MobsNotAllowedToSpawnGameRule> {

		/**
		 * Create a rule node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param rule The rule that cancel {@link EntitySpawnEvent} for specific entity while the game is in progress.
		 */
		protected SetMobsNotAllowedToSpawnRuleNode(Supplier<MobsNotAllowedToSpawnGameRule> rule) {
			super(rule, "set", null, r -> r != null);
		}

		@Override
		public IMinecraftCode getExplanation() {
			return getRule().getExplanation();
		}

		@Override
		public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
			List<String> alreadyMentionned = asList(args);
			return filter(WorldManager.MOBS.stream().map(type -> type.getName()).filter(name -> !alreadyMentionned.contains(name)), args);
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			if (args.length == 0) {
				sendSuccessful(sender, ERuleCode.GAME_RULE__MOBS_NOT_ALLOWED_TO_SPAWN_SET__ALL_MOBS_ALLOWED);
				return true;
			}

			IEntityTypeList mobs = new EntityTypeList(getRule().getGame().getName());
			for (String entityType : args) {
				EntityType type = EntityType.fromName(entityType);
				if (type == null) {
					send(eventBuilder(sender, ERuleCode.GAME_RULE__MOBS_NOT_ALLOWED_TO_SPAWN_SET__MOB_NOT_FOUND, entityType));
					return false;
				}
				mobs.add(type);
			}

			String mobNames = concat(args);
			getRule().setValue(mobs);

			if (mobs.toList().size() == 1)
				sendSuccessful(sender, ERuleCode.GAME_RULE__MOBS_NOT_ALLOWED_TO_SPAWN_SET__ONE_MOB_NOT_ALLOWED, getRule().getValue(), mobNames);
			else
				sendSuccessful(sender, ERuleCode.GAME_RULE__MOBS_NOT_ALLOWED_TO_SPAWN_SET__SEVERAL_MOBS_NOT_ALLOWED, getRule().getValue(), mobNames);
			return true;
		}
	}
}
