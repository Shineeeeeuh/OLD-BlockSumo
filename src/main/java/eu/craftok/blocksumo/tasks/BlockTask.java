package eu.craftok.blocksumo.tasks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

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
		for(Location l : instance.getGameManager().getBlocksPlaced().keySet()) {
			int i = instance.getGameManager().getBlocksPlaced().get(l);
			if(i+1 == 20) {
				l.getBlock().setType(Material.AIR);
				instance.getGameManager().getBlocksPlaced().remove(l);
				continue;
			}
			instance.getGameManager().getBlocksPlaced().put(l, i+1);
		}
	}
	
}
