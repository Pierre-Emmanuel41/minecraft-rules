package fr.pederobien.minecraftrules.rules;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftgameplateform.dictionary.ECommonMessageCode;
import fr.pederobien.minecraftmanagers.WorldManager;
import fr.pederobien.minecraftrules.EGameRuleMessageCode;
import fr.pederobien.minecraftrules.impl.GameRule;

public class PvpGameRule extends GameRule<Boolean> {

	public PvpGameRule() {
		super("pvp", false, Boolean.class, EGameRuleMessageCode.PVP_GAME_RULE__EXPLANATION);
	}

	@Override
	public void setValue(Boolean value) {
		super.setValue(value);
		WorldManager.OVERWORLD.setPVP(value);
		WorldManager.NETHER_WORLD.setPVP(value);
		WorldManager.END_WORLD.setPVP(value);
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
			sendSynchro(sender, EGameRuleMessageCode.COMMON_VALUE_DEFINED, getName(), value);
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
