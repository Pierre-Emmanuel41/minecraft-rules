package fr.pederobien.minecraft.rules.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.interfaces.IRule;

public class ResetGameRuleNode<T> extends RuleNode<T> {

	/**
	 * Creates a node in order to reset the value of a game rule.
	 * 
	 * @param rule The rule associated to this node.
	 */
	protected ResetGameRuleNode(IRule<T> rule) {
		super(rule, "reset", ERuleCode.GAME_RULE__RESET__EXPLANATION, r -> r != null && r.isEnable());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		getRule().reset();
		sendSuccessful(sender, ERuleCode.GAME_RULE__RESET__VALUE_RESET, getRule().getValue());
		return true;
	}
}
