package fr.pederobien.minecraft.rules.impl;

import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.platform.interfaces.IPvpTimeConfigurable;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.utils.event.EventManager;

public class PvpGameRule extends Rule<Boolean> {
	private static final Parser<Boolean> PARSER = new Parser<Boolean>(value -> value.toString(), value -> Boolean.parseBoolean(value));

	/**
	 * Creates a game rule to enable or disable PVP while the game is in progress. This rule works if and only if the given game
	 * implements the {@link IPvpTimeConfigurable} interface. Then according to the value, the PVP will be enabled either at the
	 * beginning of the game or after a certain time of play.
	 * 
	 * @param game The game associated to this rule.
	 */
	public PvpGameRule(IGame game) {
		super(game, "pvp", false, ERuleCode.GAME_RULE__PVP__EXPLANATION, PARSER);
		EventManager.registerListener(new PvpTimeLineObserver(game, this));
	}
}
