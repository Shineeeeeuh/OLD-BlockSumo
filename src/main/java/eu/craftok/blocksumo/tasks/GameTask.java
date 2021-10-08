package eu.craftok.blocksumo.tasks;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.enums.GameState;
import eu.craftok.blocksumo.game.Game;
import eu.craftok.blocksumo.player.BSPlayer;

public class GameTask extends BukkitRunnable{
	
	private BlockSumo instance;
	private int gameid;

	public GameTask(BlockSumo instance, Game g) {
		this.instance = instance;
		this.gameid = g.getID();
	}
	
	@Override
	public void run() {
		Game g = instance.getGameManager().getGameByID(gameid);
		if(g == null) {
			cancel();
			return;
		}
		if(g.getState() == GameState.FINISH) {
			cancel();
			return;
		}
		for (BSPlayer bs : g.getPlayers()) {
			if(bs.isVanished()) return;
            bs.updateActionBar();
        }
		int d = g.getTimeBeforeDeathmatch();
		if(d != -1) {
			if(d-1 == 0) {
				g.deathMatch();
				g.setTimeBeforeDeathMatch(-1);
			}else {
				if(g.getState() == GameState.INGAME) {
					g.decreaseTimeDeathMatch();
				}
			}
		}
		if(g.getBlocksPlaced().size() == 0) {
			return;
		}
		List<Location> locs = Lists.newArrayList(g.getBlocksPlaced().keySet());
		for(Location l : locs) {
			int i = g.getBlocksPlaced().get(l);
			if(i+1 == 13) {
				l.getBlock().setType(Material.AIR);
				g.getBlocksPlaced().remove(l);
				continue;
			}
			g.getBlocksPlaced().put(l, i+1);
		}
	}
	
}
