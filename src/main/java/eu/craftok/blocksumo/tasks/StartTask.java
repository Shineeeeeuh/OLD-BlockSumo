package eu.craftok.blocksumo.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.enums.GameState;
import eu.craftok.blocksumo.player.BSPlayer;
import eu.craftok.utils.PlayerUtils;

public class StartTask extends BukkitRunnable {

	private static int timer;
	private BlockSumo instance;
	private int taskid;

	public StartTask(BlockSumo blocksumo) {
		instance = blocksumo;
		timer = 60;
		taskid = 0;
	}
	
	@Override
	public void run() {
		setLevel(timer);
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
			sendTitle("§e4", "");
			sendSound(Sound.ORB_PICKUP);
		} else if(timer == 3) {
			sendTitle("§a➌", "");
			sendSound(Sound.ORB_PICKUP);
		} else if(timer == 2) {
			sendTitle("§d➋", "");
			sendSound(Sound.ORB_PICKUP);
		} else if(timer == 1) {
			sendTitle("§c➊", "");
			sendSound(Sound.ORB_PICKUP);
		} else if(timer == 0) {
			Bukkit.getServer().getScheduler().cancelTask(taskid);
			instance.getGameManager().setState(GameState.INGAME);
			sendSound(Sound.LEVEL_UP);
			instance.getGameManager().startGame();
			instance.getGameManager().getPlayedMap().initWorldBorder();
			return;
		}
		timer--;
	}
	
	public void setTaskID(int taskid) {
		this.taskid = taskid;
	}
	
	public int getTimer() {
		return timer;
	}
	
	public void sendTitle(String title, String sub) {
		for(BSPlayer p : instance.getPlayerManager().getPlayers()) {
			new PlayerUtils(p.getPlayer()).sendTitle(10, 20, 10, title, sub);
		}
	}
	
	public void sendSound(Sound sound) {
        for (BSPlayer p : this.instance.getPlayerManager().getPlayers()) {
            new PlayerUtils(p.getPlayer()).sendSound(sound, 1.0f);
        }
    }
	
	
	public static void setTimer(int i) {
		timer = i;
	}
	
	public void setLevel(int level) {
		for(BSPlayer p : instance.getPlayerManager().getPlayers()) {
			p.getPlayer().setLevel(level);
		}
	}
	
}
