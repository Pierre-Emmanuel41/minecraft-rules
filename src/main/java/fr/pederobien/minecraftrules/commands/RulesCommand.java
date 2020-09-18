package fr.pederobien.minecraftrules.commands;

import java.time.LocalTime;

import org.bukkit.plugin.java.JavaPlugin;

import fr.pederobien.minecraftdictionary.impl.MinecraftMessageEvent;
import fr.pederobien.minecraftgameplateform.commands.AbstractSimpleCommand;
import fr.pederobien.minecraftgameplateform.interfaces.element.IGame;
import fr.pederobien.minecraftgameplateform.interfaces.runtime.timeline.IObsTimeLine;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecraftmanagers.EColor;
import fr.pederobien.minecraftmanagers.MessageManager;
import fr.pederobien.minecraftmanagers.MessageManager.DisplayOption;
import fr.pederobien.minecraftmanagers.MessageManager.TitleMessage;
import fr.pederobien.minecraftmanagers.PlayerManager;
import fr.pederobien.minecraftrules.EGameRuleMessageCode;
import fr.pederobien.minecraftrules.impl.GameRule;

public class RulesCommand extends AbstractSimpleCommand {
	private IObsTimeLine pvpActivator;

	public RulesCommand(JavaPlugin plugin) {
		super(plugin, new RulesEdition());
		pvpActivator = new PvpActivator();
	}

	@Override
	public <U extends IGame> void onGameIsStarted(U IGame) {
		Plateform.getTimeLine().addObserver(Plateform.getGameConfigurationContext().getPvpTime(), pvpActivator);
		GameRule.RUNNABLE_RULES.forEach(rule -> rule.start());
		GameRule.PVP.setValue(false);
	}

	@Override
	public <U extends IGame> void onGameIsStopped(U IGame) {
		GameRule.RUNNABLE_RULES.forEach(rule -> rule.stop());
	}

	private class PvpActivator implements IObsTimeLine {
		private int currentCountDown;

		private PvpActivator() {
			currentCountDown = getCountDown();
		}

		@Override
		public int getCountDown() {
			return 5;
		}

		@Override
		public int getCurrentCountDown() {
			return currentCountDown;
		}

		@Override
		public void onTime(LocalTime time) {
			GameRule.PVP.setValue(true);
			Plateform.getGameConfigurationContext().onPvpEnabled();
			currentCountDown = getCountDown();
		}

		@Override
		public void onCountDownTime(LocalTime currentTime) {
			// Permission of message PVP__COUNT_DOWN is ALL, we don't need to specify a player for the event.
			PlayerManager.getPlayers().forEach(player -> {
				String message = Plateform.getNotificationCenter().getMessage(new MinecraftMessageEvent(EGameRuleMessageCode.PVP__COUNT_DOWN, currentCountDown));
				MessageManager.sendMessage(DisplayOption.TITLE, player, TitleMessage.of(message, EColor.GOLD));
			});
			currentCountDown--;
		}

		@Override
		public LocalTime getNextNotifiedTime() {
			return LocalTime.of(0, 0, 0);
		}
	}
}
