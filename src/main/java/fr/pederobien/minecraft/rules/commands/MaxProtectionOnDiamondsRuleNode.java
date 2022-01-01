package fr.pederobien.minecraft.rules.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

import fr.pederobien.minecraft.game.impl.EGameCode;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.impl.MaxProtectionOnDiamondsGameRule;

public class MaxProtectionOnDiamondsRuleNode extends RuleNode<Integer> {
	private GetCurrentValueGameRuleNode<Integer> getterNode;
	private ResetGameRuleNode<Integer> resetNode;
	private EnableRuleNode<Integer> enableNode;
	private SetMaxProtectionOnDiamondsRuleNode setterNode;

	/**
	 * Creates a node in order to get the {@link MaxProtectionOnDiamondsGameRule} value and to modify the game rule value.
	 * 
	 * @param rule The rule to modify {@link Enchantment#PROTECTION_ENVIRONMENTAL} level restrictions on diamond stuff.
	 */
	protected MaxProtectionOnDiamondsRuleNode(MaxProtectionOnDiamondsGameRule rule) {
		super(rule, rule.getName(), rule.getExplanation(), r -> r != null);
		add(getterNode = new GetCurrentValueGameRuleNode<Integer>(rule));
		add(resetNode = new ResetGameRuleNode<Integer>(rule));
		add(enableNode = new EnableRuleNode<Integer>(rule));
		add(setterNode = new SetMaxProtectionOnDiamondsRuleNode(rule));
	}

	/**
	 * @return The node that displays the current value of the {@link MaxProtectionOnDiamondsGameRule}.
	 */
	public GetCurrentValueGameRuleNode<Integer> getGetterNode() {
		return getterNode;
	}

	/**
	 * @return The node that resets the game rule value.
	 */
	public ResetGameRuleNode<Integer> getResetNode() {
		return resetNode;
	}

	/**
	 * @return The node to enable or disable the game rule.
	 */
	public EnableRuleNode<Integer> getEnableNode() {
		return enableNode;
	}

	/**
	 * @return The node that sets the {@link MaxProtectionOnDiamondsGameRule} value.
	 */
	public SetMaxProtectionOnDiamondsRuleNode getSetterNode() {
		return setterNode;
	}

	public class SetMaxProtectionOnDiamondsRuleNode extends RuleNode<Integer> {

		/**
		 * Create a rule node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param rule The rule that defines the maximal {@link Enchantment#PROTECTION_ENVIRONMENTAL} level.
		 */
		protected SetMaxProtectionOnDiamondsRuleNode(MaxProtectionOnDiamondsGameRule rule) {
			super(rule, "set", rule.getExplanation(), r -> r != null && r.isEnable());
		}

		@Override
		public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
			switch (args.length) {
			case 1:
				return asList(getMessage(sender, ERuleCode.GAME_RULE__ENCHANT__LEVEL_COMPLETION));
			default:
				return emptyList();
			}
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			int level;
			try {
				level = Integer.parseInt(args[0]);
			} catch (IndexOutOfBoundsException e) {
				send(eventBuilder(sender, ERuleCode.GAME_RULE__ENCHANT__LEVEL_IS_MISSING, getRule().getName()));
				return false;
			} catch (NumberFormatException e) {
				send(eventBuilder(sender, EGameCode.BAD_FORMAT, getMessage(sender, ERuleCode.GAME_RULE__INTEGER_BAD_FORMAT)));
				return false;
			}

			getRule().setValue(level);
			sendSuccessful(sender, ERuleCode.GAME_RULE__ENCHANT__LEVEL_UPDATED, getRule().getName(), getRule().getValue());
			return true;
		}
	}
}
