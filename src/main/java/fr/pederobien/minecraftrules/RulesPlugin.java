package fr.pederobien.minecraftrules;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import fr.pederobien.dictionary.interfaces.IDictionaryParser;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecraftrules.commands.RulesCommand;
import fr.pederobien.minecraftrules.impl.GameRule;

public class RulesPlugin extends JavaPlugin {
	private static Plugin plugin;

	/**
	 * @return The plugin associated to this rules plugin.
	 */
	public static Plugin get() {
		return plugin;
	}

	@Override
	public void onEnable() {
		Plateform.getPluginHelper().register(this);
		plugin = this;

		new RulesCommand(this);

		registerDictionaries();
	}

	@Override
	public void onDisable() {
		GameRule.RUNNABLE_RULES.forEach(gameRule -> gameRule.stop());
		GameRule.RULES.forEach(gameRule -> gameRule.reset());
	}

	private void registerDictionaries() {
		String[] dictionaries = new String[] { "GameRule.xml" };
		// Registering French dictionaries
		registerDictionary("French", dictionaries);

		// Registering English dictionaries
		registerDictionary("English", dictionaries);

		// Registering Turkish dictionaries
		registerDictionary("Turkish", dictionaries);
	}

	private void registerDictionary(String parent, String... dictionaryNames) {
		Path jarPath = Plateform.ROOT.getParent().resolve(getName().concat(".jar"));
		String dictionariesFolder = "resources/dictionaries/".concat(parent).concat("/");
		for (String name : dictionaryNames)
			registerDictionary(Plateform.getDefaultDictionaryParser(dictionariesFolder.concat(name)), jarPath);
	}

	private void registerDictionary(IDictionaryParser parser, Path jarPath) {
		try {
			Plateform.getNotificationCenter().getDictionaryContext().register(parser, jarPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
