package eu.craftok.blocksumo.managers;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.entity.Player;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.enums.GameState;
import eu.craftok.blocksumo.game.Game;
import eu.craftok.blocksumo.map.MapArena;

public class GameManager {
	private BlockSumo instance;
	private HashMap<String, Integer> playersgameid;
	private HashMap<Integer, Game> games;
	private AtomicInteger count = new AtomicInteger(0);
	private int currentgame, nextgame;
	
	public GameManager(BlockSumo instance) {
		games = new HashMap<>();
		playersgameid = new HashMap<>();
		this.instance = instance;
		this.nextgame = 0;
		this.currentgame = 0;
	}
	
	public void setNextGame(int nextgame) {
		this.nextgame = nextgame;
	}
	
	public int getNextGame() {
		return nextgame;
	}
	
	public void createGame() {
		if(getGamePlayNumbers() == 5 || getGameCreated() == 10) return;
		if(nextgame != 0) {
			Game g = getGameByID(currentgame);
			if(g.getState() != GameState.INGAME) {
				currentgame = nextgame;
				nextgame = 0;
			}
			return;
		}
		MapArena map = pickRandomMap();
		Game g = new Game(instance, count.getAndIncrement(), map);
		g.generateWorld();
		games.put(g.getID(), g);
		System.out.println("Creating game with map "+map.getWorld()+" with id : "+g.getID());
		if(currentgame == 0) {
			currentgame = g.getID();
			return;
		}
		Game gc = getGameByID(currentgame);
		if(gc == null) {
			currentgame = g.getID();
			return;
		}
		if(gc.getState() != GameState.INGAME) {
			currentgame = g.getID();
			return;
		}
		nextgame = g.getID();
		return;
		
	}
	
	public HashMap<Integer, Game> getGames() {
		return games;
	}
	
	public void removeGame(int id) {
		games.remove(id);
	}
	
	public boolean isDisconnect() {
		if(getGameByID(currentgame).isLoaded()) {
			return false;
		}else {
			return true;
		}
	}
	
	public Game getGameByWorld(String world) {
		for(Game g : games.values()) {
			if(g.getWorld().equalsIgnoreCase(world)) {
				return g;
			}
			continue;
		}
		return null;
	}
	
	public int getGamePlayNumbers() {
		int i = 0;
		for(Game g : games.values()) {
			if(g.getState() != GameState.WAITING) {
				i++;
			}
		}
		return i;
	}
	
	public int getGameCreated() {
		return games.values().size();
	}
	
	public Game getGameByID(int id) {
		return games.get(id);
	}
	
	public void setCurrentGame(int currentgame) {
		this.currentgame = currentgame;
	}
	
	public Game getGameByPlayer(Player p) {
		return games.get(playersgameid.get(p.getName()));
	}
	
	public Game getGameByPlayerName(String name) {
		return games.get(playersgameid.get(name));
	}
	
	public void setPlayerToGame(Player p, int id) {
		playersgameid.put(p.getName(), id);
	}
	
	public void removePlayerToGame(Player p) {
		playersgameid.remove(p.getName());
	}
	
	public MapArena pickRandomMap() {
		int randomIndex = new Random().nextInt(instance.getMapManager().getMaps().size());
		return instance.getMapManager().getMaps().get(randomIndex);
	}
	
	public int getCurrentGame() {
		return currentgame;
	}
	
	
}
