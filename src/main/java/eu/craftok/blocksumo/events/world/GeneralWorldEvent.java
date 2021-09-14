package eu.craftok.blocksumo.events.world;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class GeneralWorldEvent implements Listener {
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		e.setCancelled(e.toWeatherState());
	}
	
	@EventHandler
	public void onEntityDrop(EntitySpawnEvent e) {
		if(e.getEntityType() == EntityType.DROPPED_ITEM) {
			Item it = (Item) e.getEntity();
			if(it.getItemStack().getType() == Material.WOOL) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerChangeWorld(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		World w = e.getFrom();
		for(Player p2 : Bukkit.getOnlinePlayers()) {
			if(p2.getName() == p.getName()) continue;
			if(p2.getWorld().getName() == w.getName()) {
				p2.showPlayer(p);
				p.showPlayer(p2);
				continue;
			}else {
				p2.hidePlayer(p);
				p.hidePlayer(p2);
				continue;
			}
		}
	}
	
}
