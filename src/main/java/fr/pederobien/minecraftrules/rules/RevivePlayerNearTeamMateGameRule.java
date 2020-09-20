package fr.pederobien.minecraftrules.rules;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.pederobien.minecraftgameplateform.dictionary.ECommonMessageCode;
import fr.pederobien.minecraftgameplateform.helpers.TeamHelper;
import fr.pederobien.minecraftrules.EGameRuleMessageCode;
import fr.pederobien.minecraftrules.impl.EventGameRule;

public class RevivePlayerNearTeamMateGameRule extends EventGameRule<Boolean> {

	public RevivePlayerNearTeamMateGameRule() {
		super("revivePlayerNearTeamMate", false, Boolean.class, EGameRuleMessageCode.REVIVE_PLAYER_NEAR_TEAMMATE__EXPLANATION);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (!isRunning() || !getValue())
			return;

		Optional<Player> optPlayer = TeamHelper.getRandomColleagues(event.getPlayer(), player -> player.getGameMode().equals(GameMode.SURVIVAL));
		if (optPlayer.isPresent())
			event.setRespawnLocation(optPlayer.get().getLocation());
	}

	@Override
	protected boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
			sendSynchro(sender, EGameRuleMessageCode.COMMON_VALUE_DEFINED_IN_GAME, getName(), value);
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
}
