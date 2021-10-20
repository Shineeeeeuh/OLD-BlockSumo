package eu.craftok.blocksumo.events;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.manager.GameManager;
import eu.craftok.blocksumo.manager.GameManager.STATE;
import eu.craftok.blocksumo.manager.player.BSPlayerManager;
import eu.craftok.core.common.CoreCommon;

public class ProtectionEvents implements Listener {
	
	public static boolean closed = false;
	
	@EventHandler
	public void onWeather(WeatherChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		GameManager g = BlockSumo.getInstance().getGameManager();
		if(g.getState() != STATE.LOBBY) return;
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		GameManager g = BlockSumo.getInstance().getGameManager();
		Player p = e.getPlayer();
		if(g.getState() != STATE.INGAME) {
			e.setFormat(CoreCommon.getCommon().getUserManager().getUserByName(p.getName()).getDisplayName()+" §f: %2$s");
			return;
		}else {
			if(BSPlayerManager.getPlayer(p.getName()).isSpectator()) {
				e.setCancelled(true);
				BSPlayerManager.getDeadPlayers().forEach(b -> {
					b.getPlayer().sendMessage("§7[SPECTATEUR] §f"+p.getName()+" §7: §f"+e.getMessage());
				});
				return;
			}else {
				e.setFormat(CoreCommon.getCommon().getUserManager().getUserByName(p.getName()).getDisplayName()+" §f: %2$s");
			}
		}
	}
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		GameManager g = BlockSumo.getInstance().getGameManager();
		if(g.getState() == STATE.LOBBY || g.getState() == STATE.WAITING) {
			e.setCancelled(true);
		}else {
			if(e.getBlock().getType() == Material.WOOL) {
				if(g.getBlocks().containsKey(e.getBlock().getLocation())) {
					g.getBlocks().remove(e.getBlock().getLocation());
				}
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
				return;
			}else {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		GameManager g = BlockSumo.getInstance().getGameManager();
		if(g.getState() == STATE.LOBBY || g.getState() == STATE.WAITING) {
			e.setCancelled(true);
		}else {
			if(e.getBlock().getType() == Material.WOOL) {
				e.getPlayer().getItemInHand().setAmount(64);
				g.getBlocks().put(e.getBlock().getLocation(), 0);
			}
		}
	}
	
	@EventHandler
	public void onSpawn(EntitySpawnEvent e) {
		if(e.getEntityType() != EntityType.ENDER_DRAGON && e.getEntityType() != EntityType.DROPPED_ITEM) e.setCancelled(true);
		return;
	}
	
	@EventHandler
	public void onServerListPing(ServerListPingEvent e) {
		if(closed) {
			e.setMotd("INGAME");
		}else {
			e.setMotd("ONLINE");
		}
	}
}
