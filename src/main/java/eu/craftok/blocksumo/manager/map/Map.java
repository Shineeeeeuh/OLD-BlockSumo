package eu.craftok.blocksumo.manager.map;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Map {
	private String world;
	private String name;
	private ArrayList<double[]> spawns;
	private double[] bonus, lobby;
	
	public Map(String name, ArrayList<String> spawns, String lobby, String world, String bonus) {
		this.name = name;
		this.lobby = stringToDouble(lobby);
		this.bonus = stringToDouble(bonus);
		this.world = world;
		this.spawns = new ArrayList<double[]>();
		for(String spawn : spawns) {
			this.spawns.add(stringToDouble(spawn));
		}
	}
	
	private double[] stringToDouble(String s) {
		String[] locations = s.split(";");
		double[] a = new double[3];
		a[0] = Double.parseDouble(locations[0]);
		a[1] = Double.parseDouble(locations[1]);
		a[2] = Double.parseDouble(locations[2]);
		return a;
	}
	
	private Location doubleToLocation(double[] d) {
		return new Location(Bukkit.getWorld(world), d[0], d[1], d[2]);
	}
	
	public ArrayList<Location> getSpawns() {
		ArrayList<Location> l = new ArrayList<Location>();
		spawns.forEach(d -> l.add(doubleToLocation(d)));
		return l;
	}
	
	public Location getBonus() {
		return doubleToLocation(bonus);
	}
	
	public Location getLobby() {
		return doubleToLocation(lobby);
	}
	
	public String getName() {
		return name;
	}
	
	public String getWorld() {
		return world;
	}
	
	public void teleport(Player p) {
		int randomIndex = new Random().nextInt(spawns.size());
		p.teleport(doubleToLocation(spawns.get(randomIndex)));
	}
	
}
