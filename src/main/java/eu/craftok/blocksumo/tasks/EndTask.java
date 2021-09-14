package eu.craftok.blocksumo.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.game.Game;
import eu.craftok.blocksumo.player.BSPlayer;

public class EndTask extends BukkitRunnable{
	
	private int gameid, counter;
	private BlockSumo instance;
	
	public EndTask(BlockSumo instance, Game g) {
		this.instance = instance;
		this.counter = 1;
		this.gameid = g.getID();
	}

	@Override
	public void run() {
		if(counter == 1) {
			Game g = instance.getGameManager().getGameByID(gameid);
			for(BSPlayer bsp : g.getPlayers()) {
				bsp.getPlayer().kickPlayer(" ");
			}
			counter--;
		}
		if(counter == 0) {
			instance.getGameManager().removeGame(gameid);
			cancel();
		}
	}
	
	

}
