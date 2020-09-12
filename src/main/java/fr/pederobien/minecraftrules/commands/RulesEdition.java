package fr.pederobien.minecraftrules.commands;

import fr.pederobien.minecraftgameplateform.impl.editions.AbstractSimpleMapEdition;
import fr.pederobien.minecraftrules.EGameRuleMessageCode;
import fr.pederobien.minecraftrules.impl.GameRule;
import fr.pederobien.minecraftrules.interfaces.IGameRule;

public class RulesEdition extends AbstractSimpleMapEdition {

	public RulesEdition() {
		super("rules", EGameRuleMessageCode.RULES__EXPLANATION);
		for (IGameRule<?> rule : GameRule.RULES)
			addEdition(rule.getEdition());
	}
}
