package fr.pederobien.minecraftrules.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftgameplateform.dictionary.ECommonMessageCode;
import fr.pederobien.minecraftrules.EGameRuleMessageCode;
import fr.pederobien.minecraftrules.impl.EventGameRule;

public class EnchantGameRule extends EventGameRule<Integer> {
	private Enchantment enchantment;
	private List<Material> items;

	public EnchantGameRule(String name, Enchantment enchantment, IMinecraftMessageCode explanation) {
		super(name, enchantment.getMaxLevel(), Integer.class, explanation);
		this.enchantment = enchantment;
		items = new ArrayList<Material>();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEnchantItemEvent(EnchantItemEvent event) {
		if (!isRunning() || !items.contains(event.getItem().getType()))
			return;
		for (Map.Entry<Enchantment, Integer> entry : event.getEnchantsToAdd().entrySet())
			if (entry.getKey().equals(enchantment) && entry.getValue() > getValue()) {
				event.setCancelled(true);
				getEdition().sendNotSynchro(event.getEnchanter(), EGameRuleMessageCode.ENCHANT_GAME_RULE__CANNOT_ENCHANT, enchantment.getKey().getKey(), getValue(),
						entry.getValue());
			}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (!isRunning() || !(event.getInventory() instanceof AnvilInventory))
			return;

		AnvilInventory anvilInventory = (AnvilInventory) event.getInventory();
		ItemStack result = anvilInventory.getItem(2);

		if (result == null || !items.contains(result.getType()))
			return;

		for (Map.Entry<Enchantment, Integer> entry : result.getEnchantments().entrySet()) {
			if (entry.getKey().equals(enchantment) && entry.getValue() > getValue()) {
				event.setCancelled(true);
				getEdition().sendNotSynchro(event.getWhoClicked(), EGameRuleMessageCode.ENCHANT_GAME_RULE__CANNOT_ENCHANT, enchantment.getKey().getKey(), getValue(),
						entry.getValue());
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			setValue(Integer.parseInt(args[0]));
			sendSynchro(sender, EGameRuleMessageCode.COMMON_VALUE_DEFINED_IN_GAME, getName(), getValue());
		} catch (IndexOutOfBoundsException e) {
			sendSynchro(sender, EGameRuleMessageCode.COMMON_VALUE_IS_MISSING, getName());
			return false;
		} catch (NumberFormatException e) {
			sendSynchro(sender, ECommonMessageCode.COMMON_BAD_INTEGER_FORMAT);
			return false;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (args.length) {
		case 1:
			return Arrays.asList(getEdition().getMessage(sender, EGameRuleMessageCode.ENCHANT_GAME_RULE__ON_TAB_COMPLETE));
		default:
			return Arrays.asList();
		}
	}

	public EnchantGameRule setTargetItems(Material... items) {
		this.items = Arrays.asList(items);
		return this;
	}
}
