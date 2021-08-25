package eu.craftok.blocksumo.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.utils.PlayerUtils;

public class BonusTask extends BukkitRunnable{

	private BlockSumo instance;
	
	public BonusTask(BlockSumo instance) {
		this.instance = instance;
	}
	
	
	@Override
	public void run() {
        Location l = this.instance.getGameManager().getPlayedMap().getBonusLocation();
        l.getWorld().dropItem(l, this.instance.getBonusManager().getRandomItem());
        Bukkit.broadcastMessage("§c§lCRAFTOK §8» §7Un bonus vient d'apparaître au millieu de la map !");
        for (Player p : Bukkit.getOnlinePlayers()) {
            new PlayerUtils(p).sendSound(Sound.LEVEL_UP, 1.0f);
        }
	}

}
