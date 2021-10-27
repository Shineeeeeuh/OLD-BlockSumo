package eu.craftok.blocksumo;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import eu.craftok.blocksumo.commands.StartCMD;
import eu.craftok.blocksumo.events.InGameEvents;
import eu.craftok.blocksumo.events.LobbyEvents;
import eu.craftok.blocksumo.events.ProtectionEvents;
import eu.craftok.blocksumo.manager.GameManager;
import eu.craftok.blocksumo.manager.bonus.BonusManager;
import eu.craftok.blocksumo.manager.map.MapManager;
import eu.craftok.blocksumo.manager.timers.TimerManager;
import eu.craftok.blocksumo.timers.BonusTimer;
import eu.craftok.blocksumo.timers.DeathMatchTimer;
import eu.craftok.blocksumo.timers.DragonTimer;
import eu.craftok.blocksumo.timers.MegaBonusTimer;

public class BlockSumo extends JavaPlugin{
	private MapManager map;
	private static BlockSumo instance;
	private GameManager gamemanager;
	
	@Override
	public void onEnable() {
		this.map = new MapManager(this);
		this.gamemanager = new GameManager(this);
		instance = this;
		registerAllTimers();
		registerEvents();
		BonusManager.registerItems();
		getCommand("start").setExecutor(new StartCMD());
	}
	
	public void registerAllTimers() {
		TimerManager.registerTimer(new DeathMatchTimer());
		TimerManager.registerTimer(new MegaBonusTimer());
		TimerManager.registerTimer(new BonusTimer());
		TimerManager.registerTimer(new DragonTimer());
	}
	
	public void createGame() {
		if(gamemanager.getPlayedMap().getWorld().equalsIgnoreCase("Quartz")) {
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "swm load-template Quartz Quartz-1");
			gamemanager.getPlayedMap().setWorld("Quartz-1");
		}else{
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "swm load "+gamemanager.getPlayedMap().getWorld());
		}
	}

	
	public static BlockSumo getInstance() {
		return instance;
	}
	
	public MapManager getMapManager() {
		return map;
	}
	
	public GameManager getGameManager() {
		return gamemanager;
	}
	
	public void registerEvents() {
		getServer().getPluginManager().registerEvents(new LobbyEvents(), this);
		getServer().getPluginManager().registerEvents(new ProtectionEvents(), this);
		getServer().getPluginManager().registerEvents(new InGameEvents(), this);
	}
	
}
