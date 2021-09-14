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
	
	public MapArena(String name, ArrayList<String> spawns, String lobby, String world, String bonus) {
		this.spawns = spawns;
		this.name = name;
		this.lobby = lobby;
		this.world = world;
		this.bonus = bonus;
	}
	
	public Location getBonusLocation(String world) {
		String[] location = bonus.split(";");
		return new Location(Bukkit.getWorld(world), Double.parseDouble(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]));
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<String> getSpawns() {
		return spawns;
	}
	
	public void teleport(Player p, String world) {
		int randomIndex = new Random().nextInt(spawns.size());
		String[] location = spawns.get(randomIndex).split(";");
		p.teleport(new Location(Bukkit.getWorld(world), Double.parseDouble(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2])));
	}

	
	public String getWorld() {
		return world;
	}
	
	public Location getLobby(String world) {
		String[] location = lobby.split(";");
		return new Location(Bukkit.getWorld(world), Double.parseDouble(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]));
	}
	
	public Location getLobby() {
		String[] location = lobby.split(";");
		return new Location(Bukkit.getWorld(world), Double.parseDouble(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]));
	}
	
	public void initWorldBorder(String world) {
		WorldBorder worldborder = Bukkit.getWorld(world).getWorldBorder();
		worldborder.setCenter(0, 0);
		worldborder.setSize(60);
	}
	
}
