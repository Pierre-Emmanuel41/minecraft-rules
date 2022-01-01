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
import fr.pederobien.utils.IPausable.PausableState;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;

public class PvpGameRule extends Rule<Boolean> implements IEventListener {
	private boolean registered;
	private PvpTimeLineObserver timelineObserver;

	/**
	 * Creates a game rule to enable or disable PVP while the game is in progress. This rule works if and only if the given game
	 * implements the {@link IPvpTimeConfigurable} interface. Then according to the value, the PVP will be enabled either at the
	 * beginning of the game or after a certain time of play.
	 * 
	 * @param game The game associated to this rule.
	 */
	public PvpGameRule(IGame game) {
		super(game, "pvp", false, ERuleCode.GAME_RULE__PVP__EXPLANATION);
		timelineObserver = new PvpTimeLineObserver();
		EventManager.registerListener(this);
	}

	@Override
	public void setValue(Boolean value) {
		super.setValue(value);

		if (!isEnable() || !(getGame() instanceof IPvpTimeConfigurable) || getGame().getState() == PausableState.NOT_STARTED)
			return;

		IPvpTimeConfigurable pvp = (IPvpTimeConfigurable) getGame();

		// Case 1: Rule registered for the PVP time and finally disabling the PVP
		if (registered && !value)
			Platform.get(getGame().getPlugin()).getTimeLine().unregister(pvp.getPvpTime().get(), timelineObserver);

		// Case 2: Rule not registered for the PVP time and finally enabling the PVP
		else if (!registered && value)
			Platform.get(getGame().getPlugin()).getTimeLine().register(pvp.getPvpTime().get(), timelineObserver);
	}

	@EventHandler
	private void onGameStart(GameStartPostEvent event) {
		if (!isEnable() || !event.getGame().equals(getGame()) || !getValue() || !(event.getGame() instanceof IPvpTimeConfigurable))
			return;

		IPvpTimeConfigurable pvp = (IPvpTimeConfigurable) event.getGame();
		Platform.get(getGame().getPlugin()).getTimeLine().register(pvp.getPvpTime().get(), timelineObserver);
		registered = true;
	}

	@EventHandler
	private void onGameStop(GameStopPostEvent event) {
		if (!isEnable() || !event.getGame().equals(getGame()) || !(event.getGame() instanceof IPvpTimeConfigurable))
			return;

		IPvpTimeConfigurable pvp = (IPvpTimeConfigurable) event.getGame();
		Platform.get(event.getGame().getPlugin()).getTimeLine().unregister(pvp.getPvpTime().get(), timelineObserver);
	}

	@EventHandler
	private void onPvpTimeChange(ConfigurableValueChangeEvent event) {
		if (!isEnable() || getGame().getState() == PausableState.NOT_STARTED)
			return;

		if (!(getGame() instanceof IPvpTimeConfigurable) || ((IPvpTimeConfigurable) getGame()).getPvpTime().equals(event.getConfigurable()))
			return;

		IPvpTimeConfigurable pvp = (IPvpTimeConfigurable) getGame();
		ITimeLine timeLine = Platform.get(getGame().getPlugin()).getTimeLine();
		timeLine.unregister((LocalTime) event.getOldValue(), timelineObserver);
		timeLine.register(pvp.getPvpTime().get(), timelineObserver);
	}
}
