package eu.craftok.blocksumo.managers;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.entity.Player;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.game.Game;
import eu.craftok.blocksumo.map.MapArena;

public class GameManager {
	private BlockSumo instance;
	private HashMap<String, Integer> playersgameid;
	private HashMap<Integer, Game> games;
	private AtomicInteger count = new AtomicInteger(0);
	private int currentgame;
	
	public GameManager(BlockSumo instance) {
		games = new HashMap<>();
		playersgameid = new HashMap<>();
		this.instance = instance;
	}
	
	public void createGame() {
		MapArena map = pickRandomMap();
		Game g = new Game(instance, count.getAndIncrement(), map);
		currentgame = g.getID();
		g.generateWorld();
		games.put(g.getID(), g);
		System.out.println("Creating game with map "+map.getWorld()+" with id : "+g.getID());
		return;
	}
	
	public HashMap<Integer, Game> getGames() {
		return games;
	}
	
	public void removeGame(int id) {
		games.remove(id);
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
	
	public int getGameNumbers() {
		return games.size();
	}
	
	public Game getGameByID(int id) {
		return games.get(id);
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
