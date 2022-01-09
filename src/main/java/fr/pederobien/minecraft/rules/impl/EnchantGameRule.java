package fr.pederobien.minecraft.rules.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.rules.ERuleCode;
import fr.pederobien.utils.IPausable.PausableState;

public class EnchantGameRule extends EventGameRule<Integer> {
	private static final Parser<Integer> PARSER = new Parser<Integer>(value -> value.toString(), value -> Integer.parseInt(value));
	private Enchantment enchantment;
	private List<Material> items;

	/**
	 * Creates a game rule in order to specify enchant level restrictions while the game is in progress.
	 * 
	 * @param game         The game to which this rule is associated.
	 * @param name         The game rule name.
	 * @param defaultValue The default game rule value.
	 * @param explanation  The code used to explain what does this rule do.
	 */
	public EnchantGameRule(IGame game, String name, Enchantment enchantment, IMinecraftCode explanation) {
		super(game, name, enchantment.getMaxLevel(), explanation, PARSER);
		this.enchantment = enchantment;
		items = new ArrayList<Material>();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private void onEnchantItemEvent(EnchantItemEvent event) {
		if (getGame().getState() == PausableState.NOT_STARTED || !items.contains(event.getItem().getType()))
			return;

		Iterator<Map.Entry<Enchantment, Integer>> iterator = event.getEnchantsToAdd().entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Enchantment, Integer> entry = iterator.next();
			if (entry.getKey().equals(enchantment) && entry.getValue() > getValue()) {
				entry.setValue(entry.getValue());
				send(eventBuilder(event.getEnchanter(), ERuleCode.GAME_RULE__ENCHANT__ENCHANT_REMOVED, enchantment.getKey().getKey(), getValue()));
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private void onInventoryClickEvent(InventoryClickEvent event) {
		if (getGame().getState() == PausableState.NOT_STARTED || !(event.getInventory() instanceof AnvilInventory))
			return;

		AnvilInventory anvilInventory = (AnvilInventory) event.getInventory();
		ItemStack result = anvilInventory.getItem(2);

		if (result == null || !items.contains(result.getType()))
			return;

		Iterator<Map.Entry<Enchantment, Integer>> iterator = result.getEnchantments().entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Enchantment, Integer> entry = iterator.next();
			if (entry.getKey().equals(enchantment) && entry.getValue() > getValue()) {
				entry.setValue(entry.getValue());
				send(eventBuilder(event.getWhoClicked(), ERuleCode.GAME_RULE__ENCHANT__ENCHANT_REMOVED, enchantment.getKey().getKey(), getValue()));
			}
		}
	}

	/**
	 * Set the items on which the restriction are done.
	 * 
	 * @param items The list of item on which the enchant restriction occurs.
	 * 
	 * @return This game rule.
	 */
	public EnchantGameRule setTargetItems(Material... items) {
		this.items = Arrays.asList(items);
		return this;
	}

	/**
	 * @return The enchantment concerned by this game rule.
	 */
	public Enchantment getEnchantment() {
		return enchantment;
	}
}
