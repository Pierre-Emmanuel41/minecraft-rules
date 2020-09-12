package fr.pederobien.minecraftrules.impl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftdevelopmenttoolkit.interfaces.messagecode.IMessageCodeSimpleMapEdition;
import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftgameplateform.impl.editions.AbstractSimpleMapEdition;
import fr.pederobien.minecraftgameplateform.impl.element.AbstractNominable;
import fr.pederobien.minecraftrules.EGameRuleMessageCode;
import fr.pederobien.minecraftrules.interfaces.IGameRule;
import fr.pederobien.minecraftrules.interfaces.IRunnableGameRule;
import fr.pederobien.minecraftrules.rules.PvpGameRule;

public abstract class GameRule<T> extends AbstractNominable implements IGameRule<T> {
	/**
	 * A list that contains all runnable rules that inherit this class.
	 */
	public static final List<IRunnableGameRule<?>> RUNNABLE_RULES = new ArrayList<IRunnableGameRule<?>>();

	/**
	 * A list that contains all rule that inherit this class.
	 */
	public static final List<IGameRule<?>> RULES = new ArrayList<IGameRule<?>>();

	/**
	 * A game rule to enable or disable the pvp.
	 */
	public static final IGameRule<Boolean> PVP = new PvpGameRule();

	private T value, defaultValue;
	private Class<T> type;
	private IMinecraftMessageCode explanation;
	private IMessageCodeSimpleMapEdition edition;

	protected GameRule(String name, T defaultValue, Class<T> type, IMinecraftMessageCode explanation) {
		super(name);
		this.defaultValue = defaultValue;
		this.type = type;
		this.explanation = explanation;
		edition = new GameRuleEdition();
		RULES.add(this);
		if (this instanceof IRunnableGameRule<?>)
			RUNNABLE_RULES.add((IRunnableGameRule<?>) this);
	}

	@Override
	public T getValue() {
		return value == null ? defaultValue : value;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public T getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void reset() {
		setValue(getDefaultValue());
	}

	@Override
	public Class<T> getType() {
		return type;
	}

	@Override
	public IMinecraftMessageCode getExplanation() {
		return explanation;
	}

	@Override
	public IMessageCodeSimpleMapEdition getEdition() {
		return edition;
	}

	/**
	 * Executes the given command, returning its success.
	 *
	 * @param sender  Source of the command.
	 * @param command Command which was executed.
	 * @param label   Alias of the command which was used.
	 * @param args    Passed command arguments.
	 * 
	 * @return true if a valid command, otherwise false
	 */
	protected abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);

	/**
	 * Requests a list of possible completions for a command argument.
	 *
	 * @param sender  Source of the command. For players tab-completing a command inside of a command block, this will be the player,
	 *                not the command block.
	 * @param command Command which was executed.
	 * @param alias   The alias used.
	 * @param args    The arguments passed to the command, including final partial argument to be completed and command label.
	 * 
	 * @return A List of possible completions for the final argument, or null to default to the command executor.
	 */
	protected abstract List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);

	/**
	 * @return The value that is displayed when the command ./rules value is ran.
	 */
	protected String getValueToString() {
		return value.toString();
	}

	/**
	 * Class used to create an edition associated to this game rule in order to modify it
	 * 
	 * @author Utilisateur
	 *
	 */
	protected class GameRuleEdition extends AbstractSimpleMapEdition {

		public GameRuleEdition() {
			super(getName(), explanation);
			addEdition(new ResetGameRuleEdition());
			addEdition(new ValueGameRuleEdition());
			addEdition(new SetGameRuleEdition());
		}
	}

	private class ResetGameRuleEdition extends AbstractSimpleMapEdition {

		public ResetGameRuleEdition() {
			super("reset", EGameRuleMessageCode.RESET_GAME_RULE__EXPLANATION);
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			setValue(getDefaultValue());
			sendMessageToSender(sender, EGameRuleMessageCode.RESET_GAME_RULE__VALUE_RESET, getName(), getValue());
			return true;
		}
	}

	private class ValueGameRuleEdition extends AbstractSimpleMapEdition {

		public ValueGameRuleEdition() {
			super("value", EGameRuleMessageCode.VALUE_GAME_RULE__EXPLANATION);
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			sendMessageToSenderNotSynchonized(sender, EGameRuleMessageCode.VALUE_GAME_RULE__DISPLAY, getName(), getValueToString());
			return true;
		}
	}

	private class SetGameRuleEdition extends AbstractSimpleMapEdition {

		public SetGameRuleEdition() {
			super("set", EGameRuleMessageCode.SET_GAME_RULE__EXPLANATION);
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			return GameRule.this.onCommand(sender, command, label, args);
		}

		@Override
		public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
			return GameRule.this.onTabComplete(sender, command, alias, args);
		}
	}
}
