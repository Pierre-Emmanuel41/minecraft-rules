package fr.pederobien.minecraft.rules.commands;

import java.util.List;
import java.util.function.Supplier;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.game.impl.EGameCode;
import fr.pederobien.minecraft.game.interfaces.ITeamConfigurable;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.impl.RevivePlayerNearTeamMatesGameRule;

public class RevivePlayerNearTeamMateRuleNode extends RuleNode<RevivePlayerNearTeamMatesGameRule> {
	private SetRevivePlayerNearTeamMateRuleNode setterNode;

	/**
	 * Creates a node in order to get the {@link RevivePlayerNearTeamMatesGameRule} value and to modify the game rule value.
	 * 
	 * @param rule The game rule that specifies where players revive while the game is in progress.
	 */
	protected RevivePlayerNearTeamMateRuleNode(Supplier<RevivePlayerNearTeamMatesGameRule> rule) {
		super(rule, "revivePlayerNearTeamMate", ERuleCode.GAME_RULE__REVIVE_PLAYER_NEAR_TEAM_MATES__EXPLANATION,
				r -> r != null && r.getGame() instanceof ITeamConfigurable);
		add(setterNode = new SetRevivePlayerNearTeamMateRuleNode(rule));
	}

	/**
	 * @return The node that modifies the {@link RevivePlayerNearTeamMatesGameRule} value.
	 */
	public SetRevivePlayerNearTeamMateRuleNode getSetterNode() {
		return setterNode;
	}

	public class SetRevivePlayerNearTeamMateRuleNode extends RuleNodeBase<RevivePlayerNearTeamMatesGameRule> {

		/**
		 * Create a rule node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param rule The rule that manage where players revive.
		 */
		protected SetRevivePlayerNearTeamMateRuleNode(Supplier<RevivePlayerNearTeamMatesGameRule> rule) {
			super(rule, "set", null, r -> r != null && r.getGame() instanceof ITeamConfigurable);
		}

		@Override
		public IMinecraftCode getExplanation() {
			return getRule().getExplanation();
		}

		@Override
		public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
			switch (args.length) {
			case 1:
				return filter(asList(getMessage(sender, ERuleCode.GAME_RULE__TRUE), getMessage(sender, ERuleCode.GAME_RULE__FALSE)).stream(), args);
			default:
				return emptyList();
			}
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			boolean value;
			String trueValue = getMessage(sender, ERuleCode.GAME_RULE__TRUE);
			String falseValue = getMessage(sender, ERuleCode.GAME_RULE__FALSE);

			try {
				if (args[0].equalsIgnoreCase(trueValue))
					value = true;
				else if (args[0].equalsIgnoreCase(falseValue))
					value = false;
				else {
					send(eventBuilder(sender, EGameCode.BAD_FORMAT, getMessage(sender, ERuleCode.GAME_RULE__BOOLEAN_BAD_FORMAT)));
					return false;
				}
			} catch (IndexOutOfBoundsException e) {
				send(eventBuilder(sender, ERuleCode.GAME_RULE__REVIVE_PLAYER_NEAR_TEAM_MATES_SET__VALUE_IS_MISSING, getRule().getName()));
				return false;
			}

			getRule().setValue(value);
			sendSuccessful(sender, ERuleCode.GAME_RULE__REVIVE_PLAYER_NEAR_TEAM_MATES_SET__VALUE_UPDATED, getRule().getName(), getRule().getValue());
			return true;
		}
	}
}
