package fr.pederobien.minecraft.rules.impl;

import fr.pederobien.minecraft.rules.interfaces.IRunnableGameRule;
import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;

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
