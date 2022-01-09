package fr.pederobien.minecraft.rules.commands;

import java.util.List;
import java.util.function.Supplier;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.game.impl.EGameCode;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.impl.AnnounceAdvancementsGameRule;

public class AnnounceAdvancementsRuleNode extends RuleNode<AnnounceAdvancementsGameRule> {
	private SetAnnounceAdvancementsRuleNode setterNode;

	/**
	 * Creates a node in order to get the {@link AnnounceAdvancementsGameRule} value and to modify the game rule value.
	 * 
	 * @param rule The game rule that enable or disable the players advancements announcement.
	 */
	protected AnnounceAdvancementsRuleNode(Supplier<AnnounceAdvancementsGameRule> rule) {
		super(rule, "announceAdvancements", ERuleCode.GAME_RULE__ANNOUNCE_ADVANCEMENTS__EXPLANATION, r -> r != null);
		add(setterNode = new SetAnnounceAdvancementsRuleNode(rule));
	}

	/**
	 * @return The node that set the {@link AnnounceAdvancementsGameRule} value.
	 */
	public SetAnnounceAdvancementsRuleNode getSetterNode() {
		return setterNode;
	}

	public class SetAnnounceAdvancementsRuleNode extends RuleNodeBase<AnnounceAdvancementsGameRule> {

		/**
		 * Create a rule node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param rule The rule that specifies if players advancement should be displayed while the game is in progress.
		 */
		protected SetAnnounceAdvancementsRuleNode(Supplier<AnnounceAdvancementsGameRule> rule) {
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
				return asList(getMessage(sender, ERuleCode.GAME_RULE__TRUE), getMessage(sender, ERuleCode.GAME_RULE__FALSE));
			default:
				return emptyList();
			}
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			boolean value;
			try {
				if (args[0].equalsIgnoreCase(Boolean.TRUE.toString()))
					value = true;
				else if (args[0].equalsIgnoreCase(Boolean.FALSE.toString()))
					value = false;
				else {
					send(eventBuilder(sender, EGameCode.BAD_FORMAT, getMessage(sender, ERuleCode.GAME_RULE__BOOLEAN_BAD_FORMAT)));
					return false;
				}
			} catch (IndexOutOfBoundsException e) {
				send(eventBuilder(sender, ERuleCode.GAME_RULE__ANNOUNCE_ADVANCEMENTS_SET__VALUE_IS_MISSING, getRule().getName()));
				return false;
			}

			getRule().setValue(value);
			sendSuccessful(sender, ERuleCode.GAME_RULE__ANNOUNCE_ADVANCEMENTS_SET__VALUE_IS_UPDATED, getRule().getName(), getRule().getValue());
			return true;
		}
	}
}
