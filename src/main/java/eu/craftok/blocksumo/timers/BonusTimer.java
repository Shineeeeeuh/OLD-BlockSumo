package eu.craftok.blocksumo.timers;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.manager.bonus.BonusManager;
import eu.craftok.blocksumo.manager.map.Map;
import eu.craftok.blocksumo.manager.timers.Timer;

public class BonusTimer extends Timer{

	public BonusTimer() {
		super("bonus", true);
	}

	@Override
	public int getTime() {
		return 30;
	}

	@Override
	public void run() {
		Map m = BlockSumo.getInstance().getGameManager().getPlayedMap();
		Location l = m.getBonus();
		l.getWorld().dropItem(l, BonusManager.getNormalRandomItem());
		Bukkit.broadcastMessage("§c§lCRAFTOK §8» §7Un bonus vient d'apparaître au millieu de la map !");
	}

}
