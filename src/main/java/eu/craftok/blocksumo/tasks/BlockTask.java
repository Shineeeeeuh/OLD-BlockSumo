package eu.craftok.blocksumo.tasks;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.player.BSPlayer;

public class BlockTask extends BukkitRunnable{
	
	private BlockSumo instance;

	public BlockTask(BlockSumo instance) {
		this.instance = instance;
	}
	
	@Override
	public void run() {
		for (BSPlayer bs : instance.getPlayerManager().getPlayers()) {
            bs.updateActionBar();
        }
		if(instance.getGameManager().getBlocksPlaced().size() == 0) {
			return;
		}
		List<Location> locs = Lists.newArrayList(instance.getGameManager().getBlocksPlaced().keySet());
		for(Location l : locs) {
			int i = instance.getGameManager().getBlocksPlaced().get(l);
			if(i+1 == 13) {
				l.getBlock().setType(Material.AIR);
				instance.getGameManager().getBlocksPlaced().remove(l);
				continue;
			}
			instance.getGameManager().getBlocksPlaced().put(l, i+1);
		}
	}
	
}
