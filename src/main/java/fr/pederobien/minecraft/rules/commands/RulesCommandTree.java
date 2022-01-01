package fr.pederobien.minecraft.rules.commands;

import org.bukkit.GameRule;
import org.bukkit.enchantments.Enchantment;

import fr.pederobien.minecraft.commandtree.impl.MinecraftCodeRootNode;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftCodeNode;
import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.impl.AnnounceAdvancementsGameRule;
import fr.pederobien.minecraft.rules.impl.DisplayTeamMatesLocationGameRule;
import fr.pederobien.minecraft.rules.impl.MaxProtectionOnDiamondsGameRule;
import fr.pederobien.minecraft.rules.impl.MobsNotAllowedToSpawnGameRule;
import fr.pederobien.minecraft.rules.impl.NaturalRegenerationGameRule;
import fr.pederobien.minecraft.rules.impl.PvpGameRule;
import fr.pederobien.minecraft.rules.impl.RevivePlayerNearTeamMatesGameRule;
import fr.pederobien.utils.event.IEventListener;

public class RulesCommandTree implements IEventListener {
	private IMinecraftCodeNode root;
	private PvpRuleNode pvpNode;
	private MaxProtectionOnDiamondsRuleNode maxProtectionOnDiamondsNode;
	private RevivePlayerNearTeamMateRuleNode revivePlayerNearTeamMateNode;
	private AnnounceAdvancementsRuleNode announceAdvancementsNode;
	private NaturalRegenerationRuleNode naturalRegenerationNode;
	private MobsNotAllowedToSpawnRuleNode mobsNotAllowedToSpawnNode;
	private DisplayTeamMatesLocationRuleNode displayTeamMatesLocationNode;

	/**
	 * Creates a rule command tree associated to the given game.
	 * 
	 * @param game The game associated to this rules command tree.
	 */
	public RulesCommandTree(IGame game) {
		root = new MinecraftCodeRootNode("rules", ERuleCode.RULES__EXPLANATION, () -> true);
		root.add(pvpNode = new PvpRuleNode(new PvpGameRule(game)));
		root.add(maxProtectionOnDiamondsNode = new MaxProtectionOnDiamondsRuleNode(new MaxProtectionOnDiamondsGameRule(game)));
		root.add(revivePlayerNearTeamMateNode = new RevivePlayerNearTeamMateRuleNode(new RevivePlayerNearTeamMatesGameRule(game)));
		root.add(announceAdvancementsNode = new AnnounceAdvancementsRuleNode(new AnnounceAdvancementsGameRule(game)));
		root.add(naturalRegenerationNode = new NaturalRegenerationRuleNode(new NaturalRegenerationGameRule(game)));
		root.add(mobsNotAllowedToSpawnNode = new MobsNotAllowedToSpawnRuleNode(new MobsNotAllowedToSpawnGameRule(game)));
		root.add(displayTeamMatesLocationNode = new DisplayTeamMatesLocationRuleNode(new DisplayTeamMatesLocationGameRule(game)));
	}

	/**
	 * @return The root of this command tree.
	 */
	public IMinecraftCodeNode getRoot() {
		return root;
	}

	/**
	 * @return The node that modifies the PVP on the server while the game is in progress.
	 */
	public PvpRuleNode getPvpNode() {
		return pvpNode;
	}

	/**
	 * @return The node that modifies the level max of the {@link Enchantment#PROTECTION_ENVIRONMENTAL} while the game is in progress.
	 */
	public MaxProtectionOnDiamondsRuleNode getMaxProtectionOnDiamondsNode() {
		return maxProtectionOnDiamondsNode;
	}

	/**
	 * @return The node that modifies where players revive while the game is in progress.
	 */
	public RevivePlayerNearTeamMateRuleNode getRevivePlayerNearTeamMateNode() {
		return revivePlayerNearTeamMateNode;
	}

	/**
	 * @return The node that modifies the {@link GameRule#ANNOUNCE_ADVANCEMENTS} value while the game is in progress.
	 */
	public AnnounceAdvancementsRuleNode getAnnounceAdvancementsNode() {
		return announceAdvancementsNode;
	}

	/**
	 * @return The node that modifies the {@link GameRule#NATURAL_REGENERATION} value while the game is in progress.
	 */
	public NaturalRegenerationRuleNode getNaturalRegenerationNode() {
		return naturalRegenerationNode;
	}

	/**
	 * @return The node that modifies the list of mobs that are not allowed to spawn while the game is in progress.
	 */
	public MobsNotAllowedToSpawnRuleNode getMobsNotAllowedToSpawnNode() {
		return mobsNotAllowedToSpawnNode;
	}

	/**
	 * @return The node that specifies if player's team mates locations are displayed while the game is in progress.
	 */
	public DisplayTeamMatesLocationRuleNode getDisplayTeamMatesLocationNode() {
		return displayTeamMatesLocationNode;
	}
}
