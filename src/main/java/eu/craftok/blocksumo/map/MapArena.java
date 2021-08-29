package eu.craftok.blocksumo.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.player.BSPlayer;

public class MapArena {
	private ArrayList<String> spawns;
	private String name, lobby, world, bonus;
	private BlockSumo instance;
	
	public MapArena(String name, ArrayList<String> spawns, String lobby, String world, String bonus, BlockSumo instance) {
		this.spawns = spawns;
		this.name = name;
		this.lobby = lobby;
		this.world = world;
		this.instance = instance;
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
	
	public void teleportToSpawn() {
		List<String> locationrandom = Lists.newArrayList(spawns);
		for(BSPlayer bsp : instance.getPlayerManager().getAlivePlayers()) {
			Player p = bsp.getPlayer();
			int randomIndex = new Random().nextInt(locationrandom.size());
			String[] location = locationrandom.get(randomIndex).split(";");
			p.teleport(new Location(Bukkit.getWorld(world), Double.parseDouble(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2])));
			locationrandom.remove(locationrandom.get(randomIndex));
		}
	}

	
	public void teleport(Player p) {
		int randomIndex = new Random().nextInt(spawns.size());
		String[] location = this.spawns.get(randomIndex).split(";");
		int i = (int) Double.parseDouble(location[0]);
		int i2 = (int) Double.parseDouble(location[2]);
		p.teleport(Bukkit.getWorld(world).getHighestBlockAt(i, i2).getLocation());
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
