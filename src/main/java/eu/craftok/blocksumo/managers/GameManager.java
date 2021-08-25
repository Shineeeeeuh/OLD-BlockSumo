package eu.craftok.blocksumo.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.enums.GameState;
import eu.craftok.blocksumo.map.MapArena;
import eu.craftok.blocksumo.player.BSPlayer;
import eu.craftok.blocksumo.player.BSPlayerManager;
import eu.craftok.blocksumo.tasks.BlockTask;
import eu.craftok.blocksumo.tasks.BonusTask;
import eu.craftok.blocksumo.tasks.EndTask;
import eu.craftok.utils.PlayerUtils;

public class GameManager {
	private GameState state;
	private BlockSumo instance;
	private MapArena map;
	private HashMap<Location, Integer> blocksplaced;
	
	public GameManager(BlockSumo instance) {
		state = GameState.WAITING;
		this.instance = instance;
		pickRandomMap();
		blocksplaced = new HashMap<>();
	}
	
	public HashMap<Location, Integer> getBlocksPlaced() {
		return blocksplaced;
	}
	
	public GameState getState() {
		return state;
	}
	
	public void setState(GameState state) {
		this.state = state;
	}
	
	
	public void pickRandomMap() {
		int randomIndex = new Random().nextInt(instance.getMapManager().getMaps().size());
		map = instance.getMapManager().getMaps().get(randomIndex);
	}
	
	
	public MapArena getPlayedMap() {
		return map;
	}
	
	@SuppressWarnings("deprecation")
	public void startGame() {
		instance.getPlayerManager().updateSB();
		BSPlayerManager playermanager = instance.getPlayerManager();
		for(BSPlayer player : playermanager.getPlayers()) {
			player.initPlayerAbilities();
			player.loadKit();
			map.teleportToSpawn(player.getPlayer());
			player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 255));
			player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 9999999, 255));
		}
		instance.getGameManager().setState(GameState.TIMER);
		Bukkit.getScheduler().runTaskLater(instance, new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					for (PotionEffect effect : p.getActivePotionEffects()) {
						p.removePotionEffect(effect.getType());
					}
					PlayerUtils utils = new PlayerUtils(p);
					utils.sendActionBar("§f"+5+" §c\u2764 §frestante(s).");
					utils.sendSound(Sound.ENDERDRAGON_GROWL, 1.0f);
					utils.sendTitle(10, 20, 10, "§6Bonne chance !", "");
				}
				new BlockTask(instance).runTaskTimer(instance, 20L, 20L);
				instance.getGameManager().setState(GameState.INGAME);
			}
		}, 60);
		new BonusTask(instance).runTaskTimer(instance, 600, 600);
	}
	
	public void checkWin() {
		ArrayList<BSPlayer> aliveplayers = instance.getPlayerManager().getAlivePlayers();
		if(aliveplayers.size() == 1) {
			BSPlayer bsp = aliveplayers.get(0);
			Bukkit.broadcastMessage("§c§lCRAFTOK §7| §6§l"+bsp.getPlayerName()+" §ea gagné !");
			new PlayerUtils(bsp.getPlayer()).sendTitle(10, 20, 10, "§eVous avez", "§6§lGAGNÉ");
			new EndTask().runTaskTimer(instance, 0, 140);
			bsp.getPlayer().sendMessage("§c§lCraftok §7| §eVous avez gagner §610 §ecoins !");
			instance.getAPI().getUserManager().getUserByName(bsp.getPlayerName()).addCoins(10);
		}
	}
	
	
}
