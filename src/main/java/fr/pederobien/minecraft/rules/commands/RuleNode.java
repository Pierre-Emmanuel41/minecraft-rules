package fr.pederobien.minecraft.rules.commands;

import java.util.function.Function;

import fr.pederobien.minecraft.commandtree.impl.MinecraftCodeNode;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.rules.interfaces.IRule;

public class RuleNode<T> extends MinecraftCodeNode {
	private IRule<T> rule;

	/**
	 * Create a rule node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param rule        The rule associated to this node.
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 */
	protected RuleNode(IRule<T> rule, String label, IMinecraftCode explanation, Function<IRule<T>, Boolean> isAvailable) {
		super(label, explanation, () -> isAvailable.apply(rule));
		this.rule = rule;
	}

	/**
	 * @return The rule associated to this node.
	 */
	public IRule<T> getRule() {
		return rule;
	}
}
