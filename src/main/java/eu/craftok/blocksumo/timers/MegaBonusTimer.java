package eu.craftok.blocksumo.timers;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.manager.timers.Timer;
import eu.craftok.blocksumo.tasks.MegaBonusTask;
import eu.craftok.utils.PlayerUtils;

public class MegaBonusTimer extends Timer{

	public MegaBonusTimer() {
		super("megabonus", true);
	}

	@Override
	public int getTime() {
		return new Random().nextInt(120-90)+90;
	}

	@Override
	public void run() {
		Bukkit.broadcastMessage("§c§lCraftok §8» §cLe §c§lMEGABONUS §cest apparu au millieu !");
		new MegaBonusTask().runTaskTimer(BlockSumo.getInstance(), 4, 4);
		Bukkit.getOnlinePlayers().forEach(p -> new PlayerUtils(p).sendSound(Sound.ENDERDRAGON_WINGS, 1F));
		
	}
	
	
	

}
