package fr.pederobien.minecraft.rules.impl;

import java.util.List;

import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraft.rules.EGameRuleMessageCode;
import fr.pederobien.minecraftgameplateform.dictionary.ECommonMessageCode;

public class NaturalRegenerationGameRule extends EventGameRule<Boolean> {

	public NaturalRegenerationGameRule() {
		super("naturalRegeneration", true, Boolean.class, EGameRuleMessageCode.NATURAL_REGENERATION__EXPLANATION);
	}

	@Override
	public void setValue(Boolean value) {
		super.setValue(value);
		if (isRunning())
			setGameRule(GameRule.NATURAL_REGENERATION, getValue());
	}

	@Override
	public void start() {
		super.start();
		setGameRule(GameRule.NATURAL_REGENERATION, getValue());
	}

	@Override
	public void stop() {
		super.stop();
		setGameRule(GameRule.NATURAL_REGENERATION, getDefaultValue());
	}

	@Override
	protected boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			if (args[0].equals("true"))
				setValue(true);
			else if (args[0].equals("false"))
				setValue(false);
			else {
				sendNotSynchro(sender, ECommonMessageCode.COMMON_BAD_BOOLEAN_FORMAT);
				return false;
			}
			sendSynchro(sender, EGameRuleMessageCode.COMMON_VALUE_DEFINED_IN_GAME, getName(), getValue());
		} catch (IndexOutOfBoundsException e) {
			sendNotSynchro(sender, EGameRuleMessageCode.COMMON_VALUE_IS_MISSING, getName());
			return false;
		}
		return true;
	}

	@Override
	protected List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return filter(asList("true", "false").stream(), args);
	}
}
