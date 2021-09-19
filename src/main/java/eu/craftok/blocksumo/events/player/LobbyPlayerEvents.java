package eu.craftok.blocksumo.events.player;

import java.util.HashMap;

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
import eu.craftok.blocksumo.game.Game;
import eu.craftok.blocksumo.managers.GameManager;
import eu.craftok.blocksumo.player.BSPlayer;
import eu.craftok.blocksumo.tasks.StartTask;

public class LobbyPlayerEvents implements Listener{
	
	private BlockSumo instance;
	private GameManager gamemanager;
	private static HashMap<Integer, StartTask> starttaskid;
	
	public LobbyPlayerEvents(BlockSumo instance) {
		this.instance = instance;
		this.gamemanager = instance.getGameManager();
		starttaskid = new HashMap<>();
	}
	
	public static HashMap<Integer, StartTask> getStartTaskID() {
		return starttaskid;
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage("");
		Player player = e.getPlayer();
		Game g = gamemanager.getGameByID(instance.getGameManager().getCurrentGame());
		BSPlayer bsp = new BSPlayer(player.getName(), instance, g);
		g.addPlayers(bsp);
		for(Player p2 : Bukkit.getOnlinePlayers()) {
			if(p2.getName() == player.getName()) continue;
			if(instance.getGameManager().getGameByPlayer(p2).getID() == g.getID()) {
				p2.showPlayer(player);
				player.showPlayer(p2);
				continue;
			}else {
				p2.hidePlayer(player);
				player.hidePlayer(p2);
				continue;
			}
		}
		int players = g.getPlayers().size();
		g.broadcastMessage("§f"+player.getName()+" §7vient de rejoindre la partie §a("+players+"/8)");
		gamemanager.setPlayerToGame(player, g.getID());
		bsp.loadWaitingItems();
		bsp.loadScoreboard();
		if(players > 1) {
			g.updateSB();
		}
		Location l = g.getMap().getLobby();
		l.setWorld(Bukkit.getWorld(g.getWorld()));
		l.getChunk().load();
		Bukkit.getScheduler().runTaskLater(instance, new BukkitRunnable() {
			
			@Override
			public void run() {
				player.getPlayer().teleport(l);
			}
		}, 1);
		if(players == 2) {
			StartTask starttask = new StartTask(instance, g);
			starttask.runTaskTimer(instance, 20, 20);
			starttask.setTaskID(starttask.getTaskId());
			starttaskid.put(g.getID(), starttask);
			return;
		}
		if(players == 8) {
			if(starttaskid.get(g.getID()).getTimer() > 5) {
				starttaskid.get(g.getID()).setTimer(5);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		e.setQuitMessage(null);
		Game g = gamemanager.getGameByPlayer(player);
		if(g == null) {
			gamemanager.removePlayerToGame(player);
			return;
		}
		if(g.getState() != null && g.getState() == GameState.INGAME) {
			if(g.getPlayer(player.getName()) != null) {
				g.removePlayers(g.getPlayer(player.getName()));
				g.updateSB();
				if(g.getState() == GameState.INGAME) {
					g.checkWin();
				}
			}
		}else {
			g.broadcastMessage("§f"+player.getName()+" §7vient de quitter la partie §c("+g.getPlayers().size()+"/8)");
			g.updateSB();
			g.removePlayers(g.getPlayer(player.getName()));
			if(starttaskid.get(g.getID()) != null && g.getPlayers().size() == 1) {
				starttaskid.get(g.getID()).cancel();
				return;
			}
			gamemanager.removePlayerToGame(player);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageEvent e) {
		Game g = gamemanager.getGameByPlayer((Player) e.getEntity());
		if(g.getState() == GameState.WAITING || g.getState() == GameState.FINISH || g.getState() == GameState.TIMER) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Game g = gamemanager.getGameByWorld(e.getBlock().getLocation().getWorld().getName());
		if(g.getState() == GameState.INGAME && e.getBlock().getType() == Material.WOOL) {
			if(g.getBlocksPlaced().containsKey(e.getBlock().getLocation())) {
				g.getBlocksPlaced().remove(e.getBlock().getLocation());
			}
			return;
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Game g = gamemanager.getGameByWorld(e.getBlock().getLocation().getWorld().getName());
		if(g.getState() == GameState.INGAME && e.getBlock().getType() == Material.WOOL) {
			g.getBlocksPlaced().put(e.getBlock().getLocation(), 0);
			return;
		}
		e.setCancelled(true);
	}

}
