package fr.pederobien.minecraft.rules.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraft.game.impl.EGameCode;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.impl.PvpGameRule;

public class PvpRuleNode extends RuleNode<Boolean> {
	private GetCurrentValueGameRuleNode<Boolean> getterNode;
	private ResetGameRuleNode<Boolean> resetNode;
	private EnableRuleNode<Boolean> enableNode;
	private SetPvpRuleNode setterNode;

	/**
	 * Creates a node in order to get the {@link PvpGameRule} value and to modify the game rule value.
	 * 
	 * @param rule The rule to enable or disable the PVP while the game is in progress.
	 */
	protected PvpRuleNode(PvpGameRule rule) {
		super(rule, rule.getName(), rule.getExplanation(), r -> r != null);
		add(getterNode = new GetCurrentValueGameRuleNode<Boolean>(rule));
		add(resetNode = new ResetGameRuleNode<Boolean>(rule));
		add(enableNode = new EnableRuleNode<>(rule));
		add(setterNode = new SetPvpRuleNode(rule));
	}

	/**
	 * @return The node that displays the current value of the PVP rule.
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
	 * @return The node that sets the PVP rule value.
	 */
	public SetPvpRuleNode getSetterNode() {
		return setterNode;
	}

	public class SetPvpRuleNode extends RuleNode<Boolean> {

		/**
		 * Create a rule node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param rule The PVP rule associated to this node.
		 */
		protected SetPvpRuleNode(PvpGameRule rule) {
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
				send(eventBuilder(sender, ERuleCode.GAME_RULE__PVP_SET__VALUE_IS_MISSING, getRule().getName()));
				return false;
			}

			getRule().setValue(value);
			sendSuccessful(sender, ERuleCode.GAME_RULE__PVP_SET__VALUE_UPDATED, getRule().getName());
			return true;
		}
	}
}
