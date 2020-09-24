package fr.pederobien.minecraftrules.impl;

import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftrules.interfaces.IRunnableGameRule;

public abstract class RunnableGameRule<T> extends GameRule<T> implements IRunnableGameRule<T> {
	private boolean isRunning;

	protected RunnableGameRule(String name, T defaultValue, Class<T> type, IMinecraftMessageCode explanation) {
		super(name, defaultValue, type, explanation);
	}

	@Override
	public void start() {
		setIsRunning(true);
	}

	@Override
	public void stop() {
		setIsRunning(false);
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}

	private void setIsRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
