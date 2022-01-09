package fr.pederobien.minecraft.rules.impl;

import org.bukkit.GameRule;

import fr.pederobien.minecraft.game.event.GameStartPostEvent;
import fr.pederobien.minecraft.game.event.GameStopPostEvent;
import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.utils.IPausable.PausableState;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;

public class NaturalRegenerationGameRule extends Rule<Boolean> implements IEventListener {
	private static final Parser<Boolean> PARSER = new Parser<Boolean>(value -> value.toString(), value -> Boolean.parseBoolean(value));

	/**
	 * Creates a rule in order to enable or disable the natural regeneration while the game is in progress.
	 * 
	 * @param game The game associated to this rule.
	 */
	public NaturalRegenerationGameRule(IGame game) {
		super(game, "naturalRegeneration", true, ERuleCode.GAME_RULE__NATURAL_REGENERATION__EXPLANATION, PARSER);
		EventManager.registerListener(this);
	}

	@Override
	public void setValue(Boolean value) {
		super.setValue(value);

		if (getGame().getState() != PausableState.NOT_STARTED)
			setGameRule(GameRule.NATURAL_REGENERATION, getValue());
	}

	@EventHandler
	private void onGameStart(GameStartPostEvent event) {
		if (!event.getGame().equals(getGame()))
			return;

		setGameRule(GameRule.NATURAL_REGENERATION, getValue());
	}

	@EventHandler
	private void onGameStop(GameStopPostEvent event) {
		if (!event.getGame().equals(getGame()))
			return;

		setGameRule(GameRule.NATURAL_REGENERATION, getDefaultValue());
	}
}
