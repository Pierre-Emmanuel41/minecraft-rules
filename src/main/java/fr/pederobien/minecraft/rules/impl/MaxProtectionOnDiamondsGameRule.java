package fr.pederobien.minecraft.rules.impl;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import fr.pederobien.minecraft.rules.EGameRuleMessageCode;

public class MaxProtectionOnDiamondsGameRule extends EnchantGameRule {

	public MaxProtectionOnDiamondsGameRule() {
		super("maxProtectionOnDiamonds", Enchantment.PROTECTION_ENVIRONMENTAL, EGameRuleMessageCode.MAX_PROTECTION_ON_DIAMONDS__EXPLANATION);
		setTargetItems(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS);
	}

}
