package fr.pederobien.minecraft.rules.commands;

import java.util.List;
import java.util.function.Supplier;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.game.impl.EGameCode;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.impl.PvpGameRule;

public class PvpRuleNode extends RuleNode<PvpGameRule> {
	private SetPvpRuleNode setterNode;

	/**
	 * Creates a node in order to get the {@link PvpGameRule} value and to modify the game rule value.
	 * 
	 * @param rule The rule to enable or disable the PVP while the game is in progress.
	 */
	protected PvpRuleNode(Supplier<PvpGameRule> rule) {
		super(rule, "pvp", ERuleCode.GAME_RULE__PVP__EXPLANATION, r -> r != null);
		add(setterNode = new SetPvpRuleNode(rule));
	}

	/**
	 * @return The node that sets the PVP rule value.
	 */
	public SetPvpRuleNode getSetterNode() {
		return setterNode;
	}

	public class SetPvpRuleNode extends RuleNodeBase<PvpGameRule> {

		/**
		 * Create a rule node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param rule The PVP rule associated to this node.
		 */
		protected SetPvpRuleNode(Supplier<PvpGameRule> rule) {
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
				send(eventBuilder(sender, ERuleCode.GAME_RULE__PVP_SET__VALUE_IS_MISSING, getRule().getName()));
				return false;
			}

			getRule().setValue(value);
			sendSuccessful(sender, ERuleCode.GAME_RULE__PVP_SET__VALUE_UPDATED, getRule().getName(), getRule().getValue());
			return true;
		}
	}
}
