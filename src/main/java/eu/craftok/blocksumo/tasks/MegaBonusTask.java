package eu.craftok.blocksumo.tasks;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.manager.bonus.BonusManager;
import eu.craftok.blocksumo.manager.map.Map;
import eu.craftok.utils.PlayerUtils;
import eu.craftok.utils.particles.ParticleUtils;
import eu.craftok.utils.particles.Particles;

public class MegaBonusTask extends BukkitRunnable{
	
	private String lastplayer;
	private int time;
	private int eventtime;

	@Override
	public void run() {
		if((eventtime+1)/5 == 15) {
			Bukkit.broadcastMessage("§c§lCRAFTOK §8» §cLe §c§lMEGABONUS §ca disparu !");
			cancel();
		}
		Map m = BlockSumo.getInstance().getGameManager().getPlayedMap();
		ParticleUtils.playCircle(Particles.FLAME, m.getBonus().add(0, 1, 0), 1, 1);
		Collection<Entity> entities = Bukkit.getWorld(m.getWorld()).getNearbyEntities(m.getBonus(), 1, 2, 1);
		if(entities.size() == 0) {
			if(lastplayer != null) {
				Bukkit.broadcastMessage("§c§lCRAFTOK §8» §c§l"+lastplayer+" §ca perdu le contrôle du MegaBonus !");
			}
			lastplayer = null;
			time = 0;
			return;
		}else {
			Player p = (Player) entities.stream().filter(e -> e instanceof Player && ((Player) e).getGameMode() != GameMode.SPECTATOR && e.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.GOLD_BLOCK).findFirst().get();
			if(p == null) {
				if(lastplayer != null) {
					Bukkit.broadcastMessage("§c§lCRAFTOK §8» §c§l"+lastplayer+" §ca perdu le contrôle du MegaBonus !");
				}
				lastplayer = null;
				time = 0;
			}
			if(lastplayer == p.getName()) {
				if((time+1)/5 == 5) {
					p.getInventory().addItem(BonusManager.getMegaRandomItems());
					Bukkit.broadcastMessage("§c§lCRAFTOK §8» §6§l"+lastplayer+" §ea gagné le §c§lMEGABONUS §e!");
					sendProgressTitle(p);
					new PlayerUtils(p).sendSound(Sound.LEVEL_UP, 1F);
					cancel();
				}else {
					time++;
					sendProgressTitle(p);
					return;
				}
			}else {
				if(lastplayer != null) {
					Bukkit.broadcastMessage("§c§lCRAFTOK §8» §c§l"+lastplayer+" §ca perdu le contrôle du MegaBonus !");
				}
				Bukkit.broadcastMessage("§c§lCRAFTOK §8» §c§l"+p.getName()+" §ca pris le contrôle du MegaBonus !");
				lastplayer = p.getName();
				time = 0;
				sendProgressTitle(p);
				return;
			}
		}
	}
	
	private void sendProgressTitle(Player p) {
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < 10; i++) {
			if((time/5)*2 >= i) {
				sb.append("§a\u2588");
				continue;
			}else {
				sb.append("§f\u2588");
				continue;
			}
		}
		new PlayerUtils(p).sendTitle(0, 10, 0, "§f["+sb.toString()+"§f]", null);
	}
	
}
