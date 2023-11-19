package fr.pederobien.minecraft.rules.impl;

import java.time.LocalTime;
import java.util.function.Consumer;

import fr.pederobien.minecraft.commandtree.interfaces.ICodeSender;
import fr.pederobien.minecraft.dictionary.impl.MinecraftMessageEvent.MinecraftMessageEventBuilder;
import fr.pederobien.minecraft.dictionary.impl.PlayerGroup;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.dictionary.interfaces.IPlayerGroup;
import fr.pederobien.minecraft.game.event.GameStartPostEvent;
import fr.pederobien.minecraft.game.event.GameStopPostEvent;
import fr.pederobien.minecraft.game.impl.time.CountDown;
import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.game.interfaces.ITeam;
import fr.pederobien.minecraft.game.interfaces.ITeamConfigurable;
import fr.pederobien.minecraft.game.interfaces.ITeamList;
import fr.pederobien.minecraft.game.interfaces.time.ICountDown;
import fr.pederobien.minecraft.game.interfaces.time.IObsTimeLine;
import fr.pederobien.minecraft.game.interfaces.time.ITimeLine;
import fr.pederobien.minecraft.managers.EColor;
import fr.pederobien.minecraft.managers.MessageManager.DisplayOption;
import fr.pederobien.minecraft.managers.WorldManager;
import fr.pederobien.minecraft.platform.Platform;
import fr.pederobien.minecraft.platform.event.ConfigurableValueChangeEvent;
import fr.pederobien.minecraft.platform.interfaces.IPvpTimeConfigurable;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.event.RuleChangePostEvent;
import fr.pederobien.utils.IPausable.PausableState;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.IEventListener;

public class PvpTimeLineObserver implements IObsTimeLine, ICodeSender, IEventListener {
	private IGame game;
	private PvpGameRule pvpRule;
	private Consumer<Integer> countDownAction;
	private Consumer<LocalTime> onTimeAction;
	private ICountDown countDown;
	private boolean pvpEnable;

	/**
	 * Creates a time line observer associated to the given game.
	 * 
	 * @param game The game associated to this observer.
	 * @param the  PVP game rule associated to this observer.
	 */
	public PvpTimeLineObserver(IGame game, PvpGameRule pvpRule) {
		this.game = game;
		this.pvpRule = pvpRule;

		// Action during the count down
		countDownAction = count -> send(ERuleCode.GAME_RULE__PVP__COUNT_DOWN, EColor.GOLD, DisplayOption.TITLE, count);

		// Action when the count down is over
		onTimeAction = time -> setPvp(true);

		// Dummy initialization, will be update when the game is starting
		pvpEnable = true;
	}

	@Override
	public ICountDown getCountDown() {
		return countDown;
	}

	@Override
	public LocalTime getNextTime() {
		return null;
	}

	@EventHandler
	private void onGameStart(GameStartPostEvent event) {
		if (!event.getGame().equals(game) || !pvpRule.getValue() || !(event.getGame() instanceof IPvpTimeConfigurable))
			return;

		setPvp(false);

		ITimeLine timeLine = Platform.get(game.getPlugin()).getTimeLine();
		LocalTime pvpTime = ((IPvpTimeConfigurable) game).getPvpTime().get();
		LocalTime modified = pvpTime.toSecondOfDay() < 10 ? LocalTime.of(0, 0, 10) : pvpTime;
		countDown = new CountDown(5, countDownAction, onTimeAction);
		send("PvpTimeLineObserver - onGameStart");
		timeLine.register(modified, this);
	}

	@EventHandler
	private void onGameStop(GameStopPostEvent event) {
		if (!event.getGame().equals(game) || !pvpRule.getValue() || !(event.getGame() instanceof IPvpTimeConfigurable))
			return;

		LocalTime pvpTime = ((IPvpTimeConfigurable) event.getGame()).getPvpTime().get();
		Platform.get(event.getGame().getPlugin()).getTimeLine().unregister(pvpTime, this);
		setPvp(false);
	}

