package fr.pederobien.minecraftrules.commands;

import java.time.LocalTime;

import org.bukkit.plugin.java.JavaPlugin;

import fr.pederobien.minecraftgameplateform.commands.AbstractSimpleCommand;
import fr.pederobien.minecraftgameplateform.interfaces.element.IGame;
import fr.pederobien.minecraftgameplateform.interfaces.runtime.timeline.IObsTimeLine;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
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

		@Override
		public int getCountDown() {
			return 0;
		}

		@Override
		public int getCurrentCountDown() {
			return 0;
		}

		@Override
		public void onTime(LocalTime time) {
			GameRule.PVP.setValue(true);
			Plateform.getGameConfigurationContext().getGame().onPvpEnabled();
		}

		@Override
		public void onCountDownTime(LocalTime currentTime) {

		}

		@Override
		public LocalTime getNextNotifiedTime() {
			return LocalTime.of(0, 0, 0);
		}
	}
}
