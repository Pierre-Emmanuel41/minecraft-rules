package fr.pederobien.minecraftrules.commands;

import org.bukkit.plugin.java.JavaPlugin;

import fr.pederobien.minecraftgameplateform.commands.AbstractSimpleCommand;

public class RulesCommand extends AbstractSimpleCommand {

	public RulesCommand(JavaPlugin plugin) {
		super(plugin, new RulesEdition());
	}
}
