package fr.pederobien.minecraft.rules.commands;

import java.util.List;
import java.util.function.Supplier;

import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.game.impl.EGameCode;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.impl.NaturalRegenerationGameRule;

public class NaturalRegenerationRuleNode extends RuleNode<NaturalRegenerationGameRule> {
	private SetNaturalRegenerationRuleNode setterNode;

	/**
	 * Creates a node in order to get the {@link NaturalRegenerationGameRule} value and to modify the game rule value.
	 * 
	 * @param rule The rule to enable or disable the {@link GameRule#NATURAL_REGENERATION} while the game is in progress.
	 */
	protected NaturalRegenerationRuleNode(Supplier<NaturalRegenerationGameRule> rule) {
		super(rule, "naturalRegeneration", ERuleCode.GAME_RULE__NATURAL_REGENERATION__EXPLANATION, r -> r != null);
		add(setterNode = new SetNaturalRegenerationRuleNode(rule));
	}

	/**
	 * @return The node that sets the natural regeneration rule value.
	 */
	public SetNaturalRegenerationRuleNode getSetterNode() {
		return setterNode;
	}

	public class SetNaturalRegenerationRuleNode extends RuleNodeBase<NaturalRegenerationGameRule> {

		/**
		 * Create a rule node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param rule The natural regeneration rule associated to this node.
		 */
		protected SetNaturalRegenerationRuleNode(Supplier<NaturalRegenerationGameRule> rule) {
			super(rule, "set", null, r -> r != null);
		}

		@Override
		public IMinecraftCode getExplanation() {
			return getRule().getExplanation();
		}

		@Override
		public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
			switch (args.length) {
			case 1:
				return filter(asList(getMessage(sender, ERuleCode.GAME_RULE__TRUE), getMessage(sender, ERuleCode.GAME_RULE__FALSE)).stream(), args);
			default:
				return emptyList();
			}
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			boolean value;
			String trueValue = getMessage(sender, ERuleCode.GAME_RULE__TRUE);
			String falseValue = getMessage(sender, ERuleCode.GAME_RULE__FALSE);

			try {
				if (args[0].equalsIgnoreCase(trueValue))
					value = true;
				else if (args[0].equalsIgnoreCase(falseValue))
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
			sendSuccessful(sender, ERuleCode.GAME_RULE__NATURAL_REGENERATION_SET__VALUE_IS_UPDATED, getRule().getName(), getRule().getValue());
			return true;
		}
	}
}
