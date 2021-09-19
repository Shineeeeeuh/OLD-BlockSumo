package eu.craftok.blocksumo.events.world;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
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
	
}
