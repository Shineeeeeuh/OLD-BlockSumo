package eu.craftok.blocksumo.timers;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.manager.map.Map;
import eu.craftok.blocksumo.manager.player.BSPlayerManager;
import eu.craftok.blocksumo.manager.timers.Timer;
import eu.craftok.utils.PlayerUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class DragonTimer extends Timer{

	public DragonTimer() {
		super("dragon", false);
	}

	@Override
	public int getTime() {
		return 900;
	}

	@Override
	public void run() {
		Bukkit.broadcastMessage("§c§lCraftok §8» §cLes démons veulent votre mort ! Faîtes attention !");
		Map m = BlockSumo.getInstance().getGameManager().getPlayedMap();
		Bukkit.getWorld(m.getWorld()).spawnEntity(m.getBonus().add(0, 10, 0), EntityType.ENDER_DRAGON);
		Bukkit.getWorld(m.getWorld()).strikeLightningEffect(m.getBonus());
		BSPlayerManager.getAlivePlayers().forEach(b -> {
			Player p = b.getPlayer();
			p.setHealth(2F);
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 255));
			new PlayerUtils(p).sendSound(Sound.ENDERDRAGON_GROWL, 1F);
			new PlayerUtils(p).sendTitle(10, 20, 10, "§c§lL'apocalypse commence !", "§c§lFaîtes attention !");
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.MOB_APPEARANCE, false, 0, 0, 0, 0, 0, 0, 0, 1);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		});
	}

}
