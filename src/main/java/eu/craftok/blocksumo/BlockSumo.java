package eu.craftok.blocksumo;

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
		saveDefaultConfig();
		this.map = new MapManager(this);
		this.gamemanager = new GameManager(this);
		instance = this;
		registerAllTimers();
		registerEvents();
		BonusManager.registerItems();
		getCommand("start").setExecutor(new StartCMD());
	}
	
	public void registerAllTimers() {
		TimerManager.registerTimer(new DeathMatchTimer(getConfig().getInt("time.deathmatch")));
		TimerManager.registerTimer(new MegaBonusTimer());
		TimerManager.registerTimer(new BonusTimer());
		TimerManager.registerTimer(new DragonTimer(getConfig().getInt("time.dragon")));
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
