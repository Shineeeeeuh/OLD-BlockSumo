package eu.craftok.blocksumo.events.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener; 
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
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
		if(bsp.isSpectator()) {
			g.sendMessageToSpec("§7[SPECTATEUR] §f"+e.getPlayer().getName()+" : "+e.getMessage());
			return;
		}
		User u = CoreCommon.getCommon().getUserManager().getUserByUniqueId(e.getPlayer().getUniqueId());
		g.broadcastMessage(u.getDisplayName()+" §f: %2$s");
	}
	
	@EventHandler
	public void onServerListPing(ServerListPingEvent e) {
		if(instance.getGameManager().getGameNumbers() == 5) {
			e.setMotd("INGAME");
		}else {
			e.setMotd("ONLINE");
		}
	}
	

}
