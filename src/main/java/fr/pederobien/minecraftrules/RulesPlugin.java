package fr.pederobien.minecraftrules;

import org.bukkit.plugin.java.JavaPlugin;

import fr.pederobien.minecraftgameplateform.utils.Plateform;

public class RulesPlugin extends JavaPlugin {
	public static String NAME = "minecraft-rules";

	@Override
	public void onEnable() {
		Plateform.getPluginHelper().register(this);
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
}
