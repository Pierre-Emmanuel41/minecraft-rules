package fr.pederobien.minecraft.rules.impl;

import java.time.LocalTime;
import java.util.function.Consumer;

import fr.pederobien.minecraft.commandtree.interfaces.ICodeSender;
import fr.pederobien.minecraft.dictionary.impl.MinecraftMessageEvent.MinecraftMessageEventBuilder;
import fr.pederobien.minecraft.game.impl.time.CountDown;
import fr.pederobien.minecraft.game.interfaces.time.ICountDown;
import fr.pederobien.minecraft.game.interfaces.time.IObsTimeLine;
import fr.pederobien.minecraft.managers.EColor;
import fr.pederobien.minecraft.managers.MessageManager.DisplayOption;
import fr.pederobien.minecraft.managers.WorldManager;
import fr.pederobien.minecraft.rules.ERuleCode;

public class PvpTimeLineObserver implements IObsTimeLine, ICodeSender {
	private ICountDown countDown;

	public PvpTimeLineObserver() {
		// Action during the count down
		Consumer<Integer> countDownAction = count -> {
			MinecraftMessageEventBuilder builder = eventBuilder(ERuleCode.GAME_RULE__PVP__COUNT_DOWN);
			builder.withDisplayOption(DisplayOption.TITLE).withColor(EColor.GOLD);
			send(builder.build(count));
		};

		// Action when the count down is over
		Consumer<LocalTime> onTimeAction = time -> {
			WorldManager.OVERWORLD.setPVP(true);
			WorldManager.NETHER_WORLD.setPVP(true);
			WorldManager.END_WORLD.setPVP(true);
		};
		countDown = new CountDown(5, countDownAction, onTimeAction);
	}

	@Override
	public ICountDown getCountDown() {
		return countDown;
	}

	@Override
	public LocalTime getNextTime() {
		return null;
	}
}
