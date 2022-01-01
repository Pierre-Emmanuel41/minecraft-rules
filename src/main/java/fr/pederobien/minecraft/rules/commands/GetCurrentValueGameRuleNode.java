package fr.pederobien.minecraft.rules.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.interfaces.IRule;

public class GetCurrentValueGameRuleNode<T> extends RuleNode<T> {

	/**
	 * Creates a node to display the current value of a game rule.
	 * 
	 * @param rule The rule associated to this node.
	 */
	protected GetCurrentValueGameRuleNode(IRule<T> rule) {
		super(rule, "get", ERuleCode.GAME_RULE__GET__EXPLANATION, r -> r != null && r.isEnable());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sendSuccessful(sender, ERuleCode.GAME_RULE__GET__DISPLAY, getRule().getName(), getRule().getValue());
		return super.onCommand(sender, command, label, args);
	}
}
