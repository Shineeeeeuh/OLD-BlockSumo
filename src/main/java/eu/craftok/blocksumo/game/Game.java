package eu.craftok.blocksumo.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.enums.GameState;
import eu.craftok.blocksumo.map.MapArena;
import eu.craftok.blocksumo.player.BSPlayer;
import eu.craftok.blocksumo.tasks.BlockTask;
import eu.craftok.blocksumo.tasks.BonusTask;
import eu.craftok.utils.PlayerUtils;

public class Game {
	private int id;
	private MapArena map;
	private String world;
	private ArrayList<BSPlayer> players;
	private GameState state;
	private BlockSumo instance;
	private HashMap<Location, Integer> blocksplaced;
	
	public Game(BlockSumo instance, int id, MapArena map) {
		world = id+"-"+map.getName();
		this.map = map;
		this.players = new ArrayList<>();
		this.id = id;
		this.instance = instance;
		blocksplaced = new HashMap<>();
		this.state = GameState.WAITING;
	}
	
	public HashMap<Location, Integer> getBlocksPlaced() {
		return blocksplaced;
	}
	
	public int getID() {
		return id;
	}
	
	public MapArena getMap() {
		return map;
	}
	
	public ArrayList<BSPlayer> getPlayers() {
		return players;
	}
	
