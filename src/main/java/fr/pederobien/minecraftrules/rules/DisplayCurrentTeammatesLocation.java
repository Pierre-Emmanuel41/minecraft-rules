package fr.pederobien.minecraftrules.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.pederobien.minecraftgameplateform.dictionary.ECommonMessageCode;
import fr.pederobien.minecraftgameplateform.interfaces.element.ITeam;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecraftmanagers.MessageManager;
import fr.pederobien.minecraftmanagers.MessageManager.DisplayOption;
import fr.pederobien.minecraftmanagers.MessageManager.TitleMessage;
import fr.pederobien.minecraftmanagers.TeamManager;
import fr.pederobien.minecraftmanagers.TeamManager.ColleagueInfo;
import fr.pederobien.minecraftmanagers.WorldManager;
import fr.pederobien.minecraftrules.EGameRuleMessageCode;
import fr.pederobien.minecraftrules.impl.PeriodicGameRule;

public class DisplayCurrentTeammatesLocation extends PeriodicGameRule<Boolean> {

	public DisplayCurrentTeammatesLocation() {
		super("displayCurrentTeamMatesLocation", true, Boolean.class, EGameRuleMessageCode.DISPLAY_CURRENT_TEAMMATES_LOCATION__EXPLANATION);
		setPeriod(20);
	}

	@Override
	public void setValue(Boolean value) {
		super.setValue(value);
		if (isRunning() && !value)
			stop();
		else if (!isRunning() && Plateform.getGameConfigurationContext().isRunning() && value)
			start();
	}

	@Override
	public void start() {
		super.start();
		setSendCommandFeedBackValue(false);
	}

	@Override
	public void stop() {
		super.stop();
		setSendCommandFeedBackValue(true);
	}

	@Override
	public void run() {
		if (!getValue()) {
			stop();
			return;
		}

		List<ITeam> teams = new ArrayList<ITeam>(Plateform.getGameConfigurationContext().getTeams());
		for (ITeam team : teams) {
			List<Player> players = team.getPlayers();
			for (Player player : players) {
				List<ColleagueInfo> colleagueInfos = TeamManager.getColleaguesInfo(player, p -> p.getGameMode().equals(GameMode.SURVIVAL)).collect(Collectors.toList());

				if (colleagueInfos.isEmpty())
					continue;

				StringJoiner joiner = new StringJoiner(" ");
				for (ColleagueInfo info : colleagueInfos)
					joiner.add(info.getColleague().getName() + " : " + info.getDistance() + " | " + info.getArrow().getUnicode());

				MessageManager.sendMessage(DisplayOption.ACTION_BAR, player, TitleMessage.of(joiner.toString()));
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			String value = args[0];
			if (value.equals("true"))
				setValue(true);
			else if (value.equals("false"))
				setValue(false);
			else {
				sendSynchro(sender, ECommonMessageCode.COMMON_BAD_BOOLEAN_FORMAT);
				return false;
			}
			sendSynchro(sender, EGameRuleMessageCode.COMMON_VALUE_DEFINED_IN_GAME, getName(), getValue());
		} catch (IndexOutOfBoundsException e) {
			sendSynchro(sender, EGameRuleMessageCode.COMMON_VALUE_IS_MISSING, getName());
			return false;
		}
		return true;
	}

	@Override
	protected List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (args.length) {
		case 1:
			return Arrays.asList("true", "false");
		default:
			return Arrays.asList();
		}
	}

	private void setSendCommandFeedBackValue(boolean value) {
		WorldManager.OVERWORLD.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, value);
		WorldManager.NETHER_WORLD.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, value);
		WorldManager.END_WORLD.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, value);
	}
}
