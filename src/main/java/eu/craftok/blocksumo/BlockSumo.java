package eu.craftok.blocksumo;

import org.bukkit.plugin.java.JavaPlugin;

import eu.craftok.blocksumo.commands.StartCMD;
import eu.craftok.blocksumo.events.EventsRegister;
import eu.craftok.blocksumo.managers.BonusManager;
import eu.craftok.blocksumo.managers.GameManager;
import eu.craftok.blocksumo.managers.MapManager;
import eu.craftok.blocksumo.player.BSPlayerManager;
import eu.craftok.core.common.CoreCommon;

public class BlockSumo extends JavaPlugin{
	
	private GameManager gamemanager;
	private MapManager mapmanager;
	private BSPlayerManager playermanager;
	private BonusManager bonusmanager;
	private CoreCommon API;
	
	@Override
	public void onEnable() {
		this.mapmanager = new MapManager(this);
		this.playermanager = new BSPlayerManager(this);
		this.gamemanager = new GameManager(this);
		new EventsRegister(this).registerEvents();
		bonusmanager = new BonusManager();
		this.API = CoreCommon.getCommon();
		getCommand("start").setExecutor(new StartCMD());
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
	
	public BSPlayerManager getPlayerManager() {
		return playermanager;
	}
}
