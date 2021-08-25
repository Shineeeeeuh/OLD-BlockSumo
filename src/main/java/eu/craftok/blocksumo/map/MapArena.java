package eu.craftok.blocksumo.map;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

public class MapArena {
	private ArrayList<String> spawns;
	private String name, lobby, world, bonus;
	ArrayList<String> locationrandom = new ArrayList<>();
	
	public MapArena(String name, ArrayList<String> spawns, String lobby, String world, String bonus) {
		this.spawns = spawns;
		this.name = name;
		this.lobby = lobby;
		this.world = world;
		this.locationrandom = spawns;
		this.bonus = bonus;
	}
	
	public Location getBonusLocation() {
		String[] location = bonus.split(";");
		return new Location(Bukkit.getWorld(world), Double.parseDouble(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]));
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<String> getSpawns() {
		return spawns;
	}
	
	public void teleportToSpawn(Player p) {
		int randomIndex = new Random().nextInt(locationrandom.size());
		String[] location = locationrandom.get(randomIndex).split(";");
		p.teleport(new Location(Bukkit.getWorld(world), Double.parseDouble(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2])));
		locationrandom.remove(locationrandom.get(randomIndex));
	}

	
	public void teleport(Player p) {
		int randomIndex = new Random().nextInt(this.spawns.size());
		String[] location = this.spawns.get(randomIndex).split(";");
		int i = (int) Double.parseDouble(location[0]);
		int i2 = (int) Double.parseDouble(location[2]);
		p.teleport(Bukkit.getWorld(this.world).getHighestBlockAt(i, i2).getLocation());
	}
	
	public String getWorld() {
		return world;
	}
	
	public Location getLobby() {
		String[] location = lobby.split(";");
		return new Location(Bukkit.getWorld(world), Double.parseDouble(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]));
	}
	
	public void initWorldBorder() {
		WorldBorder worldborder = Bukkit.getWorld(world).getWorldBorder();
		worldborder.setCenter(0, 0);
		worldborder.setSize(60);
	}
	
}
