package eu.craftok.blocksumo.events.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener; 
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.server.ServerListPingEvent;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.game.Game;
import eu.craftok.blocksumo.player.BSPlayer;
import eu.craftok.core.common.CoreCommon;
import eu.craftok.core.common.user.User;

public class GeneralPlayerEvents implements Listener {

	private BlockSumo instance;
	
	public GeneralPlayerEvents(BlockSumo bs) {
		this.instance = bs;
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Game g = instance.getGameManager().getGameByPlayer(e.getPlayer());
		BSPlayer bsp = g.getPlayer(e.getPlayer().getName());
		e.setCancelled(true);
		if(bsp.isSpectator() || bsp.isVanished()) {
			g.sendMessageToSpec("§7[SPECTATEUR] §f"+e.getPlayer().getName()+" : "+e.getMessage());
			return;
		}
		User u = CoreCommon.getCommon().getUserManager().getUserByUniqueId(e.getPlayer().getUniqueId());
		g.broadcastMessage(u.getDisplayName()+" §f: "+e.getMessage());
	}
	
	@EventHandler
	public void onServerListPing(ServerListPingEvent e) {
		if(instance.getGameManager().getGameNumbers() == 5) {
			e.setMotd("INGAME");
		}else {
			e.setMotd("ONLINE");
		}
	}
	
	@EventHandler
	public void onTP(PlayerTeleportEvent e) {
		if(e.getCause() == TeleportCause.PLUGIN || e.getCause() == TeleportCause.COMMAND) {
			Location l = e.getTo();
			if(l.getWorld().getName() == e.getFrom().getWorld().getName()) return;
			if(!instance.isVanished(e.getPlayer())) return;
			if(l.getWorld().getName() != "world") {
				Game g = instance.getGameManager().getGameByWorld(l.getWorld().getName());
				if(g == null) return;
				g.addPlayers(new BSPlayer(e.getPlayer().getName(), true, instance, g));
				instance.getGameManager().setPlayerToGame(e.getPlayer(), g.getID());
				for(Player p : Bukkit.getOnlinePlayers()) {
					Game gp = instance.getGameManager().getGameByPlayer(p);
					if(gp == null) continue;
					if(gp.getID() == g.getID()) {
						e.getPlayer().showPlayer(p);
					}else {
						e.getPlayer().hidePlayer(p);
					}
					p.hidePlayer(e.getPlayer());
				}
			}
		}
	}
	

}
