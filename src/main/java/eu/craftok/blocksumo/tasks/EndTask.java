package eu.craftok.blocksumo.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.game.Game;

public class EndTask extends BukkitRunnable{
	
	private int gameid;
	private BlockSumo instance;
	
	public EndTask(BlockSumo instance, Game g) {
		this.instance = instance;
		this.gameid = g.getID();
	}
	
	@Override
	public void run() {
		Game g = instance.getGameManager().getGameByID(gameid);
		if(g.getPlayers() != null && g.getPlayers().size() > 0) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(instance.isVanished(p)) continue;
				Game pg = instance.getGameManager().getGameByPlayer(p);
				if(pg != null && pg.getID() == g.getID()) {
					p.kickPlayer(" ");
				}
			}
		}
		for(int i : g.getTasks()) {
			Bukkit.getServer().getScheduler().cancelTask(i);
		}
		instance.getGameManager().removeGame(gameid);
		instance.getGameManager().createGame();
	}
	
	

}
