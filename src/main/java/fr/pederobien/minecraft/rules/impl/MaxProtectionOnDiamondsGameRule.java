package fr.pederobien.minecraft.rules.impl;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.rules.ERuleCode;

public class MaxProtectionOnDiamondsGameRule extends EnchantGameRule {

	/**
	 * Creates a game rule to specify {@link Enchantment#PROTECTION_ENVIRONMENTAL} level restrictions while the game is in progress.
	 * 
	 * @param game The game associated to this rule.
	 */
	public MaxProtectionOnDiamondsGameRule(IGame game) {
		super(game, "maxProtectionOnDiamonds", Enchantment.PROTECTION_ENVIRONMENTAL, ERuleCode.GAME_RULE__MAX_PROTECTION_ON_DIAMONDS__EXPLANATION);
		setTargetItems(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS);
	}
}
