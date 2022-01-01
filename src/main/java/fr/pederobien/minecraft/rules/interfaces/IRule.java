package fr.pederobien.minecraft.rules.interfaces;

import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.game.interfaces.IGame;

public interface IRule<T> {

	/**
	 * @return The code used to explain what does this rule do.
	 */
	IMinecraftCode getExplanation();

	/**
	 * @return The game associated to this game rule.
	 */
	IGame getGame();

	/**
	 * @return The name of this game rule.
	 */
	String getName();

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
	 * Reset the value of this game rule. This is a convenient method and is equivalent to <code>setValue(getDefaultValue());</code>
	 */
	void reset();

	/**
	 * @return True if this rule is enabled.
	 */
	boolean isEnable();

	/**
	 * Set to true to enable this rule.
	 * 
	 * @param isEnable True if this rule is enabled, false otherwise.
	 */
	void setEnable(boolean isEnable);
}