	@EventHandler
	private void onPvpTimeChange(ConfigurableValueChangeEvent event) {
		if (game.getState() == PausableState.NOT_STARTED || !pvpRule.getValue())
			return;

		if (!(game instanceof IPvpTimeConfigurable) || !((IPvpTimeConfigurable) game).getPvpTime().equals(event.getConfigurable()))
			return;

		ITimeLine timeLine = Platform.get(game.getPlugin()).getTimeLine();
		LocalTime oldPvpTime = (LocalTime) event.getOldValue();
		LocalTime newPvpTime = (LocalTime) event.getConfigurable().get();

		// Unregistering the observer for the old PVP time value
		timeLine.unregister(oldPvpTime, this);

		if (oldPvpTime.compareTo(newPvpTime) <= 0)
			setPvp(false);

		int pvpTimeSecond = newPvpTime.toSecondOfDay();
		int gameTimeSecond = timeLine.getTimeTask().getGameTime().toSecondOfDay();

		int difference = pvpTimeSecond - gameTimeSecond;

		// Enabling the rule after the PVP time
		if (difference < 0)
			setPvp(true);
		else {
			// Enabling the rule during/before the count down
			countDown = new CountDown(difference < 5 ? difference : 5, countDownAction, onTimeAction);
			timeLine.register(newPvpTime, this);
		}
	}

	@EventHandler
	private void onValueChange(RuleChangePostEvent<Boolean> event) {
		if (!event.getRule().equals(pvpRule))
			return;

		// Game not started yet or stopped
		// Game does not have a PVP time value
		if (game.getState() == PausableState.NOT_STARTED || !(game instanceof IPvpTimeConfigurable))
			return;

		ITimeLine timeLine = Platform.get(game.getPlugin()).getTimeLine();
		LocalTime pvpTime = ((IPvpTimeConfigurable) game).getPvpTime().get();

		// Rule enabled then disabled
		if (event.getOldValue() && !event.getRule().getValue()) {
			timeLine.unregister(pvpTime, this);
			setPvp(false);
		}
		// Rule disabled then enabled
		else if (!event.getOldValue() && event.getRule().getValue()) {
			int pvpTimeSecond = pvpTime.toSecondOfDay();
			int gameTimeSecond = timeLine.getTimeTask().getGameTime().toSecondOfDay();

			int difference = pvpTimeSecond - gameTimeSecond;

			// Enabling the rule after the PVP time
			if (difference < 0)
				setPvp(true);
			else {
				// Enabling the rule during/before the count down
				countDown = new CountDown(difference < 5 ? difference : 5, countDownAction, onTimeAction);
				timeLine.register(pvpTime, this);
			}
		}
	}

	/**
	 * The value of PVP in the three minecraft dimension : Overworld, Nether and End.
	 * 
	 * @param value True to enable the PVP, false otherwise.
	 */
	private void setPvp(boolean pvpEnable) {
		if (this.pvpEnable == pvpEnable)
			return;

		this.pvpEnable = pvpEnable;
		WorldManager.OVERWORLD.setPVP(pvpEnable);
		WorldManager.NETHER_WORLD.setPVP(pvpEnable);
		WorldManager.END_WORLD.setPVP(pvpEnable);

		send(pvpEnable ? ERuleCode.GAME_RULE__PVP__PVP_ENABLED : ERuleCode.GAME_RULE__PVP__PVP_DISABLED, EColor.DARK_RED, DisplayOption.CONSOLE);
	}

	private void send(IMinecraftCode code, EColor color, DisplayOption displayOption, Object... args) {
		MinecraftMessageEventBuilder builder = eventBuilder(code);
		builder.withDisplayOption(displayOption).withColor(color);

		if (game instanceof ITeamConfigurable) {
			ITeamList teams = ((ITeamConfigurable) game).getTeams();
			IPlayerGroup group = PlayerGroup.of("Players", player -> {
				for (ITeam team : teams)
					if (team.getPlayers().getPlayer(player.getName()).isPresent())
						return true;
				return false;
			});

			send(builder.withGroup(group).build(args));
		}
	}
}
