package fr.pederobien.minecraftrules.rules;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import fr.pederobien.minecraftgameplateform.dictionary.ECommonMessageCode;
import fr.pederobien.minecraftrules.EGameRuleMessageCode;
import fr.pederobien.minecraftrules.impl.EventGameRule;

public class AppleDropRateGameRule extends EventGameRule<Double> {
	private Random random;

	public AppleDropRateGameRule() {
		super("appleDropRate", -1.0, Double.class, EGameRuleMessageCode.APPLE_DROP_RATE__EXPLANATION);
		random = new Random();
	}

	@Override
	public void reset() {
		super.setValue(getDefaultValue());
	}

	@Override
	public void setValue(Double value) {
		if (value < 0 || value > 1)
			throw new UnsupportedOperationException("The value is out of range [0,1]");
		super.setValue(value);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onLeavesDecay(LeavesDecayEvent event) {
		tryDropApple(event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onLeavesDecay(BlockBreakEvent event) {
		tryDropApple(event);
	}

	@Override
	protected boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			setValue(Double.parseDouble(args[0]));
			sendSynchro(sender, EGameRuleMessageCode.COMMON_VALUE_DEFINED_IN_GAME, getName(), getValue());
		} catch (IndexOutOfBoundsException e) {
			sendNotSynchro(sender, EGameRuleMessageCode.COMMON_VALUE_IS_MISSING);
			return false;
		} catch (NumberFormatException e) {
			sendNotSynchro(sender, ECommonMessageCode.COMMON_BAD_DOUBLE_FORMAT);
			return false;
		} catch (UnsupportedOperationException e) {
			sendNotSynchro(sender, EGameRuleMessageCode.APPLE_DROP_RATE__VALUE_OUT_OF_BOUND);
			return false;
		}
		return true;
	}

	@Override
	protected String getCurrent(CommandSender sender) {
		return getValue() == -1 ? getMessage(sender, EGameRuleMessageCode.APPLE_DROP_RATE__DROP_RATE_DEFAULT) : getValue().toString();
	}

	@Override
	protected String getDefault(CommandSender sender) {
		return getMessage(sender, EGameRuleMessageCode.APPLE_DROP_RATE__DROP_RATE_DEFAULT);
	}

	@Override
	protected List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return asList(getMessage(sender, EGameRuleMessageCode.APPLE_DROP_RATE__DROP_RATE_ON_TAB_COMPLETE));
	}

	private void tryDropApple(BlockEvent event) {
		if (!isRunning() || !event.getBlock().getType().equals(Material.OAK_LEAVES) || getValue() == -1.0)
			return;

		double probability = random.nextDouble();
		if (probability <= getValue()) {
			event.getBlock().setType(Material.AIR);
			event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
		}
	}
}
