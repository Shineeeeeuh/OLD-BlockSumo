package eu.craftok.blocksumo.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.Lists;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.events.InGameEvents;
import eu.craftok.blocksumo.manager.map.Map;
import eu.craftok.blocksumo.manager.player.BSPlayerManager;
import eu.craftok.blocksumo.tasks.EndTask;
import eu.craftok.blocksumo.tasks.GameTask;
import eu.craftok.core.common.CoreCommon;
import eu.craftok.utils.PlayerUtils;
import eu.craftok.utils.firework.FireworkBuilder;
import eu.craftok.utils.firework.FireworkUtils;

public class GameManager {
	
	private STATE state;
	private Map playedmap;
	private BlockSumo instance;
	private HashMap<Location, Integer> blocks;
	
	public enum STATE{
		LOBBY, WAITING, INGAME, FINISH;
	}
	
	public GameManager(BlockSumo instance) {
		this.instance = instance;
		this.state = STATE.LOBBY;
		this.playedmap = pickrandomMap();
		this.blocks = new HashMap<>();
	}
	
	public HashMap<Location, Integer> getBlocks() {
		return blocks;
	}
	
	public STATE getState() {
		return state;
	}
	
	public Map getPlayedMap() {
		return playedmap;
	}
	
	private Map pickrandomMap() {
		int randomindex = new Random().nextInt(instance.getMapManager().getMaps().size());
		InGameEvents.y = (int) (instance.getMapManager().getMaps().get(randomindex).getBonus().getY()+10);
		return instance.getMapManager().getMaps().get(randomindex);
	}
	
	public void startGame() {
		state = STATE.WAITING;
		ArrayList<Location> locs = Lists.newArrayList(playedmap.getSpawns());
		WorldBorder worldborder = Bukkit.getWorld(playedmap.getWorld()).getWorldBorder();
		worldborder.setCenter(0, 0);
		worldborder.setSize(60);
		worldborder.setDamageBuffer(0);
		worldborder.setWarningDistance(0);
		BSPlayerManager.getAlivePlayers().forEach(bp -> {
			Player p = bp.getPlayer();
			int r = new Random().nextInt(locs.size());
			p.getInventory().clear();
			p.teleport(locs.get(r));
			locs.remove(r);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 0, false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, 250, false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 250));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 250));
			p.setFlySpeed(0.0F);
			new PlayerUtils(p).sendTitle(10, 20, 10, "§6Préparation", "§e(Patientez...)");
		});
		BSPlayerManager.updateSB();
		Bukkit.getScheduler().runTaskLater(instance, () -> {
			state = STATE.INGAME;
			BSPlayerManager.getAlivePlayers().forEach(bp -> {
				Player p = bp.getPlayer();
				bp.loadKit();
				p.getActivePotionEffects().forEach(po -> p.removePotionEffect(po.getType()));
				p.setFlySpeed(0.2F);
				PlayerUtils u = new PlayerUtils(p);
				u.sendTitle(10, 20, 10, "§eBonne chance !", null);
				u.sendSound(Sound.ENDERDRAGON_GROWL, 1F);
			});
			new GameTask().runTaskTimer(instance, 0, 20L);
			BSPlayerManager.updateSB();
		}, 80);
	}
	
	public void checkWin() {
		if(state == STATE.FINISH) return;
		if(BSPlayerManager.getAlivePlayers().size() == 1) {
			state = STATE.FINISH;
			Bukkit.getScheduler().cancelAllTasks();
			Player p = BSPlayerManager.getAlivePlayers().get(0).getPlayer();
			p.setGameMode(GameMode.SPECTATOR);
			new PlayerUtils(p).sendTitle(10, 20, 10, "§6Tu as gagné !", "§eBien jouer !");
			new PlayerUtils(p).sendSound(Sound.LEVEL_UP, 1F);
			FireworkBuilder.summonInstantFirework(FireworkUtils.getRandomFireworkEffect(), playedmap.getBonus());
			FireworkBuilder.summonInstantFirework(FireworkUtils.getRandomFireworkEffect(), playedmap.getBonus().add(1, 0, 0));
			FireworkBuilder.summonInstantFirework(FireworkUtils.getRandomFireworkEffect(), playedmap.getBonus().add(-1, 0, 0));
			FireworkBuilder.summonInstantFirework(FireworkUtils.getRandomFireworkEffect(), playedmap.getBonus().add(0, 0, 0));
			FireworkBuilder.summonInstantFirework(FireworkUtils.getRandomFireworkEffect(), playedmap.getBonus().add(0, 0, 1));
			FireworkBuilder.summonInstantFirework(FireworkUtils.getRandomFireworkEffect(), playedmap.getBonus().add(0, 0, -1));
			CoreCommon.getCommon().getUserManager().getUserByName(p.getName()).addCoins(5);
			p.sendMessage("§c§lCRAFTOK §8» §7Vous avez gagné §c5 coins §7!");
			Bukkit.broadcastMessage("§c§lCRAFTOK §8» §c"+p.getName()+" §7a gagné !");
			new EndTask().runTaskLater(instance, 160);
		}
	}
	
	

}
