package fr.pederobien.minecraft.rules.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.game.interfaces.ITeamConfigurable;
import fr.pederobien.minecraft.game.interfaces.ITeamList;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.utils.IPausable.PausableState;

public class NaturalRegenerationGameRule extends EventRule<Boolean> {
	private static final Parser<Boolean> PARSER = new Parser<Boolean>(value -> value.toString(), value -> Boolean.parseBoolean(value));

	/**
	 * Creates a rule in order to enable or disable the natural regeneration while the game is in progress.
	 * 
	 * @param game The game associated to this rule.
	 */
	public NaturalRegenerationGameRule(IGame game) {
		super(game, "naturalRegeneration", true, ERuleCode.GAME_RULE__NATURAL_REGENERATION__EXPLANATION, PARSER);
	}

	@EventHandler
	private void onPlayerRegainHealth(EntityRegainHealthEvent event) {
		if (!(event.getEntity() instanceof Player) || getGame().getState() == PausableState.NOT_STARTED || !(getGame() instanceof ITeamConfigurable))
			return;

		ITeamList teams = ((ITeamConfigurable) getGame()).getTeams();
		if (!teams.getTeam((Player) event.getEntity()).isPresent())
			return;

		if (!getValue() || event.getRegainReason() != RegainReason.SATIATED)
			return;

		event.setCancelled(true);
	}
}
