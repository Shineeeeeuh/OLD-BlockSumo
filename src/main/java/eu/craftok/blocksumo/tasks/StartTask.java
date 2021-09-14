package eu.craftok.blocksumo.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.enums.GameState;
import eu.craftok.blocksumo.game.Game;
import eu.craftok.blocksumo.player.BSPlayer;
import eu.craftok.utils.PlayerUtils;

public class StartTask extends BukkitRunnable {

	private int timer;
	private BlockSumo instance;
	private int taskid,gameid;

	public StartTask(BlockSumo blocksumo, Game g) {
		instance = blocksumo;
		timer = 60;
		taskid = 0;
		gameid = g.getID();
	}
	
	@Override
	public void run() {
		Game g = instance.getGameManager().getGameByID(gameid);
		setLevel(timer, g);
		if(timer == 60) {
			sendTitle("§eDébut dans", "§61 minute", g);
		} else if(timer == 30) {
			sendTitle("§eDébut dans", "§630 secondes", g);
		} else if(timer == 10) {
			sendTitle("§eDébut dans", "§610 secondes", g);
		} else if(timer == 5) {
			sendTitle("§65", "", g);
			sendSound(Sound.ORB_PICKUP, g);
		} else if(timer == 4) {
			sendTitle("§e4", "", g);
			sendSound(Sound.ORB_PICKUP, g);
		} else if(timer == 3) {
			sendTitle("§a➌", "", g);
			sendSound(Sound.ORB_PICKUP, g);
		} else if(timer == 2) {
			sendTitle("§d➋", "", g);
			sendSound(Sound.ORB_PICKUP, g);
		} else if(timer == 1) {
			sendTitle("§c➊", "", g);
			sendSound(Sound.ORB_PICKUP, g);
		} else if(timer == 0) {
			Bukkit.getServer().getScheduler().cancelTask(taskid);
			g.setState(GameState.INGAME);
			sendSound(Sound.LEVEL_UP, g);
			g.start();
			g.initWorldBorder();
			instance.getGameManager().createGame();
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
	
	public void sendTitle(String title, String sub, Game g) {
		for(BSPlayer p : g.getPlayers()) {
			new PlayerUtils(p.getPlayer()).sendTitle(10, 20, 10, title, sub);
		}
	}
	
	public void sendSound(Sound sound, Game g) {
		for(BSPlayer p : g.getPlayers()) {
            new PlayerUtils(p.getPlayer()).sendSound(sound, 1.0f);
        }
    }
	
	
	public void setTimer(int i) {
		timer = i;
	}
	
	public void setLevel(int level, Game g) {
		for(BSPlayer p : g.getPlayers()) {
			p.getPlayer().setLevel(level);
		}
	}
	
}
