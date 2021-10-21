package eu.craftok.blocksumo.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.manager.GameManager;
import eu.craftok.blocksumo.manager.GameManager.STATE;
import eu.craftok.blocksumo.manager.player.BSPlayer;
import eu.craftok.blocksumo.manager.player.BSPlayerManager;
import eu.craftok.blocksumo.tasks.StartTask;
import eu.craftok.core.common.CoreCommon;

public class LobbyEvents implements Listener {
	
	private static int timer = 0; 
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(isVanished(p)) {
			e.setJoinMessage(null);
			BSPlayer bp = new BSPlayer(p.getName(), true);
			bp.initSB();
			BSPlayerManager.addPlayer(bp);
			return;
		}else {
			if(BlockSumo.getInstance().getGameManager().getState() != STATE.LOBBY && (p.hasPermission("craftok.mod") || p.hasPermission("craftok.admin"))) {
				e.setJoinMessage(null);
				BSPlayer bp = new BSPlayer(p.getName(), true);
				bp.initSB();
				BSPlayerManager.addPlayer(bp);
				p.teleport(BlockSumo.getInstance().getGameManager().getPlayedMap().getBonus().add(0, 3, 0));
				Bukkit.getScheduler().runTaskLater(BlockSumo.getInstance(), () -> {
					p.setGameMode(GameMode.SPECTATOR);
				}, 1);
				return;
			}
			BSPlayer bp = new BSPlayer(p.getName());
			BSPlayerManager.addPlayer(bp);
			bp.initPlayerAbilities();
			bp.initSB();
			bp.loadWaitingItems();
			BSPlayerManager.updateSB();
			int players = BSPlayerManager.getPlayersNB();
			e.setJoinMessage("�f"+p.getName()+" §7vient de rejoindre la partie §a("+players+"/8)");
			if(timer != 0) {
				if(players == 8) {
					if(StartTask.getTimer() > 3) {
						StartTask.setTimer(3);
					}
				}else if(players == 5) {
					if(StartTask.getTimer() > 30) {
						StartTask.setTimer(30);
					}
				}
			}else if(players == 2) {
				StartTask st = new StartTask();
				st.runTaskTimer(BlockSumo.getInstance(), 0L, 20L);
				timer = st.getTaskId();
			}
		}
		Bukkit.getScheduler().runTaskLater(BlockSumo.getInstance(), () -> {
			Location l = BlockSumo.getInstance().getGameManager().getPlayedMap().getLobby();
			p.teleport(l);
			p.setGameMode(GameMode.SURVIVAL);
		}, 1);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		GameManager g = BlockSumo.getInstance().getGameManager();
		if(g.getState() == STATE.LOBBY) {
			BSPlayerManager.removePlayer(p.getName());
			e.setQuitMessage("�f"+p.getName()+" §7vient de quitter la partie §c("+BSPlayerManager.getPlayersNB()+"/8)");
			if(BSPlayerManager.getPlayersNB() == 1) {
				if(ProtectionEvents.closed) {
					ProtectionEvents.closed = false;
				}
				if(timer != 0) {
					Bukkit.getScheduler().cancelTask(timer);
					timer = 0;
					Bukkit.getOnlinePlayers().forEach(po -> po.setLevel(0));
				}
			}
			return;
		}else {
			e.setQuitMessage(null);
			BSPlayer bp = BSPlayerManager.getPlayer(p.getName());
			if(bp.getLastDamager() != null) {
				if(Bukkit.getPlayer(bp.getLastDamager()) != null) {
					CoreCommon.getCommon().getUserManager().getUserByName(bp.getLastDamager()).addCoins(2);
					Bukkit.getPlayer(bp.getLastDamager()).sendMessage("§c§lCRAFTOK §8» §c§l+2 §7coins pour avoir tuer §c"+p.getName());
				}
			}
			BSPlayerManager.removePlayer(p.getName());
			BSPlayerManager.updateSB();
			g.checkWin();
		}
	}
	
	public boolean isVanished(Player p) {
		if(p != null && p.hasMetadata("vanished") && !p.getMetadata("vanished").isEmpty()) {
			return p.getMetadata("vanished").get(0).asBoolean();
		}else {
			return false;
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(BlockSumo.getInstance().getGameManager().getState() != STATE.LOBBY) return;
		if(e.getItem() == null || e.getItem().getType() == Material.AIR) return;
		if(e.getPlayer() == null) return;
		if(e.getAction() == null) return;
		if(e.getItem().getType() == Material.BED) {
			e.setCancelled(true);
			e.getPlayer().kickPlayer(" ");
			return;
		}
		return;
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if(BlockSumo.getInstance().getGameManager().getState() != STATE.LOBBY && (!p.hasPermission("craftok.mod") || !p.hasPermission("craftok.admin"))) {
			e.disallow(Result.KICK_OTHER, "§cDésolé, de vous le dire ! Mais vous ne pouvez pas spec la partie !");
			e.setResult(Result.KICK_OTHER);
		}
	}
}
