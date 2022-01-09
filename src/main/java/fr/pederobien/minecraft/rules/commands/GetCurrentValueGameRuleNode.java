package fr.pederobien.minecraft.rules.commands;

import java.util.function.Supplier;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.interfaces.IRule;

public class GetCurrentValueGameRuleNode<T extends IRule<?>> extends RuleNodeBase<T> {

	/**
	 * Creates a node to display the current value of a game rule.
	 * 
	 * @param rule The rule associated to this node.
	 */
	protected GetCurrentValueGameRuleNode(Supplier<T> rule) {
		super(rule, "get", ERuleCode.GAME_RULE__GET__EXPLANATION, r -> r != null);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sendSuccessful(sender, ERuleCode.GAME_RULE__GET__DISPLAY, getRule().getName(), getRule().getValue(), getRule().getDefaultValue());
		return super.onCommand(sender, command, label, args);
	}
}
