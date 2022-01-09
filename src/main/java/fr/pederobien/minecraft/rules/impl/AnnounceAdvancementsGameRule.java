package fr.pederobien.minecraft.rules.impl;

import org.bukkit.GameRule;

import fr.pederobien.minecraft.game.event.GameStartPostEvent;
import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.utils.IPausable.PausableState;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;

public class AnnounceAdvancementsGameRule extends Rule<Boolean> implements IEventListener {
	private static final Parser<Boolean> PARSER = new Parser<Boolean>(value -> value.toString(), value -> Boolean.parseBoolean(value));

	/**
	 * Creates a rule to enable or disable the players advancements announcement while the game is in progress.
	 * 
	 * @param game The game associated to this rule.
	 */
	public AnnounceAdvancementsGameRule(IGame game) {
		super(game, "announceAdvancements", true, ERuleCode.GAME_RULE__ANNOUNCE_ADVANCEMENTS__EXPLANATION, PARSER);
		EventManager.registerListener(this);
	}

	@Override
	public void setValue(Boolean value) {
		super.setValue(value);

		if (getGame().getState() != PausableState.NOT_STARTED)
			setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, getValue());
	}

	@EventHandler
	private void onGameStart(GameStartPostEvent event) {
		if (!event.getGame().equals(getGame()))
			return;

		setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, getValue());
	}

	@EventHandler
	private void onGameStop(GameStartPostEvent event) {
		if (!event.getGame().equals(getGame()))
			return;

		setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, getDefaultValue());
	}
}
