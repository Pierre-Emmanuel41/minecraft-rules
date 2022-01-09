package fr.pederobien.minecraft.rules.impl;

import java.util.Optional;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.pederobien.minecraft.game.impl.TeamHelper;
import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.game.interfaces.ITeamConfigurable;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.utils.IPausable.PausableState;

public class RevivePlayerNearTeamMatesGameRule extends EventGameRule<Boolean> {
	private static final Parser<Boolean> PARSER = new Parser<Boolean>(value -> value.toString(), value -> Boolean.parseBoolean(value));

	/**
	 * Creates a game rule to specify where players revive while the game is in progress.
	 * 
	 * @param game The game associated to this rule.
	 */
	public RevivePlayerNearTeamMatesGameRule(IGame game) {
		super(game, "revivePlayerNearTeamMate", false, ERuleCode.GAME_RULE__REVIVE_PLAYER_NEAR_TEAM_MATES__EXPLANATION, PARSER);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private void onPlayerRespawn(PlayerRespawnEvent event) {
		if (getGame().getState() == PausableState.NOT_STARTED || !getValue() || !(getGame() instanceof ITeamConfigurable))
			return;

		ITeamConfigurable teams = (ITeamConfigurable) getGame();
		Optional<Player> optPlayer = TeamHelper.getRandomColleagues(teams.getTeams(), event.getPlayer(), p -> p.getGameMode() == GameMode.SURVIVAL);
		if (optPlayer.isPresent())
			event.setRespawnLocation(optPlayer.get().getLocation());
	}
}
