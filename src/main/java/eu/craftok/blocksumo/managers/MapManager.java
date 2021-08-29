package eu.craftok.blocksumo.managers;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.map.MapArena;

public class MapManager {
	private File mapfolder;
	private ArrayList<MapArena> maps = new ArrayList<MapArena>();
	private BlockSumo instance;
	
	public MapManager(BlockSumo instance) {
		loadMaps(new File(instance.getDataFolder(), "maps"));
		this.instance = instance;
	}
	
	public void loadMaps(File directory) {
		mapfolder = directory;
		if(!directory.exists()) {
			directory.mkdir();
			return;
		}
		if(directory.listFiles().length == 0) {
			return;
		}
		for(File files : directory.listFiles()) {
			loadMap(files);
		}
	}
	
	public void loadMap(File f) {
		FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
		int posnb = fc.getInt("positions.length");
		System.out.println(fc.getString("locations.bonus"));
		ArrayList<String> locations = new ArrayList<String>();
		for(int i = 1; i <= posnb; i++) {
			locations.add(fc.getString("positions."+i));
		}
		maps.add(new MapArena(f.getName(), locations, fc.getString("locations.lobby"), fc.getString("world.file"), fc.getString("locations.bonus"), instance));
	}

	 public File getMapFolder() {
		return mapfolder;
	}
	
	public ArrayList<MapArena> getMaps() {
		return maps;
	}

}
