package fr.pederobien.minecraft.rules.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraft.game.impl.EGameCode;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.interfaces.IRule;

public class EnableRuleNode<T> extends RuleNode<T> {

	/**
	 * Creates a node to enable or disable a rule.
	 * 
	 * @param rule The rule associated to this node.
	 */
	protected EnableRuleNode(IRule<T> rule) {
		super(rule, "enable", ERuleCode.GAME_RULE__ENABLE__EXPLANATION, r -> r != null);
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
		boolean enable;
		try {
			if (args[0].equalsIgnoreCase(Boolean.TRUE.toString()))
				enable = true;
			else if (args[0].equalsIgnoreCase(Boolean.FALSE.toString()))
				enable = false;
			else {
				send(eventBuilder(sender, EGameCode.BAD_FORMAT, getMessage(sender, ERuleCode.GAME_RULE__BOOLEAN_BAD_FORMAT)));
				return false;
			}
		} catch (IndexOutOfBoundsException e) {
			send(eventBuilder(sender, ERuleCode.GAME_RULE__ENABLE__VALUE_IS_MISSING, getRule().getName()));
			return false;
		}

		getRule().setEnable(enable);
		sendSuccessful(sender, getRule().isEnable() ? ERuleCode.GAME_RULE__ENABLE__RULE_ENABLED : ERuleCode.GAME_RULE__ENABLE__RULE_DISABLED, getRule().getName());
		return true;
	}
}
