package eu.craftok.blocksumo.timers;

import java.awt.Color;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.manager.bonus.BonusManager;
import eu.craftok.blocksumo.manager.map.Map;
import eu.craftok.blocksumo.manager.timers.Timer;
import eu.craftok.utils.particles.ParticleUtils;
import eu.craftok.utils.particles.Particles;

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
		ParticleUtils.drawParticleLine(l.add(0,5,0), l.subtract(0, 3, 0), Particles.VILLAGER_HAPPY, 20, Color.ORANGE);
		Bukkit.broadcastMessage("§c§lCRAFTOK §8» §7Un bonus vient d'appara�tre au millieu de la map !");
	}

}