	public void teleportToSpawn() {
		List<String> locationrandom = Lists.newArrayList(map.getSpawns());
		for(BSPlayer bsp : players) {
			if(bsp.isSpectator()) continue;
			int randomIndex = new Random().nextInt(locationrandom.size());
			String[] location = locationrandom.get(randomIndex).split(";");
			Player p = bsp.getPlayer();
			p.teleport(new Location(Bukkit.getWorld(world), Double.parseDouble(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2])));
			locationrandom.remove(locationrandom.get(randomIndex));
		}
	}
	
	@SuppressWarnings("deprecation")
	public void start() {
		updateSB();
		for(BSPlayer player : getPlayers()) {
			player.initPlayerAbilities();
			player.loadKit();
			player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 255));
			player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 9999999, 255));
		}
		teleportToSpawn();
		setState(GameState.TIMER);
		Game g = this;
		Bukkit.getScheduler().runTaskLater(instance, new BukkitRunnable() {
			
			@Override
			public void run() {
				for(BSPlayer bsp : players) {
					if(bsp.isSpectator()) continue;
					Player p = bsp.getPlayer();
					for (PotionEffect effect : p.getActivePotionEffects()) {
						p.removePotionEffect(effect.getType());
					}
					PlayerUtils utils = new PlayerUtils(p);
					utils.sendActionBar("§f"+5+" §c\u2764 §frestante(s).");
					utils.sendSound(Sound.ENDERDRAGON_GROWL, 1.0f);
					utils.sendTitle(10, 20, 10, "§6Bonne chance !", "");
				}
				setState(GameState.INGAME);
				new BonusTask(instance, g).runTaskTimer(instance, 600, 600);
				new BlockTask(instance, g).runTaskTimer(instance, 20, 20);
			}
		},60L);
	}
	
	public BSPlayer getPlayer(String name) {
		for(BSPlayer p : players) {
			if(p.getPlayerName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}
	
	public ArrayList<BSPlayer> getAlivePlayers(){
		ArrayList<BSPlayer> aliveplayers = new ArrayList<>();
		for(BSPlayer bsp : players) {
			if(bsp == null) continue;
			if(!bsp.isSpectator()) {
				aliveplayers.add(bsp);
			}
		}
		return aliveplayers;
	}
	
	public ArrayList<BSPlayer> getDeadPlayers(){
		ArrayList<BSPlayer> deadplayers = new ArrayList<>();
		for(BSPlayer bsp : players) {
			if(bsp.isSpectator()) {
				deadplayers.add(bsp);
			}
		}
		return deadplayers;
	}
	
	public void addPlayers(BSPlayer player) {
		players.add(player);
	}
	
	public void removePlayers(BSPlayer player) {
		players.remove(player);
	}
	
	public void updateSB() {
		List<String> lines = new ArrayList<>();
		lines.add(" ");
		lines.add("§c§lPARTIE");
		lines.add(" §fJoueurs §3» §b" + getAlivePlayers().size() + "/8");
		if (getState() == GameState.WAITING) {
		    lines.add(" §fStatus §3» §bAttente");
		}
		else {
		    lines.add(" §fStatus §3» §bEn jeu");
		    lines.add(" ");
		    for (BSPlayer bsp : getAlivePlayers()) {
		    	if(bsp.getPlayerName().length() >= 13) {
		    		lines.add("§f" + bsp.getPlayerName().substring(0, 13) + " §3» §b§l" + bsp.getLife() + " §c\u2764");
		    	}else {
		    		lines.add("§f" + bsp.getPlayerName() + " §3» §b§l" + bsp.getLife() + " §c\u2764");
		    	}
		    }
		}
		lines.add(" ");
		lines.add("§c§lJEU");
		lines.add(" §fMode §3» §bSolo");
		lines.add(" §fMap §3» §b" + map.getWorld());
		lines.add(" ");
		lines.add("§b[§fcraftok.fr§c]");
		for(BSPlayer bsp : getPlayers()) {
			bsp.updateSB(lines);
		}
	}
	
	public String getWorld() {
		return world;
	}
	
	public GameState getState() {
		return state;
	}
	
	public Location getBonusLoc() {
		return map.getBonusLocation(getWorld());
	}
	
	public void broadcastMessage(String message) {
		for(BSPlayer bsp : players) {
			bsp.getPlayer().sendMessage(message);
		}
	}
	
	public void sendMessageToSpec(String message) {
		for(BSPlayer bsp : getDeadPlayers()) {
			bsp.getPlayer().sendMessage(message);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void generateWorld() {
		try {
			SlimePlugin plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
			SlimeLoader loader = plugin.getLoader("file");
			SlimePropertyMap prop = new SlimePropertyMap();
			prop.setString(SlimeProperties.DIFFICULTY, "normal");
			prop.setInt(SlimeProperties.SPAWN_X, map.getLobby().getBlockX());
			prop.setInt(SlimeProperties.SPAWN_Y, map.getLobby().getBlockY()+3);
			prop.setInt(SlimeProperties.SPAWN_Z, map.getLobby().getBlockZ());
			prop.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
			prop.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
			SlimeWorld world = plugin.loadWorld(loader, map.getWorld(), true, prop).clone(this.world);
			Bukkit.getScheduler().runTaskAsynchronously(instance, new BukkitRunnable() {
				
				@Override
				public void run() {
					plugin.generateWorld(world);
				}
			});
		} catch (UnknownWorldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CorruptedWorldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NewerFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WorldInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteWorld() {
		SlimePlugin plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
		SlimeLoader loader = plugin.getLoader("file");
		try {
			loader.deleteWorld(this.world);
		} catch (UnknownWorldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setState(GameState state) {
		this.state = state;
	}
	
	public void checkWin() {
		if(getState() == GameState.FINISH || getState() == GameState.WAITING) {
			return;
		}
		if(getAlivePlayers().size() == 1) {
			BSPlayer bsp = getAlivePlayers().get(0);
			broadcastMessage("§c§lCRAFTOK §8» §c"+bsp.getPlayerName()+" §7a gagné !");
			new PlayerUtils(bsp.getPlayer()).sendTitle(10, 20, 10, "§eVous avez", "§6§lGAGNÉ");
			bsp.getPlayer().sendMessage("§c§lCRAFTOK §8» §7Vous avez gagné §c10 coins §7!");
			for(BSPlayer bspl : players) {
				bspl.getPlayer().setGameMode(GameMode.SPECTATOR);
			}
			instance.getAPI().getUserManager().getUserByName(bsp.getPlayerName()).addCoins(10);
		}
	}
	
	public void initWorldBorder() {
		map.initWorldBorder(world);
	}

	public void teleport(Player player) {
		int randomIndex = new Random().nextInt(map.getSpawns().size());
		String[] location = map.getSpawns().get(randomIndex).split(";");
		int i = (int) Double.parseDouble(location[0]);
		int i2 = (int) Double.parseDouble(location[2]);
		player.teleport(Bukkit.getWorld(world).getHighestBlockAt(i, i2).getLocation());
	}
}
