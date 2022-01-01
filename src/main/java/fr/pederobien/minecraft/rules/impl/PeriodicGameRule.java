package fr.pederobien.minecraft.rules.impl;

import org.bukkit.scheduler.BukkitTask;

import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.managers.BukkitManager;
import fr.pederobien.minecraft.rules.RulesPlugin;
import fr.pederobien.minecraft.rules.interfaces.IPeriodicGameRule;
import fr.pederobien.utils.IPausable.PausableState;

public abstract class PeriodicGameRule<T> extends Rule<T> implements IPeriodicGameRule<T> {
	private long period;
	private BukkitTask task;

	/**
	 * Creates a periodic game rule based on the given parameter.
	 * 
	 * @param game         The game associated to this rule.
	 * @param name         The name of this rule.
	 * @param defaultValue The default rule value.
	 * @param explanation  The code used to explain what does this rule do.
	 */
	protected PeriodicGameRule(IGame game, String name, T defaultValue, IMinecraftCode explanation) {
		super(game, name, defaultValue, explanation);
	}

	@Override
	public long getPeriod() {
		return period;
	}

	@Override
	public void setPeriod(long period) {
		this.period = period;

		if (getGame().getState() != PausableState.STARTED)
			return;

		stop();
		start();
	}

	/**
	 * Register this game rule for the Bukkit scheduler in order to be executed later.
	 */
	protected void start() {
		if (isEnable())
			task = BukkitManager.getScheduler().runTaskTimer(RulesPlugin.instance(), this, 0, getPeriod());
	}

	/**
	 * Unregister this game rule from the Bukkit scheduler.
	 */
	protected void stop() {
		if (task != null)
			BukkitManager.getScheduler().cancelTask(task.getTaskId());
	}
}
