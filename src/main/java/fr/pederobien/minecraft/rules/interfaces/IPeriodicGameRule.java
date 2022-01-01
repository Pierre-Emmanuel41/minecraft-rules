package fr.pederobien.minecraft.rules.interfaces;

public interface IPeriodicGameRule<T> extends IRule<T>, Runnable {

	/**
	 * @return The number of ticks between two runs.
	 */
	long getPeriod();

	/**
	 * Set the number of ticks between two runs.
	 * 
	 * @param period The number of ticks.
	 */
	void setPeriod(long period);
}
