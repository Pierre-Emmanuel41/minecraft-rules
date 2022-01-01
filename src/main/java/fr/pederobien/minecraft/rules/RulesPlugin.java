package fr.pederobien.minecraft.rules;

import java.io.FileNotFoundException;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import fr.pederobien.dictionary.exceptions.MessageRegisteredException;
import fr.pederobien.dictionary.impl.JarXmlDictionaryParser;
import fr.pederobien.minecraft.dictionary.impl.MinecraftDictionaryContext;
import fr.pederobien.utils.AsyncConsole;

public class RulesPlugin extends JavaPlugin {
	private static final String DICTIONARY_FOLDER = "resources/dictionaries/";

	private static Plugin instance;

	/**
	 * @return The instance of this plugin.
	 */
	public static Plugin instance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;

		registerDictionaries();
	}

	private void registerDictionaries() {
		try {
			JarXmlDictionaryParser dictionaryParser = new JarXmlDictionaryParser(getFile().toPath());

			MinecraftDictionaryContext context = MinecraftDictionaryContext.instance();
			String[] dictionaries = new String[] { "English.xml", "French.xml" };
			for (String dictionary : dictionaries)
				try {
					context.register(dictionaryParser.parse(DICTIONARY_FOLDER.concat(dictionary)));
				} catch (MessageRegisteredException e) {
					AsyncConsole.print(e);
					for (StackTraceElement element : e.getStackTrace())
						AsyncConsole.print(element);
				}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
