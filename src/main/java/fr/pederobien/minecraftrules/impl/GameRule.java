package fr.pederobien.minecraftrules.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import fr.pederobien.minecraftdevelopmenttoolkit.interfaces.messagecode.IMessageCodeSimpleMapEdition;
import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftgameplateform.impl.editions.AbstractSimpleMapEdition;
import fr.pederobien.minecraftgameplateform.impl.element.AbstractNominable;
import fr.pederobien.minecraftrules.EGameRuleMessageCode;
import fr.pederobien.minecraftrules.interfaces.IGameRule;
import fr.pederobien.minecraftrules.interfaces.IRunnableGameRule;
import fr.pederobien.minecraftrules.rules.AnnounceAdvancementsGameRule;
import fr.pederobien.minecraftrules.rules.AppleDropRateGameRule;
import fr.pederobien.minecraftrules.rules.DisplayCurrentTeammatesLocation;
import fr.pederobien.minecraftrules.rules.MaxProtectionOnDiamondsGameRule;
import fr.pederobien.minecraftrules.rules.MobsNotAllowedToSpawnGameRule;
import fr.pederobien.minecraftrules.rules.PvpGameRule;
import fr.pederobien.minecraftrules.rules.RevivePlayerNearTeamMateGameRule;

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

	/**
	 * Game rule to set whether or not a respawning player is teleported to a teammate.
	 */
	public static final IRunnableGameRule<Boolean> REVIVE_NEAR_TEAM_MATE = new RevivePlayerNearTeamMateGameRule();

	/**
	 * Game rule to enable/deactivate the display of player team mates location.
	 */
	public static final IRunnableGameRule<Boolean> DISPLAY_CURRENT_TEAMMATE_LOCATION = new DisplayCurrentTeammatesLocation();

	/**
	 * Game rule to modify the max level of the protection level on diamonds pieces.
	 */
	public static final IRunnableGameRule<Integer> MAX_PROTECTION_ON_DIAMONDS = new MaxProtectionOnDiamondsGameRule();

	/**
	 * Game rule to enable or disable the advancement announcement in the overworld, in the nether and in the ender.
	 */
	public static final IRunnableGameRule<Boolean> ANNOUNCE_ADVANCEMENTS = new AnnounceAdvancementsGameRule();

	/**
	 * Game rule to specify which mobs are not allowed to spawn.
	 */
	public static final IRunnableGameRule<List<EntityType>> MOBS_NOT_ALLOWED_TO_SPAWN = new MobsNotAllowedToSpawnGameRule();

	/**
	 * Game rule to modify the apple drop rate.
	 */
	public static final IRunnableGameRule<Double> APPLE_DROP_RATE = new AppleDropRateGameRule();

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
		value = defaultValue;
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
	 * Filter each string from the given stream using condition : <code>str.contains(filter)</code>
	 * 
	 * @param stream A stream that contains string to filter.
	 * @param filter The condition used to filter the previous stream.
	 * 
	 * @return A list of string from the given stream that contains the filter.
	 */
	protected List<String> filter(Stream<String> stream, String filter) {
		return stream.filter(str -> str.contains(filter)).collect(Collectors.toList());
	}

	/**
	 * Filter each string from the given stream using condition : <code>str.contains(args[args.length - 1])</code>. This method is
	 * equivalent to : <code>filter(stream, args[args.length - 1])</code>. In other words, this method filter the given stream using
	 * the last argument from the array <code>args</code>.
	 * 
	 * @param stream A stream that contains string to filter.
	 * @param args   The array that contains arguments coming from method <code>onTabComplete</code>.
	 * 
	 * @return A list of string from the given stream that contains the filter.
	 * 
	 * @see #filter(Stream, String)
	 * @see #onTabComplete(org.bukkit.command.CommandSender, org.bukkit.command.Command, String, String[])
	 */
	protected List<String> filter(Stream<String> stream, String... args) {
		return filter(stream, args[args.length - 1]);
	}

	/**
	 * Returns a fixed-size list backed by the specified array. (Changes to the returned list "write through" to the array.) This
	 * method acts as bridge between array-based and collection-based APIs, in combination with {@link Collection#toArray}. The
	 * returned list is serializable and implements {@link RandomAccess}.
	 *
	 * <p>
	 * This method also provides a convenient way to create a fixed-size list initialized to contain several elements:
	 * 
	 * <pre>
	 * List&lt;String&gt; stooges = Arrays.asList("Larry", "Moe", "Curly");
	 * </pre>
	 *
	 * @param strings the array by which the list will be backed.
	 * @return A list view of the specified array.
	 */
	protected List<String> asList(String... strings) {
		return Arrays.asList(strings);
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
	 * @return The current value that is displayed when the command ./rules value is ran.
	 */
	protected String getCurrent(CommandSender sender) {
		return getValue().toString();
	}

	/**
	 * @return The default value that is displayed when the command ./rules value is ran.
	 */
	protected String getDefault(CommandSender sender) {
		return defaultValue.toString();
	}

	/**
	 * Class used to create an edition associated to this game rule in order to modify it
	 * 
	 * @author Pierre-Emmanuel
	 *
	 */
	protected class GameRuleEdition extends AbstractSimpleMapEdition {

		public GameRuleEdition() {
			super(getName(), explanation);
			addEdition(new ResetGameRuleEdition());
			addEdition(new CurrentValueGameRuleEdition());
			addEdition(new SetGameRuleEdition());
		}
	}

	private class ResetGameRuleEdition extends AbstractSimpleMapEdition {

		public ResetGameRuleEdition() {
			super("reset", EGameRuleMessageCode.RESET_GAME_RULE__EXPLANATION);
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			reset();
			sendSynchro(sender, EGameRuleMessageCode.RESET_GAME_RULE__VALUE_RESET, getName(), getDefault(sender));
			return true;
		}
	}

	private class CurrentValueGameRuleEdition extends AbstractSimpleMapEdition {

		public CurrentValueGameRuleEdition() {
			super("get", EGameRuleMessageCode.CURRENT_VALUE_GAME_RULE__EXPLANATION);
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			sendNotSynchro(sender, EGameRuleMessageCode.CURRENT_VALUE_GAME_RULE__DISPLAY, getName(), getCurrent(sender), getDefault(sender));
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
