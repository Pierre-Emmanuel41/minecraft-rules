package fr.pederobien.minecraft.rules.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntitySpawnEvent;

import fr.pederobien.minecraft.managers.WorldManager;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.impl.EntityTypeList;
import fr.pederobien.minecraft.rules.impl.MobsNotAllowedToSpawnGameRule;
import fr.pederobien.minecraft.rules.interfaces.IEntityTypeList;

public class MobsNotAllowedToSpawnRuleNode extends RuleNode<IEntityTypeList> {
	private GetCurrentValueGameRuleNode<IEntityTypeList> getterNode;
	private ResetGameRuleNode<IEntityTypeList> resetNode;
	private EnableRuleNode<IEntityTypeList> enableNode;
	private SetMobsNotAllowedToSpawnRuleNode setterNode;

	/**
	 * Creates a node in order to get the {@link MobsNotAllowedToSpawnGameRule} value and to modify the game rule value.
	 * 
	 * @param rule The rule to specify which mobs cannot spawn while the game is in progress.
	 */
	protected MobsNotAllowedToSpawnRuleNode(MobsNotAllowedToSpawnGameRule rule) {
		super(rule, rule.getName(), rule.getExplanation(), r -> r != null);
		add(getterNode = new GetCurrentValueGameRuleNode<IEntityTypeList>(rule));
		add(resetNode = new ResetGameRuleNode<IEntityTypeList>(rule));
		add(enableNode = new EnableRuleNode<IEntityTypeList>(rule));
		add(setterNode = new SetMobsNotAllowedToSpawnRuleNode(rule));
	}

	/**
	 * @return The node that displays the current value of the {@link MobsNotAllowedToSpawnGameRule}.
	 */
	public GetCurrentValueGameRuleNode<IEntityTypeList> getGetterNode() {
		return getterNode;
	}

	/**
	 * @return The node that resets the value of the {@link MobsNotAllowedToSpawnGameRule}.
	 */
	public ResetGameRuleNode<IEntityTypeList> getResetNode() {
		return resetNode;
	}

	/**
	 * @return The node to enable or disable the game rule.
	 */
	public EnableRuleNode<IEntityTypeList> getEnableNode() {
		return enableNode;
	}

	/**
	 * @return The node that modifies the type of mobs allowed to spawn while the game is in progress.
	 */
	public SetMobsNotAllowedToSpawnRuleNode getSetterNode() {
		return setterNode;
	}

	@SuppressWarnings("deprecation")
	public class SetMobsNotAllowedToSpawnRuleNode extends RuleNode<IEntityTypeList> {

		/**
		 * Create a rule node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param rule The rule that cancel {@link EntitySpawnEvent} for specific entity while the game is in progress.
		 */
		protected SetMobsNotAllowedToSpawnRuleNode(MobsNotAllowedToSpawnGameRule rule) {
			super(rule, "set", rule.getExplanation(), r -> r != null && r.isEnable());
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
				sendSuccessful(sender, ERuleCode.GAME_RULE__MOBS_NOT_ALLOWED_TO_SPAWN_SET__ONE_MOB_NOT_ALLOWED, mobNames);
			else
				sendSuccessful(sender, ERuleCode.GAME_RULE__MOBS_NOT_ALLOWED_TO_SPAWN_SET__SEVERAL_MOBS_NOT_ALLOWED, mobNames);
			return true;
		}
	}
}
