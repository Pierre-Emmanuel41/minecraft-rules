package fr.pederobien.minecraftrules;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import org.bukkit.plugin.java.JavaPlugin;

import fr.pederobien.dictionary.interfaces.IDictionaryParser;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecraftrules.impl.GameRule;

public class RulesPlugin extends JavaPlugin {
	public static String NAME = "minecraft-rules";

	@Override
	public void onEnable() {
		Plateform.getPluginHelper().register(this);

		registerDictionaries();
	}

	@Override
	public void onDisable() {
		GameRule.RUNNABLE_RULES.forEach(gameRule -> gameRule.stop());
		GameRule.RULES.forEach(gameRule -> gameRule.reset());
	}

	private void registerDictionaries() {
		// Registering French dictionaries
		registerDictionary("French", "GameRule.xml");

		// Registering English dictionaries
		registerDictionary("English", "GameRule.xml");
	}

	private void registerDictionary(String parent, String... dictionaryNames) {
		Path jarPath = Plateform.ROOT.getParent().resolve(NAME.concat(".jar"));
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
