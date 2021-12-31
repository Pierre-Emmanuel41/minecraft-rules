package fr.pederobien.minecraft.rules.commands;

import fr.pederobien.minecraft.rules.EGameRuleMessageCode;
import fr.pederobien.minecraft.rules.impl.GameRule;
import fr.pederobien.minecraft.rules.interfaces.IGameRule;
import fr.pederobien.minecraftgameplateform.impl.editions.AbstractSimpleMapEdition;

public class RulesEdition extends AbstractSimpleMapEdition {

	public RulesEdition() {
		super("rules", EGameRuleMessageCode.RULES__EXPLANATION);
		for (IGameRule<?> rule : GameRule.RULES)
			addEdition(rule.getEdition());
	}
}
