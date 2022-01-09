package fr.pederobien.minecraft.rules.interfaces;

import java.util.function.Function;

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
	 * @return The parser used to serialize/deserialize rule value and default value.
	 */
	Parser<T> getParser();

	public static class Parser<T> {
		private Function<T, String> serializer;
		private Function<String, T> deserializer;

		/**
		 * Creates a Parser in order to serialize and deserialize game rule value and default value.
		 * 
		 * @param serializer   The function to save game rule values.
		 * @param deserializer The function to load game rule values.
		 */
		public Parser(Function<T, String> serializer, Function<String, T> deserializer) {
			this.serializer = serializer;
			this.deserializer = deserializer;
		}

		/**
		 * Transforms the given value to a String in order to be saved.
		 * 
		 * @param value The value to transform.
		 * 
		 * @return The String representation of the given value.
		 */
		public String serialize(T value) {
			return serializer.apply(value);
		}

		/**
		 * Retrieve the value from the given String representation.
		 * 
		 * @param value The String representation of a value.
		 * 
		 * @return The value associated to the String representation.
		 */
		public T deserialize(String value) {
			return deserializer.apply(value);
		}
	}
}
