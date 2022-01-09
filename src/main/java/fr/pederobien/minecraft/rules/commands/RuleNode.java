package fr.pederobien.minecraft.rules.commands;

import java.util.function.Function;
import java.util.function.Supplier;

import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.rules.interfaces.IRule;

public class RuleNode<T extends IRule<?>> extends RuleNodeBase<T> {
	private GetCurrentValueGameRuleNode<T> getterNode;
	private ResetGameRuleNode<T> resetNode;

	/**
	 * Create a rule node defined by a label, which correspond to its name, and an explanation. This node already contains the node to
	 * get the current game rule value, the node to reset the game rule value and the node to enable/disable the game rule.
	 * 
	 * @param rule        The rule associated to this node.
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 */
	protected RuleNode(Supplier<T> rule, String label, IMinecraftCode explanation, Function<T, Boolean> isAvailable) {
		super(rule, label, explanation, isAvailable);
		add(getterNode = new GetCurrentValueGameRuleNode<T>(rule));
		add(resetNode = new ResetGameRuleNode<T>(rule));
	}

	/**
	 * @return The node to display the current value of the rule.
	 */
	public GetCurrentValueGameRuleNode<T> getGetterNode() {
		return getterNode;
	}

	/**
	 * @return The node that reset the rule value.
	 */
	public ResetGameRuleNode<T> getResetNode() {
		return resetNode;
	}
}
