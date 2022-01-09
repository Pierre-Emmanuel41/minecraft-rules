package fr.pederobien.minecraft.rules.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import fr.pederobien.minecraft.game.exceptions.TeamAlreadyRegisteredException;
import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.rules.event.RuleListRuleAddPostEvent;
import fr.pederobien.minecraft.rules.event.RuleListRuleRemovePostEvent;
import fr.pederobien.minecraft.rules.exceptions.RuleAlreadyRegisteredException;
import fr.pederobien.minecraft.rules.interfaces.IRule;
import fr.pederobien.minecraft.rules.interfaces.IRuleList;
import fr.pederobien.utils.event.EventManager;

public class RuleList implements IRuleList {
	private String name;
	private Map<String, IRule<?>> rules;
	private Lock lock;
	private PvpGameRule pvpGameRule;
	private MaxProtectionOnDiamondsGameRule maxProtectionOnDiamondsGameRule;
	private RevivePlayerNearTeamMatesGameRule revivePlayerNearTeamMatesGameRule;
	private AnnounceAdvancementsGameRule announceAdvancementsGameRule;
	private NaturalRegenerationGameRule naturalRegenerationGameRule;
	private MobsNotAllowedToSpawnGameRule mobsNotAllowedToSpawnGameRule;
	private DisplayTeamMatesLocationGameRule displayTeamMatesLocationGameRule;

	public RuleList(IGame game) {
		this.name = game.getName();

		rules = new LinkedHashMap<String, IRule<?>>();
		lock = new ReentrantLock(true);
		pvpGameRule = new PvpGameRule(game);
		add(maxProtectionOnDiamondsGameRule = new MaxProtectionOnDiamondsGameRule(game));
		add(revivePlayerNearTeamMatesGameRule = new RevivePlayerNearTeamMatesGameRule(game));
		add(announceAdvancementsGameRule = new AnnounceAdvancementsGameRule(game));
		add(naturalRegenerationGameRule = new NaturalRegenerationGameRule(game));
		add(mobsNotAllowedToSpawnGameRule = new MobsNotAllowedToSpawnGameRule(game));
		add(displayTeamMatesLocationGameRule = new DisplayTeamMatesLocationGameRule(game));
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public <T> void add(IRule<T> rule) {
		addRule(rule);
		EventManager.callEvent(new RuleListRuleAddPostEvent(this, rule));
	}

	@Override
	public <T> IRule<T> remove(String name) {
		IRule<T> rule = removeRule(name);
		if (rule != null)
			EventManager.callEvent(new RuleListRuleRemovePostEvent(this, rule));
		return rule;
	}

	@Override
	public <T> boolean remove(IRule<T> rule) {
		return removeRule(rule.getName()) != null;
	}

	@Override
	public void clear() {
		lock.lock();
		try {
			Set<String> names = new HashSet<String>(rules.keySet());
			for (String name : names) {
				IRule<?> team = rules.remove(name);
				EventManager.callEvent(new RuleListRuleRemovePostEvent(this, team));
			}
		} finally {
			lock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<IRule<T>> getRule(String name) {
		return Optional.ofNullable((IRule<T>) rules.get(name));
	}

	@Override
	public Stream<IRule<?>> stream() {
		return rules.values().stream();
	}

	@Override
	public List<IRule<?>> toList() {
		return new ArrayList<IRule<?>>(rules.values());
	}

	@Override
	public PvpGameRule getPvpGameRule() {
		return pvpGameRule;
	}

	@Override
	public MaxProtectionOnDiamondsGameRule getMaxProtectionOnDiamondsGameRule() {
		return maxProtectionOnDiamondsGameRule;
	}

	@Override
	public RevivePlayerNearTeamMatesGameRule getRevivePlayerNearTeamMatesGameRule() {
		return revivePlayerNearTeamMatesGameRule;
	}

	@Override
	public AnnounceAdvancementsGameRule getAnnounceAdvancementsGameRule() {
		return announceAdvancementsGameRule;
	}

	@Override
	public NaturalRegenerationGameRule getNaturalRegenerationGameRule() {
		return naturalRegenerationGameRule;
	}

	@Override
	public MobsNotAllowedToSpawnGameRule getMobsNotAllowedToSpawnGameRule() {
		return mobsNotAllowedToSpawnGameRule;
	}

	@Override
	public DisplayTeamMatesLocationGameRule getDisplayTeamMatesLocationGameRule() {
		return displayTeamMatesLocationGameRule;
	}

	/**
	 * Thread safe operation that adds a rule to the rules list.
	 * 
	 * @param rule The rule to add.
	 * 
	 * @throws TeamAlreadyRegisteredException if a rule is already registered for the rule name.
	 */
	private <T> void addRule(IRule<T> rule) {
		lock.lock();
		try {
			if (rules.get(rule.getName()) != null)
				throw new RuleAlreadyRegisteredException(this, rule);

			rules.put(rule.getName(), rule);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Thread safe operation that removes a rules from the rules list.
	 * 
	 * @param rule The rule to remove.
	 * 
	 * @return The rule associated to the given name if registered, null otherwise.
	 */
	@SuppressWarnings("unchecked")
	private <T> IRule<T> removeRule(String name) {
		lock.lock();
		try {
			return (IRule<T>) rules.remove(name);
		} finally {
			lock.unlock();
		}
	}
}
