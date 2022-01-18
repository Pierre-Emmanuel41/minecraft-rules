package fr.pederobien.minecraft.rules.impl;

import java.time.LocalTime;

import fr.pederobien.minecraft.game.event.GameStartPostEvent;
import fr.pederobien.minecraft.game.event.GameStopPostEvent;
import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.game.interfaces.time.ITimeLine;
import fr.pederobien.minecraft.platform.Platform;
import fr.pederobien.minecraft.platform.event.ConfigurableValueChangeEvent;
import fr.pederobien.minecraft.platform.interfaces.IPvpTimeConfigurable;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.event.RuleChangePostEvent;
import fr.pederobien.utils.IPausable.PausableState;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;

public class PvpGameRule extends Rule<Boolean> implements IEventListener {
	private static final Parser<Boolean> PARSER = new Parser<Boolean>(value -> value.toString(), value -> Boolean.parseBoolean(value));
	private PvpTimeLineObserver pvpTimeObserver;

	/**
	 * Creates a game rule to enable or disable PVP while the game is in progress. This rule works if and only if the given game
	 * implements the {@link IPvpTimeConfigurable} interface. Then according to the value, the PVP will be enabled either at the
	 * beginning of the game or after a certain time of play.
	 * 
	 * @param game The game associated to this rule.
	 */
	public PvpGameRule(IGame game) {
		super(game, "pvp", false, ERuleCode.GAME_RULE__PVP__EXPLANATION, PARSER);
		EventManager.registerListener(this);
	}

	@EventHandler
	private void onGameStart(GameStartPostEvent event) {
		if (!event.getGame().equals(getGame()) || !getValue() || !(event.getGame() instanceof IPvpTimeConfigurable))
			return;

		ITimeLine timeLine = Platform.get(getGame().getPlugin()).getTimeLine();
		LocalTime pvpTime = ((IPvpTimeConfigurable) getGame()).getPvpTime().get();
		LocalTime realPvpTime = pvpTime.equals(LocalTime.MIN) ? LocalTime.of(0, 0, 1) : pvpTime;
		timeLine.register(realPvpTime, pvpTimeObserver = new PvpTimeLineObserver(pvpTime.toSecondOfDay() < 5 ? pvpTime.toSecondOfDay() : 5, getGame()));
	}

	@EventHandler
	private void onGameStop(GameStopPostEvent event) {
		if (!event.getGame().equals(getGame()) || !getValue() || !(event.getGame() instanceof IPvpTimeConfigurable))
			return;

		LocalTime pvpTime = ((IPvpTimeConfigurable) event.getGame()).getPvpTime().get();
		Platform.get(event.getGame().getPlugin()).getTimeLine().unregister(pvpTime, pvpTimeObserver);
	}

	@EventHandler
	private void onPvpTimeChange(ConfigurableValueChangeEvent event) {
		if (getGame().getState() == PausableState.NOT_STARTED || !getValue())
			return;

		if (!(getGame() instanceof IPvpTimeConfigurable) || !((IPvpTimeConfigurable) getGame()).getPvpTime().equals(event.getConfigurable()))
			return;

		ITimeLine timeLine = Platform.get(getGame().getPlugin()).getTimeLine();
		LocalTime oldPvpTime = (LocalTime) event.getOldValue();
		LocalTime newPvpTime = (LocalTime) event.getConfigurable().get();

		// Unregistering the observer for the old PVP time value
		timeLine.unregister(oldPvpTime, pvpTimeObserver);
		if (oldPvpTime.compareTo(newPvpTime) <= 0)
			pvpTimeObserver.setPvp(false);

		int pvpTimeSecond = newPvpTime.toSecondOfDay();
		int gameTimeSecond = timeLine.getTimeTask().getGameTime().toSecondOfDay();

		int difference = pvpTimeSecond - gameTimeSecond;

		// Enabling the rule after the PVP time
		if (difference < 0) {
			pvpTimeObserver = new PvpTimeLineObserver(0, getGame());
			pvpTimeObserver.setPvp(true);
		} else
			// Enabling the rule during/before the count down
			timeLine.register(newPvpTime, pvpTimeObserver = new PvpTimeLineObserver(difference < 5 ? difference : 5, getGame()));
	}

	@EventHandler
	private void onValueChange(RuleChangePostEvent<Boolean> event) {
		if (!event.getRule().equals(this))
			return;

		// Game not started yet or stopped
		// Game does not have a PVP time value
		if (getGame().getState() == PausableState.NOT_STARTED || !(getGame() instanceof IPvpTimeConfigurable))
			return;

		ITimeLine timeLine = Platform.get(getGame().getPlugin()).getTimeLine();
		LocalTime pvpTime = ((IPvpTimeConfigurable) getGame()).getPvpTime().get();

		// Rule enabled then disabled
		if (event.getOldValue() && !event.getRule().getValue()) {
			if (pvpTimeObserver == null)
				return;

			timeLine.unregister(pvpTime, pvpTimeObserver);
			pvpTimeObserver.setPvp(false);
		}
		// Rule disabled then enabled
		else if (!event.getOldValue() && event.getRule().getValue()) {
			int pvpTimeSecond = pvpTime.toSecondOfDay();
			int gameTimeSecond = timeLine.getTimeTask().getGameTime().toSecondOfDay();

			int difference = pvpTimeSecond - gameTimeSecond;

			// Enabling the rule after the PVP time
			if (difference < 0) {
				pvpTimeObserver = new PvpTimeLineObserver(0, getGame());
				pvpTimeObserver.setPvp(true);
			} else
				// Enabling the rule during/before the count down
				timeLine.register(pvpTime, pvpTimeObserver = new PvpTimeLineObserver(difference < 5 ? difference : 5, getGame()));
		}
	}
}
