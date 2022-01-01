package fr.pederobien.minecraft.rules.commands;

import java.util.List;

import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraft.game.impl.EGameCode;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.impl.NaturalRegenerationGameRule;

public class NaturalRegenerationRuleNode extends RuleNode<Boolean> {
	private GetCurrentValueGameRuleNode<Boolean> getterNode;
	private ResetGameRuleNode<Boolean> resetNode;
	private EnableRuleNode<Boolean> enableNode;
	private SetNaturalRegenerationRuleNode setterNode;

	/**
	 * Creates a node in order to get the {@link NaturalRegenerationGameRule} value and to modify the game rule value.
	 * 
	 * @param rule The rule to enable or disable the {@link GameRule#NATURAL_REGENERATION} while the game is in progress.
	 */
	protected NaturalRegenerationRuleNode(NaturalRegenerationGameRule rule) {
		super(rule, rule.getName(), rule.getExplanation(), r -> r != null);
		add(getterNode = new GetCurrentValueGameRuleNode<Boolean>(rule));
		add(resetNode = new ResetGameRuleNode<Boolean>(rule));
		add(enableNode = new EnableRuleNode<Boolean>(rule));
		add(setterNode = new SetNaturalRegenerationRuleNode(rule));
	}

	/**
	 * @return The node that displays the current value of the natural regeneration rule.
	 */
	public GetCurrentValueGameRuleNode<Boolean> getGetterNode() {
		return getterNode;
	}

	/**
	 * @return The node that resets the game rule value.
	 */
	public ResetGameRuleNode<Boolean> getResetNode() {
		return resetNode;
	}

	/**
	 * @return The node to enable or disable the game rule.
	 */
	public EnableRuleNode<Boolean> getEnableNode() {
		return enableNode;
	}

	/**
	 * @return The node that sets the natural regeneration rule value.
	 */
	public SetNaturalRegenerationRuleNode getSetterNode() {
		return setterNode;
	}

	public class SetNaturalRegenerationRuleNode extends RuleNode<Boolean> {

		/**
		 * Create a rule node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param rule The natural regeneration rule associated to this node.
		 */
		protected SetNaturalRegenerationRuleNode(NaturalRegenerationGameRule rule) {
			super(rule, "set", rule.getExplanation(), r -> r != null && r.isEnable());
		}

		@Override
		public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
			switch (args.length) {
			case 1:
				return asList(getMessage(sender, ERuleCode.GAME_RULE__TRUE), getMessage(sender, ERuleCode.GAME_RULE__FALSE));
			default:
				return emptyList();
			}
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			boolean value;
			try {
				if (args[0].equalsIgnoreCase(Boolean.TRUE.toString()))
					value = true;
				else if (args[0].equalsIgnoreCase(Boolean.FALSE.toString()))
					value = false;
				else {
					send(eventBuilder(sender, EGameCode.BAD_FORMAT, getMessage(sender, ERuleCode.GAME_RULE__BOOLEAN_BAD_FORMAT)));
					return false;
				}
			} catch (IndexOutOfBoundsException e) {
				send(eventBuilder(sender, ERuleCode.GAME_RULE__NATURAL_REGENERATION_SET__VALUE_IS_MISSING, getRule().getName()));
				return false;
			}

			getRule().setValue(value);
			sendSuccessful(sender, ERuleCode.GAME_RULE__NATURAL_REGENERATION_SET__VALUE_IS_UPDATED, getRule().getName());
			return true;
		}
	}
}
