package fr.pederobien.minecraftrules.impl;

import org.bukkit.plugin.Plugin;

import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecraftrules.RulesPlugin;
import fr.pederobien.minecraftrules.interfaces.IRunnableGameRule;

public class RunnableGameRule<T> extends GameRule<T> implements IRunnableGameRule<T> {
	private boolean isRunning;
	private Plugin plugin;

	protected RunnableGameRule(String name, T defaultValue, Class<T> type, IMinecraftMessageCode explanation) {
		super(name, defaultValue, type, explanation);
		plugin = Plateform.getPluginHelper().getPlugin(RulesPlugin.NAME).get();
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

	/**
	 * @return The plugin associated to this rule. This plugin is useful to registered event or run task periodically.
	 */
	protected Plugin getPlugin() {
		return plugin;
	}

	private void setIsRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
