package eu.craftok.blocksumo.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EndTask extends BukkitRunnable {
	private int timer = 1;

	@Override
	public void run() {
		if(timer == 1) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.kickPlayer("");
			}
			timer = 0;
		}
		if(timer == 0) {
			Bukkit.getServer().shutdown();
			cancel();
		}
	}
	
	
}
