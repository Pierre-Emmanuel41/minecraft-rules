package fr.pederobien.minecraftrules.impl;

import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftrules.interfaces.IEventGameRule;

public class EventGameRule<T> extends RunnableGameRule<T> implements IEventGameRule<T> {
	private boolean isRegistered;

	protected EventGameRule(String name, T defaultValue, Class<T> type, IMinecraftMessageCode explanation) {
		super(name, defaultValue, type, explanation);
	}

	@Override
	public void start() {
		if (isRegistered)
			return;

		getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
		isRegistered = true;
	}
}
