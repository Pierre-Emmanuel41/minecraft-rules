package fr.pederobien.minecraftrules.impl;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftgameplateform.impl.element.AbstractNominable;
import fr.pederobien.minecraftrules.interfaces.IGameRule;
import fr.pederobien.minecraftrules.interfaces.IRunnableGameRule;

public abstract class GameRule<T> extends AbstractNominable implements IGameRule<T> {
	/**
	 * A list that contains all runnable rules that inherit this class.
	 */
	public static final List<IRunnableGameRule<?>> RUNNABLE_RULES = new ArrayList<IRunnableGameRule<?>>();

	/**
	 * A list that contains all rule that inherit this class.
	 */
	public static final List<IGameRule<?>> RULES = new ArrayList<IGameRule<?>>();

	private T value, defaultValue;
	private Class<T> type;
	private IMinecraftMessageCode explanation;

	protected GameRule(String name, T defaultValue, Class<T> type, IMinecraftMessageCode explanation) {
		super(name);
		this.defaultValue = defaultValue;
		this.type = type;
		this.explanation = explanation;
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
	public Class<T> getType() {
		return type;
	}

	@Override
	public IMinecraftMessageCode getExplanation() {
		return explanation;
	}
}
