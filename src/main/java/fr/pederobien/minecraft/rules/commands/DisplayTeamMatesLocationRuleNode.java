package fr.pederobien.minecraft.rules.commands;

import java.util.List;
import java.util.function.Supplier;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.game.impl.EGameCode;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.impl.DisplayTeamMatesLocationGameRule;

public class DisplayTeamMatesLocationRuleNode extends RuleNode<DisplayTeamMatesLocationGameRule> {
	private SetDisplayTeamMatesLocationRuleNode setterNode;

	/**
	 * Creates a node in order to get the {@link DisplayTeamMatesLocationGameRule} value and to modify the game rule value.
	 * 
	 * @param rule The rule to enable or disable the display of player's team mates location.
	 */
	protected DisplayTeamMatesLocationRuleNode(Supplier<DisplayTeamMatesLocationGameRule> rule) {
		super(rule, "displayTeamMatesLocation", ERuleCode.GAME_RULE__DISPLAY_TEAM_MATES_LOCATION__EXPLANATION, r -> r != null);
		add(setterNode = new SetDisplayTeamMatesLocationRuleNode(rule));
	}

	/**
	 * @return The node that sets the value of {@link DisplayTeamMatesLocationGameRule}.
	 */
	public SetDisplayTeamMatesLocationRuleNode getSetterNode() {
		return setterNode;
	}

	public class SetDisplayTeamMatesLocationRuleNode extends RuleNodeBase<DisplayTeamMatesLocationGameRule> {

		/**
		 * Create a rule node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param rule The rule that displays the team mate location wile the game is in progress.
		 */
		protected SetDisplayTeamMatesLocationRuleNode(Supplier<DisplayTeamMatesLocationGameRule> rule) {
			super(rule, "set", null, r -> r != null);
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
				send(eventBuilder(sender, ERuleCode.GAME_RULE__DISPLAY_TEAM_MATES_LOCATION_SET__VALUE_IS_MISSING, getRule().getName()));
				return false;
			}

			getRule().setValue(value);
			sendSuccessful(sender, ERuleCode.GAME_RULE__DISPLAY_TEAM_MATES_LOCATION_SET__VALUE_IS_UPDATED, getRule().getName(), getRule().getValue());
			return true;
		}
	}
}
