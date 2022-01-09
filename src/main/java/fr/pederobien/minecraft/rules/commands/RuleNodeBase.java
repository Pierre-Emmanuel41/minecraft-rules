package fr.pederobien.minecraft.rules.commands;

import java.util.function.Function;
import java.util.function.Supplier;

import fr.pederobien.minecraft.commandtree.impl.MinecraftCodeNode;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.rules.interfaces.IRule;

public class RuleNodeBase<T extends IRule<?>> extends MinecraftCodeNode {
	private Supplier<T> rule;

	/**
	 * Create a rule node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param rule        The rule associated to this node.
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 */
	protected RuleNodeBase(Supplier<T> rule, String label, IMinecraftCode explanation, Function<T, Boolean> isAvailable) {
		super(label, explanation, () -> isAvailable.apply(rule.get()));
		this.rule = rule;
	}

	/**
	 * @return The rule associated to this node.
	 */
	public T getRule() {
		return rule.get();
	}
}
