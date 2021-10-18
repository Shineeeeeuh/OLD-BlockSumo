package eu.craftok.blocksumo.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class EndTask extends BukkitRunnable{

	@Override
	public void run() {
		Bukkit.getOnlinePlayers().forEach(p -> p.kickPlayer(""));
		Bukkit.shutdown();
	}

}
