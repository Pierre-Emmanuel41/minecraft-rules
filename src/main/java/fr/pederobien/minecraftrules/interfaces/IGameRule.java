package fr.pederobien.minecraftrules.interfaces;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.persistence.interfaces.IUnmodifiableNominable;

public interface IGameRule<T> extends IUnmodifiableNominable {

	/**
	 * @return the current value of this game rule.
	 */
	T getValue();

	/**
	 * Set the current value of this game rule.
	 * 
	 * @param value The current game rule value.
	 */
	void setValue(T value);

	/**
	 * @return The default value of this game rule. The returned value could not match the current game rule value.
	 */
	T getDefaultValue();

	/**
	 * Get the type of this rule.
	 *
	 * @return the rule type.
	 */
	Class<T> getType();

	/**
	 * @return The code used to explain what does this rule do.
	 */
	IMinecraftMessageCode getExplanation();

	/**
	 * @return The completer that propose completions for this game rule.
	 */
	TabCompleter getCompleter();

	/**
	 * @return The executor used to execute some codes associated to this game rule.
	 */
	CommandExecutor getExecutor();
}
