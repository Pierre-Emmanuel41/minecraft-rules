package fr.pederobien.minecraft.rules.impl;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;

import fr.pederobien.minecraft.game.event.GameStartPostEvent;
import fr.pederobien.minecraft.game.event.GameStopPostEvent;
import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.game.interfaces.IPlayerList;
import fr.pederobien.minecraft.game.interfaces.ITeam;
import fr.pederobien.minecraft.game.interfaces.ITeamConfigurable;
import fr.pederobien.minecraft.managers.MessageManager;
import fr.pederobien.minecraft.managers.MessageManager.DisplayOption;
import fr.pederobien.minecraft.managers.MessageManager.TitleMessage;
import fr.pederobien.minecraft.managers.TeamManager;
import fr.pederobien.minecraft.managers.TeamManager.ColleagueInfo;
import fr.pederobien.minecraft.managers.WorldManager;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.utils.IPausable.PausableState;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;

public class DisplayTeamMatesLocationGameRule extends PeriodicGameRule<Boolean> implements IEventListener {

	/**
	 * Creates a rule to enable or disable the display of player's team mates location while the game is in progress.
	 * 
	 * @param game The game associated to this rule.
	 */
	public DisplayTeamMatesLocationGameRule(IGame game) {
		super(game, "displayTeamMatesLocation", true, ERuleCode.GAME_RULE__DISPLAY_TEAM_MATES_LOCATION__EXPLANATION);
		setPeriod(10);

		EventManager.registerListener(this);
	}

	@Override
	public void setValue(Boolean value) {
		super.setValue(value);

		if (getGame().getState() == PausableState.NOT_STARTED)
			return;

		stop();

		if (getValue())
			start();
	}

	@Override
	public void run() {
		if (!getValue() || !(getGame() instanceof ITeamConfigurable)) {
			stop();
			return;
		}

		ITeamConfigurable teams = (ITeamConfigurable) getGame();
		for (ITeam team : teams.getTeams().toList()) {
			IPlayerList players = team.getPlayers();
			for (Player player : players) {
				List<ColleagueInfo> colleagueInfos = TeamManager.getColleaguesInfo(player, p -> p.getGameMode() == GameMode.SURVIVAL).collect(Collectors.toList());

				if (colleagueInfos.isEmpty())
					continue;

				StringJoiner joiner = new StringJoiner(" ");
				for (ColleagueInfo info : colleagueInfos) {
					if (info.isInDifferentWorld())
						joiner.add(info.getColleague().getName() + " : " + WorldManager.getWorldNameNormalised(info.getColleague().getWorld()));
					else
						joiner.add(info.getColleague().getName() + " : " + info.getDistance() + " | " + info.getArrow().getUnicode());
				}

				MessageManager.sendMessage(DisplayOption.ACTION_BAR, player, TitleMessage.of(joiner.toString()));
			}
		}
	}

	@EventHandler
	private void onGameStart(GameStartPostEvent event) {
		if (!event.getGame().equals(getGame()))
			return;

		start();
		setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false);
	}

	@EventHandler
	private void onGameStop(GameStopPostEvent event) {
		if (!event.getGame().equals(getGame()))
			return;

		stop();
		setGameRule(GameRule.SEND_COMMAND_FEEDBACK, true);
	}
}
