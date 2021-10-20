package eu.craftok.blocksumo.timers;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.WorldBorder;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.manager.player.BSPlayerManager;
import eu.craftok.blocksumo.manager.timers.Timer;
import eu.craftok.utils.PlayerUtils;

public class DeathMatchTimer extends Timer{

	private int time;
	
	public DeathMatchTimer(int time) {
		super("deathmatch", false);
		this.time = time;
	}

	@Override
	public int getTime() {
		return time;
	}

	@Override
	public void run() {
		BSPlayerManager.getAlivePlayers().forEach(b -> {
			b.setLife(1);
			PlayerUtils utils = new PlayerUtils(b.getPlayer());
			utils.sendTitle(5, 20, 5, "§f< §c§lDEATHMATCH §f>", "§c§lQue le meilleur gagne !");
			utils.sendSound(Sound.GHAST_SCREAM, 1F);
		});
		BSPlayerManager.updateSB();
		WorldBorder wb = Bukkit.getWorld(BlockSumo.getInstance().getGameManager().getPlayedMap().getWorld()).getWorldBorder();
		wb.setDamageAmount(5);
		wb.setDamageBuffer(0);
		wb.setSize(10,60);
		Bukkit.broadcastMessage("§c§lCRAFTOK §8» §cLe §c§lDEATCHMATCH §ccommence !");
	}

}
