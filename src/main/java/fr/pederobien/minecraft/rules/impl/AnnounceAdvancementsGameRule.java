package fr.pederobien.minecraft.rules.impl;

import java.util.Arrays;
import java.util.List;

import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraft.rules.EGameRuleMessageCode;
import fr.pederobien.minecraftgameplateform.dictionary.ECommonMessageCode;

public class AnnounceAdvancementsGameRule extends RunnableGameRule<Boolean> {

	public AnnounceAdvancementsGameRule() {
		super("announceAdvancements", true, Boolean.class, EGameRuleMessageCode.ANNOUNCE_ADVANCEMENTS_GAME_RULE__EXPLANATION);
	}

	@Override
	public void setValue(Boolean value) {
		super.setValue(value);
		if (isRunning())
			setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, getValue());
	}

	@Override
	public void start() {
		super.start();
		setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, getValue());
	}

	@Override
	public void stop() {
		super.stop();
		setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, getDefaultValue());
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
