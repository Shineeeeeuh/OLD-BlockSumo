package eu.craftok.blocksumo.events;

import org.bukkit.plugin.PluginManager;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.events.player.GeneralPlayerEvents;
import eu.craftok.blocksumo.events.player.InGameEvents;
import eu.craftok.blocksumo.events.player.LobbyPlayerEvents;
import eu.craftok.blocksumo.events.world.GeneralWorldEvent;

public class EventsRegister {
	
	private BlockSumo instance;
	
	public EventsRegister(BlockSumo instance) {
		this.instance = instance;
	}
	
	public void registerEvents() {
		PluginManager pm = instance.getServer().getPluginManager();
		pm.registerEvents(new LobbyPlayerEvents(instance), instance);
		pm.registerEvents(new GeneralPlayerEvents(instance), instance);
		pm.registerEvents(new GeneralWorldEvent(), instance);
		pm.registerEvents(new InGameEvents(instance), instance);
	}

}
