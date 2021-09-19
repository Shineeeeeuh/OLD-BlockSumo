package eu.craftok.blocksumo;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import eu.craftok.blocksumo.commands.StartCMD;
import eu.craftok.blocksumo.events.EventsRegister;
import eu.craftok.blocksumo.managers.BonusManager;
import eu.craftok.blocksumo.managers.GameManager;
import eu.craftok.blocksumo.managers.MapManager;
import eu.craftok.core.common.CoreCommon;

public class BlockSumo extends JavaPlugin{
	
	private GameManager gamemanager;
	private MapManager mapmanager;
	private BonusManager bonusmanager;
	private CoreCommon API;
	
	
	@Override
	public void onEnable() {
		this.mapmanager = new MapManager(this);
		this.gamemanager = new GameManager(this);
		new EventsRegister(this).registerEvents();
		bonusmanager = new BonusManager();
		this.API = CoreCommon.getCommon();
		getCommand("start").setExecutor(new StartCMD(this));
		gamemanager.createGame();
	}
	
	public CoreCommon getAPI() {
		return API;
	}
	
	public BonusManager getBonusManager() {
		return bonusmanager;
	}
	public GameManager getGameManager() {
		return gamemanager;
	}
	
	public MapManager getMapManager() {
		return mapmanager;
	}
	
	public boolean isVanished(Player p) {
		if(p != null && p.hasMetadata("vanished") && !p.getMetadata("vanished").isEmpty()) {
			return p.getMetadata("vanished").get(0).asBoolean();
		}else {
			return false;
		}
	}
}
