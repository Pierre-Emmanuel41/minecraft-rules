package fr.pederobien.minecraftrules.rules;

import java.util.Arrays;
import java.util.List;

import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftgameplateform.dictionary.ECommonMessageCode;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecraftmanagers.WorldManager;
import fr.pederobien.minecraftrules.EGameRuleMessageCode;
import fr.pederobien.minecraftrules.impl.RunnableGameRule;

public class AnnounceAdvancementsGameRule extends RunnableGameRule<Boolean> {

	public AnnounceAdvancementsGameRule() {
		super("announceAdvancements", true, Boolean.class, EGameRuleMessageCode.ANNOUNCE_ADVANCEMENTS_GAME_RULE__EXPLANATION);
	}

	@Override
	public void setValue(Boolean value) {
		super.setValue(value);
		if (isRunning() && !value)
			stop();
		else if (!isRunning() && Plateform.getGameConfigurationContext().isRunning() && value)
			start();
	}

	@Override
	public void start() {
		super.start();
		setAnnounceAdvancements(getValue());
	}

	@Override
	public void stop() {
		super.stop();
		setAnnounceAdvancements(getDefaultValue());
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

	private void setAnnounceAdvancements(boolean value) {
		WorldManager.OVERWORLD.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, value);
		WorldManager.NETHER_WORLD.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, value);
		WorldManager.END_WORLD.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, value);
	}
}
