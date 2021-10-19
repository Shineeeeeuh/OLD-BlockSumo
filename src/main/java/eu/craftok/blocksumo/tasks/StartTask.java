package eu.craftok.blocksumo.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.events.ProtectionEvents;
import eu.craftok.utils.PlayerUtils;

public class StartTask extends BukkitRunnable {
	private static int timer = 60;

	@Override
	public void run() {
		if(timer == 60) {
			sendTitle("§eDébut dans", "§61 minute");
		} else if(timer == 30) {
			sendTitle("§eDébut dans", "§630 secondes");
		} else if(timer == 10) {
			sendTitle("§eDébut dans", "§610 secondes");
		} else if(timer == 5) {
			sendTitle("§65", "");
			sendSound(Sound.ORB_PICKUP);
		} else if(timer == 4) {
			sendTitle("§64", "");
			sendSound(Sound.ORB_PICKUP);
		} else if(timer == 3) {
			sendTitle("§6\u2778", "");
			sendSound(Sound.ORB_PICKUP);
			ProtectionEvents.closed = true;
		} else if(timer == 2) {
			sendTitle("§e\u2777", "");
			sendSound(Sound.ORB_PICKUP);
		} else if(timer == 1) {
			sendTitle("§c\u2776", "");
			sendSound(Sound.ORB_PICKUP);
		} else if(timer == 0) {
			BlockSumo.getInstance().getGameManager().startGame();
			cancel();
		}
		Bukkit.getOnlinePlayers().forEach(p -> p.setLevel(timer));
		timer--;
	}
	
	private void sendTitle(String title, String subtitle) {
		Bukkit.getOnlinePlayers().forEach(p -> new PlayerUtils(p).sendTitle(10, 20, 10, title, subtitle));
	}
	
	private void sendSound(Sound s) {
		Bukkit.getOnlinePlayers().forEach(p -> new PlayerUtils(p).sendSound(s, 1F));
	}
	
	public static void setTimer(int timer) {
		StartTask.timer = timer;
	}
	
	public static int getTimer() {
		return timer;
	}
}
