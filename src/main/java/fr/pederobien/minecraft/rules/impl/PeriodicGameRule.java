package fr.pederobien.minecraft.rules.impl;

import org.bukkit.scheduler.BukkitTask;

import fr.pederobien.minecraft.rules.RulesPlugin;
import fr.pederobien.minecraft.rules.interfaces.IPeriodicGameRule;
import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftmanagers.BukkitManager;

public abstract class PeriodicGameRule<T> extends RunnableGameRule<T> implements IPeriodicGameRule<T> {
	private long period;
	private BukkitTask task;

	protected PeriodicGameRule(String name, T defaultValue, Class<T> type, IMinecraftMessageCode explanation) {
		super(name, defaultValue, type, explanation);
	}

	@Override
	public void start() {
		super.start();
		task = BukkitManager.getScheduler().runTaskTimer(RulesPlugin.get(), this, 0, getPeriod());
	}

	@Override
	public void stop() {
		super.stop();
		if (task != null)
			BukkitManager.getScheduler().cancelTask(task.getTaskId());
	}

	@Override
	public long getPeriod() {
		return period;
	}

	@Override
	public void setPeriod(long period) {
		this.period = period;

		if (!isRunning())
			return;

		stop();
		start();
	}
}
