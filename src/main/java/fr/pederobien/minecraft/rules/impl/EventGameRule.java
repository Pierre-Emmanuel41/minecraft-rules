package fr.pederobien.minecraft.rules.impl;

import fr.pederobien.minecraft.rules.RulesPlugin;
import fr.pederobien.minecraft.rules.interfaces.IEventGameRule;
import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;

public abstract class EventGameRule<T> extends RunnableGameRule<T> implements IEventGameRule<T> {
	private boolean isRegistered;

	protected EventGameRule(String name, T defaultValue, Class<T> type, IMinecraftMessageCode explanation) {
		super(name, defaultValue, type, explanation);
	}

	@Override
	public void start() {
		super.start();
		if (isRegistered)
			return;

		RulesPlugin.get().getServer().getPluginManager().registerEvents(this, RulesPlugin.get());
		isRegistered = true;
	}
}
