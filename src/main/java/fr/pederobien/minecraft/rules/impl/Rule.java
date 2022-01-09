package fr.pederobien.minecraft.rules.impl;

import org.bukkit.GameRule;

import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.managers.WorldManager;
import fr.pederobien.minecraft.rules.event.RuleChangePostEvent;
import fr.pederobien.minecraft.rules.interfaces.IRule;
import fr.pederobien.utils.event.EventManager;

public class Rule<T> implements IRule<T> {
	private IMinecraftCode explanation;
	private IGame game;
	private String name;
	private T value, defaultValue;
	private Parser<T> parser;

	/**
	 * Creates a game rule based on the given parameters.
	 * 
	 * @param game         The game to which this rule is associated.
	 * @param name         The game rule name.
	 * @param defaultValue The default game rule value.
	 * @param explanation  The code used to explain what does this rule do.
	 * @param parser       The parser used to save/load game rule values.
	 */
	protected Rule(IGame game, String name, T defaultValue, IMinecraftCode explanation, Parser<T> parser) {
		this.game = game;
		this.name = name;
		this.defaultValue = defaultValue;
		this.explanation = explanation;
		this.parser = parser;
	}

	@Override
	public IMinecraftCode getExplanation() {
		return explanation;
	}

	@Override
	public IGame getGame() {
		return game;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public T getValue() {
		return value == null ? defaultValue : value;
	}

	@Override
	public void setValue(T value) {
		if (getValue().equals(value))
			return;

		T oldValue = this.value;
		this.value = value;
		EventManager.callEvent(new RuleChangePostEvent<T>(this, oldValue));
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
	public Parser<T> getParser() {
		return parser;
	}

	/**
	 * Set the given {@link GameRule}'s new value.
	 *
	 * @param rule     the GameRule to update
	 * @param newValue the new value
	 */
	protected <U> void setGameRule(GameRule<U> rule, U newValue) {
		WorldManager.OVERWORLD.setGameRule(rule, newValue);
		WorldManager.NETHER_WORLD.setGameRule(rule, newValue);
		WorldManager.END_WORLD.setGameRule(rule, newValue);
	}
}
