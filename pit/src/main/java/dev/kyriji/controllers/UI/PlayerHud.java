package dev.kyriji.controllers.UI;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import dev.kyriji.controllers.PlayerManager;
import dev.kyriji.objects.PitPlayer;
import dev.kyriji.utils.ColorUtils;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PlayerHud extends CustomUIHud {
	private final PlayerRef playerRef;
	private ScheduledFuture<?> updateTask;

	public PlayerHud(@Nonnull PlayerRef playerRef) {
		super(playerRef);

		this.playerRef = playerRef;
	}

	@Override
	protected void build(@Nonnull UICommandBuilder uiCommandBuilder) {
		this.updateTask = HytaleServer.SCHEDULED_EXECUTOR.scheduleWithFixedDelay(this::update, 0, 1, TimeUnit.SECONDS);
	}

	private void update() {
		PitPlayer pitPlayer = PlayerManager.getPitPlayer(playerRef.getUuid());

		UICommandBuilder builder = new UICommandBuilder();
		builder.append("Hud/InfoCard.ui");
		Color randomColor = new Color((int)(Math.random() * 0x1000000));
		builder.set("#Title.TextSpans", Message.raw("The Pit").color(randomColor));

		builder.set("#Row1.TextSpans", Message.raw("Kills: ").color(Color.WHITE).insert(Message.raw(String.valueOf(pitPlayer.getKills())).color(Color.GREEN)));
		builder.set("#Row2.TextSpans", Message.raw("Deaths: ").color(Color.WHITE).insert(Message.raw(String.valueOf(pitPlayer.getDeaths())).color(Color.RED)));
		builder.set("#Row3.TextSpans", Message.raw("Streak: ").color(Color.WHITE).insert(Message.raw(String.valueOf(pitPlayer.getCurrentStreak())).color(Color.YELLOW)));
		builder.set("#Row4.TextSpans", Message.raw("Gold: ").color(Color.WHITE).insert(Message.raw(String.valueOf(pitPlayer.getGold())).color(ColorUtils.GOLD)));
		builder.set("#Row5.TextSpans", Message.raw(""));
		builder.set("#Row6.TextSpans", Message.raw("Thanks for playing!").color(Color.GRAY));

		update(true, builder);
	}

	public void destroy() {
		if (this.updateTask != null && !this.updateTask.isCancelled()) this.updateTask.cancel(true);
	}
}
