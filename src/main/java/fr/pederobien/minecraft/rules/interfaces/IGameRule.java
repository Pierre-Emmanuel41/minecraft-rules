package fr.pederobien.minecraft.rules.interfaces;

import fr.pederobien.minecraftdevelopmenttoolkit.interfaces.messagecode.IMessageCodeSimpleMapEdition;
import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftgameplateform.interfaces.editions.IPlateformCodeSender;
import fr.pederobien.persistence.interfaces.IUnmodifiableNominable;

public interface IGameRule<T> extends IUnmodifiableNominable, IPlateformCodeSender {

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
	 * @return The edition in charge of update this game rule.
	 */
	IMessageCodeSimpleMapEdition getEdition();
}
