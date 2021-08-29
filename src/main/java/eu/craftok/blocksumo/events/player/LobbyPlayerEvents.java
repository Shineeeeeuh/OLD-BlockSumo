package eu.craftok.blocksumo.events.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.enums.GameState;
import eu.craftok.blocksumo.managers.GameManager;
import eu.craftok.blocksumo.player.BSPlayer;
import eu.craftok.blocksumo.player.BSPlayerManager;
import eu.craftok.blocksumo.tasks.StartTask;

public class LobbyPlayerEvents implements Listener{
	
	private BlockSumo instance;
	private GameManager gamemanager;
	private StartTask starttask;
	private BSPlayerManager playermanager;
	
	public LobbyPlayerEvents(BlockSumo instance) {
		this.instance = instance;
		this.gamemanager = instance.getGameManager();
		this.playermanager = instance.getPlayerManager();
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if(instance.getPlayerManager().getPlayers().size() > 8) {
			BSPlayer bsp = new BSPlayer(player.getName(), true, instance);
			e.setJoinMessage(null);
			bsp.initPlayerAbilities();
			bsp.loadSpectator();
			playermanager.addPlayers(bsp);
			playermanager.updateSB();
			return;
		}
		if(gamemanager.getState() == GameState.INGAME || gamemanager.getState() == GameState.FINISH) {
			BSPlayer bsp = new BSPlayer(player.getName(), true, instance);
			e.setJoinMessage(null);
			bsp.initPlayerAbilities();
			bsp.loadSpectator();
			playermanager.addPlayers(bsp);
			playermanager.updateSB();
		}else {
			BSPlayer bsp = new BSPlayer(player.getName(), instance);
			playermanager.addPlayers(bsp);
			int players = playermanager.getPlayers().size();
			e.setJoinMessage("§f"+player.getName()+" §7vient de rejoindre la partie §a("+players+"/8)");
			bsp.loadWaitingItems();
			bsp.loadScoreboard();
			if(players > 1) {
				playermanager.updateSB();
			}
			Location l = gamemanager.getPlayedMap().getLobby();
			l.getChunk().load();
			Bukkit.getScheduler().runTaskLater(instance, new BukkitRunnable() {
				
				@Override
				public void run() {
					player.getPlayer().teleport(gamemanager.getPlayedMap().getLobby());
				}
			}, 1);
			if(players == 2) {
				starttask = new StartTask(instance);
				starttask.runTaskTimer(instance, 20, 20);
				starttask.setTaskID(starttask.getTaskId());
				return;
			}
			if(players == 8) {
				if(starttask.getTimer() > 5) {
					StartTask.setTimer(5);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if(gamemanager.getState() == GameState.INGAME || gamemanager.getState() == GameState.FINISH) {
			e.setQuitMessage(null);
			if(playermanager.getPlayer(player.getName()) != null) {
				playermanager.removePlayers(playermanager.getPlayer(player.getName()));
				playermanager.updateSB();
				gamemanager.checkWin();
			}
			if(starttask != null && playermanager.getPlayers().size() == 1) {
				starttask.cancel();
				return;
			}
		}else {
			e.setQuitMessage("§f"+player.getName()+" §7vient de quitter la partie §c("+playermanager.getPlayers().size()+"/8)");
			playermanager.updateSB();
			playermanager.removePlayers(playermanager.getPlayer(player.getName()));
			if(starttask != null && playermanager.getPlayers().size() == 1) {
				starttask.cancel();
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageEvent e) {
		if(gamemanager.getState() == GameState.WAITING || gamemanager.getState() == GameState.FINISH || gamemanager.getState() == GameState.TIMER) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		GameManager gamemanager = instance.getGameManager();
		if(gamemanager.getState() == GameState.INGAME && e.getBlock().getType() == Material.WOOL) {
			if(gamemanager.getBlocksPlaced().containsKey(e.getBlock().getLocation())) {
				gamemanager.getBlocksPlaced().remove(e.getBlock().getLocation());
			}
			return;
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		GameManager gamemanager = instance.getGameManager();
		if(gamemanager.getState() == GameState.INGAME && e.getBlock().getType() == Material.WOOL) {
			gamemanager.getBlocksPlaced().put(e.getBlock().getLocation(), 0);
			return;
		}
		e.setCancelled(true);
	}

}
