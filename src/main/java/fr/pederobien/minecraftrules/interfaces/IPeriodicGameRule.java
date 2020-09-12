package fr.pederobien.minecraftrules.interfaces;

public interface IPeriodicGameRule<T> extends IRunnableGameRule<T>, Runnable {

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
