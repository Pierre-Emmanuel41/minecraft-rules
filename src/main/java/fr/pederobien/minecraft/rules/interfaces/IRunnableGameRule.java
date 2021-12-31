package fr.pederobien.minecraft.rules.interfaces;

public interface IRunnableGameRule<T> extends IGameRule<T> {

	/**
	 * Start running this game rule.
	 */
	void start();

	/**
	 * Stop running this game rule.
	 */
	void stop();

	/**
	 * @return True if this game rule is running, false otherwise.
	 */
	boolean isRunning();
}
