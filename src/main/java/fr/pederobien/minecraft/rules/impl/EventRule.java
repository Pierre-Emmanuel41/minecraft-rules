package fr.pederobien.minecraft.rules.impl;

import org.bukkit.event.Listener;

import fr.pederobien.minecraft.commandtree.interfaces.ICodeSender;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.rules.RulesPlugin;

public class EventRule<T> extends Rule<T> implements Listener, ICodeSender {

	/**
	 * Creates a game rule based on the given parameters.
	 * 
	 * @param game         The game to which this rule is associated.
	 * @param name         The game rule name.
	 * @param defaultValue The default game rule value.
	 * @param explanation  The code used to explain what does this rule do.
	 * @param parser       The parser used to save/load game rule values.
	 */
	protected EventRule(IGame game, String name, T defaultValue, IMinecraftCode explanation, Parser<T> parser) {
		super(game, name, defaultValue, explanation, parser);
		RulesPlugin.instance().getServer().getPluginManager().registerEvents(this, RulesPlugin.instance());
	}
}
