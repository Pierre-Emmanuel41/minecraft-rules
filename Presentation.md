# Presentation

This project proposes a model for a rule in order to be attached to a game, to be configured and to be serialized/deserialied.

A Rule is modelled by the <code>IRule&lt;T&gt;</code> interface :

```java
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
```

A rule is autonomous. This means that it is attached to a game and according to the game state, it enables or disables itself. The <code>Parser</code> is responsible to generate a String representation of the rule in order to be serialized, and to parse the String representation in order to update the rule properties.

A game may need several rules, that is why it should implements the <code>IRuleList</code> interfaces. The implementation of this interface which is <code>RuleList</code> contains already implemented rules :

<code>PvpGameRule</code> - To enabled the PVP after a certain time of play. In that case, the game should implements the <code>IPvpTimeConfigurable</code>.</br>
<code>MaxProtectionOnDiamondsGameRule</code> - To set the level max of protection enchantment on diamonds armor pieces.</br>
<code>RevivePlayerNearTeamMatesGameRule</code> - To set whether or not a respawning player should be teleported to a team mate.</br>
<code>AnnounceAdvancementsGameRule</code> - To enable or disable the advancements announcement.</br>
<code>NaturalRegenerationGameRule</code> - To set if players can regenerate health naturally through their hunger bar.</br>
<code>MobsNotAllowedToSpawnGameRule</code> - To specify which mobs cannot spawn while a game is in progress.</br>
<code>DisplayTeamMatesLocationGameRule</code> - To enable or disable the display of player's team mates location.</br>

With the ITeamConfigurable interface comes a <code>RulesCommandTree</code> which provides already implemented minecraft command line argument in order to modify game rules properties :  

<code>rules</code> - To modify rules characteristics.</br>
&ensp;&lt;rule name&gt; - To specify which rule will be set up.</br>
&ensp;&ensp;<code>get</code> - To display the value of a game rule.</br>
&ensp;&ensp;<code>reset</code> - To set the value of this game rule to its default value.</br>
&ensp;&ensp;<code>set</code> - To set the value of a game rule.</br>