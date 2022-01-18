package fr.pederobien.minecraft.rules.impl;

import java.time.LocalTime;
import java.util.function.Consumer;

import fr.pederobien.minecraft.commandtree.interfaces.ICodeSender;
import fr.pederobien.minecraft.dictionary.impl.MinecraftMessageEvent.MinecraftMessageEventBuilder;
import fr.pederobien.minecraft.dictionary.impl.PlayerGroup;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.dictionary.interfaces.IPlayerGroup;
import fr.pederobien.minecraft.game.impl.time.CountDown;
import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.game.interfaces.ITeam;
import fr.pederobien.minecraft.game.interfaces.ITeamConfigurable;
import fr.pederobien.minecraft.game.interfaces.ITeamList;
import fr.pederobien.minecraft.game.interfaces.time.ICountDown;
import fr.pederobien.minecraft.game.interfaces.time.IObsTimeLine;
import fr.pederobien.minecraft.managers.EColor;
import fr.pederobien.minecraft.managers.MessageManager.DisplayOption;
import fr.pederobien.minecraft.managers.WorldManager;
import fr.pederobien.minecraft.rules.ERuleCode;

public class PvpTimeLineObserver implements IObsTimeLine, ICodeSender {
	private IGame game;
	private ICountDown countDown;
	private boolean value;

	/**
	 * Creates a time line observer associated to the given game.
	 * 
	 * @param initialValue The count down associated to this observer.
	 * @param game         The game associated to this observer.
	 */
	public PvpTimeLineObserver(int initialValue, IGame game) {
		this.game = game;

		// Action during the count down
		Consumer<Integer> countDownAction = count -> send(ERuleCode.GAME_RULE__PVP__COUNT_DOWN, EColor.GOLD, DisplayOption.TITLE, count);

		// Action when the count down is over
		Consumer<LocalTime> onTimeAction = time -> setPvp(true);
		countDown = new CountDown(initialValue, countDownAction, onTimeAction);
	}

	@Override
	public ICountDown getCountDown() {
		return countDown;
	}

	@Override
	public LocalTime getNextTime() {
		return null;
	}

	/**
	 * The value of PVP in the three minecraft dimension : Overworld, Nether and End.
	 * 
	 * @param value True to enable the PVP, false otherwise.
	 */
	public void setPvp(boolean value) {
		if (this.value == value)
			return;

		this.value = value;
		WorldManager.OVERWORLD.setPVP(value);
		WorldManager.NETHER_WORLD.setPVP(value);
		WorldManager.END_WORLD.setPVP(value);

		send(value ? ERuleCode.GAME_RULE__PVP__PVP_ENABLED : ERuleCode.GAME_RULE__PVP__PVP_DISABLED, EColor.DARK_RED, DisplayOption.CONSOLE);
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

			builder.withGroup(group);
		}

		send(builder.build(args));
	}
}
