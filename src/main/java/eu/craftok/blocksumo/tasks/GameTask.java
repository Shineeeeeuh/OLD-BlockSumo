package eu.craftok.blocksumo.tasks;

import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.manager.player.BSPlayerManager;
import eu.craftok.blocksumo.manager.timers.TimerManager;

public class GameTask extends BukkitRunnable{

	@Override
	public void run() {
		BSPlayerManager.updateActionBar();
		TimerManager.updateTimers();
		if(!BlockSumo.getInstance().getGameManager().getBlocks().isEmpty()) {
			Lists.newArrayList(BlockSumo.getInstance().getGameManager().getBlocks().keySet()).forEach(l -> {
				int i = BlockSumo.getInstance().getGameManager().getBlocks().get(l);
				if(i+1 == 13) {
					l.getBlock().setType(Material.AIR);
					BlockSumo.getInstance().getGameManager().getBlocks().remove(l);
					return;
				}
				BlockSumo.getInstance().getGameManager().getBlocks().put(l, i+1);
			});
		}
	}
	

}
