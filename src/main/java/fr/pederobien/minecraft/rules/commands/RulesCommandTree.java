package fr.pederobien.minecraft.rules.commands;

import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.GameRule;
import org.bukkit.enchantments.Enchantment;

import fr.pederobien.minecraft.commandtree.impl.MinecraftCodeRootNode;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftCodeRootNode;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.minecraft.rules.interfaces.IRule;
import fr.pederobien.minecraft.rules.interfaces.IRuleConfigurable;
import fr.pederobien.minecraft.rules.interfaces.IRuleList;
import fr.pederobien.utils.event.IEventListener;

public class RulesCommandTree implements IEventListener {
	private IMinecraftCodeRootNode root;
	private Supplier<IRuleConfigurable> configurable;
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
	public RulesCommandTree(Supplier<IRuleConfigurable> configurable) {
		this.configurable = configurable;

		root = new MinecraftCodeRootNode("rules", ERuleCode.RULES__EXPLANATION, () -> configurable.get() != null);
		root.add(pvpNode = new PvpRuleNode(() -> getRule(rules -> rules.getPvpGameRule())));
		root.add(maxProtectionOnDiamondsNode = new MaxProtectionOnDiamondsRuleNode(() -> getRule(rules -> rules.getMaxProtectionOnDiamondsGameRule())));
		root.add(revivePlayerNearTeamMateNode = new RevivePlayerNearTeamMateRuleNode(() -> getRule(rules -> rules.getRevivePlayerNearTeamMatesGameRule())));
		root.add(announceAdvancementsNode = new AnnounceAdvancementsRuleNode(() -> getRule(rules -> rules.getAnnounceAdvancementsGameRule())));
		root.add(naturalRegenerationNode = new NaturalRegenerationRuleNode(() -> getRule(rules -> rules.getNaturalRegenerationGameRule())));
		root.add(mobsNotAllowedToSpawnNode = new MobsNotAllowedToSpawnRuleNode(() -> getRule(rules -> rules.getMobsNotAllowedToSpawnGameRule())));
		root.add(displayTeamMatesLocationNode = new DisplayTeamMatesLocationRuleNode(() -> getRule(rules -> rules.getDisplayTeamMatesLocationGameRule())));
	}

	/**
	 * @return The list of rules.
	 */
	public IRuleList getRules() {
		IRuleConfigurable rules = configurable.get();
		return rules == null ? null : rules.getRules();
	}

	/**
	 * @return The root of this command tree.
	 */
	public IMinecraftCodeRootNode getRoot() {
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

	private <T extends IRule<?>> T getRule(Function<IRuleList, T> selector) {
		if (getRules() == null)
			return null;
		return selector.apply(getRules());
	}
}
