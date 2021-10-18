package eu.craftok.blocksumo.manager.map;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Map {
	private String world;
	private Location lobby, bonus;
	private String name;
	private ArrayList<Location> spawns;
	
	public Map(String name, ArrayList<String> spawns, String lobby, String world, String bonus) {
		this.name = name;
		this.lobby = stringToLocation(lobby, world);
		this.bonus = stringToLocation(bonus, world);
		this.world = world;
		this.spawns = new ArrayList<Location>();
		for(String spawn : spawns) {
			this.spawns.add(stringToLocation(spawn, world));
		}
	}
	
	private Location stringToLocation(String s, String world) {
		String[] location = s.split(";");
		return new Location(Bukkit.getWorld(world), Double.parseDouble(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]));
	}
	
	public ArrayList<Location> getSpawns() {
		return spawns;
	}
	
	public Location getBonus() {
		return bonus;
	}
	
	public Location getLobby() {
		return lobby;
	}
	
	public String getName() {
		return name;
	}
	
	public String getWorld() {
		return world;
	}
	
	public void teleport(Player p) {
		int randomIndex = new Random().nextInt(spawns.size());
		p.teleport(spawns.get(randomIndex));
	}
	
}
