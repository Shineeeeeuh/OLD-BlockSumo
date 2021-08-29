package eu.craftok.blocksumo.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener; 
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.server.ServerListPingEvent;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.enums.GameState;
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
		BSPlayer bsp = instance.getPlayerManager().getPlayer(e.getPlayer().getName());
		if(bsp.isSpectator()) {
			for(BSPlayer bs : instance.getPlayerManager().getDeadPlayers()){
				Player p = bs.getPlayer();
				p.sendMessage("§7[SPECTATEUR] §f"+e.getPlayer().getName()+" : "+e.getMessage());
			}
			return;
		}
		User u = CoreCommon.getCommon().getUserManager().getUserByUniqueId(e.getPlayer().getUniqueId());
		e.setFormat(u.getDisplayName()+" §f: %2$s");
	}
	
	@EventHandler
	public void onServerListPing(ServerListPingEvent e) {
		if(instance.getGameManager().getState() == GameState.WAITING) {
			e.setMotd("ONLINE");
		}else {
			e.setMotd("INGAME");
		}
	}
	

}
