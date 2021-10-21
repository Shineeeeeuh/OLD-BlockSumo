package eu.craftok.blocksumo.manager.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.manager.GameManager;
import eu.craftok.blocksumo.manager.timers.TimerManager;
import eu.craftok.blocksumo.scoreboard.ScoreboardBuilder;
import eu.craftok.core.common.CoreCommon;
import eu.craftok.utils.ItemCreator;
import eu.craftok.utils.PlayerUtils;

public class BSPlayer {
	private String name;
	private ScoreboardBuilder sb;
	private int life;
	private String lastdamager;
	private boolean invincibility, spectator;
	
	public BSPlayer(String name) {
		this.name = name;
		this.life = 5;
		this.invincibility = false;
		this.lastdamager = null;
		this.spectator = false;
	}
	
	public BSPlayer(String name, boolean spectator) {
		this.name = name;
		this.life = 5;
		this.invincibility = true;
		this.lastdamager = null;
		this.spectator = spectator;
	}
	
	public boolean isSpectator() {
		return spectator;
	}
	
	public void setSpectator(boolean spectator) {
		this.spectator = spectator;
	}
	
	public void initPlayerAbilities() {
		Player p = getPlayer();
		p.setHealth(20D);
		p.setExp(0F);
		p.setFoodLevel(20);
		p.setAllowFlight(false);
		p.setLevel(0);
		p.setSaturation(12.8F);
		p.setMaxHealth(20.0D);
	}
	
	public void setLife(int life) {
		this.life = life;
	}
	
	public void loadWaitingItems() {
		Player p = getPlayer();
		PlayerInventory inventory = p.getInventory();
		inventory.setArmorContents(null);
		inventory.clear();
		inventory.setHeldItemSlot(4);
		inventory.setItem(8, new ItemCreator(Material.BED).setName("§cQuitter").getItemstack());
		p.updateInventory();
	}
	
	public void loadKit() {
		Player p = getPlayer();
		PlayerInventory inventory = p.getInventory();
		inventory.clear();
		inventory.setArmorContents(null);
		inventory.setItem(0, new ItemCreator(Material.SHEARS).setUnbreakable(true).setName("§bLa super Cisaille").addEnchantment(Enchantment.DIG_SPEED, 1).getItemstack());
		inventory.setItem(1, new ItemStack(Material.WOOL, 64, (byte) new Random().nextInt(15)));
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(name);
	}
	
	public void initSB() {
		GameManager g = BlockSumo.getInstance().getGameManager();
		sb = new ScoreboardBuilder(getPlayer());
		List<String> lines = new ArrayList<>();
		sb.updateTitle("§c§lBLOCKSUMO");
		lines.add(" ");
		lines.add("§c§lPARTIE");
		lines.add(" §fJoueurs §3» §b" + BSPlayerManager.getPlayersNB() + "/8");
		if (g.getState() == GameManager.STATE.LOBBY) {
		    lines.add(" §fStatus §3» §bAttente");
		}
		else {
		    lines.add(" §fStatus §3» §bEn jeu");
		    lines.add(" ");
		    for (BSPlayer bsp : BSPlayerManager.getAlivePlayers()) {
		    	if(bsp.getName().length() >= 13) {
		    		lines.add("§f" + bsp.getName().substring(0, 13) + " §3» §b§l" + bsp.getLife() + " §c\u2764");
		    	}else {
		    		lines.add("§f" + bsp.getName() + " §3» §b§l" + bsp.getLife() + " §c\u2764");
		    	}
		    }
		}
		lines.add(" ");
		lines.add("§c§lJEU");
		lines.add(" §fMode §3» §bSolo");
		lines.add(" §fMap §3» §b" + g.getPlayedMap().getName());
		lines.add(" ");
		lines.add("§b[§fcraftok.fr§c]");
		sb.updateLines(lines);
	}
	
	public void updateLines(List<String> lines) {
		sb.updateTitle("§c§lBLOCKSUMO");
		sb.updateLines(lines);
	}
	
	public void updateActionBar() {
		PlayerUtils utils = new PlayerUtils(getPlayer());
		if(life >= 1) {
			if(TimerManager.isFinished("deathmatch")) {
				utils.sendActionBar("§f" + this.life + " §c\u2764 §7restante(s) | §fDeathMatch §3» §b§lActiver");
			}else {
				utils.sendActionBar("§f" + this.life + " §c\u2764 §7restante(s) | §fDeathMatch §3» §b"+TimerManager.getTimeBeforeFinish("deathmatch"));
			}
			return;
		}else {
			utils.sendActionBar("§7SPECTATEUR §f| §cVous êtes mort !");
		}
	}
	
	public boolean isInvincibility() {
		return invincibility;
	}
	
	public String getName() {
		return name;
	}
	
	public int getLife() {
		return life;
	}
	
	public String getLastDamager() {
		return lastdamager;
	}
	
	public void setLastDamager(String lastdamager) {
		this.lastdamager = lastdamager;
	}
	
	public void kill() {
		life--;
		if(life == 0) {
			setSpectator(true);
			Player p = getPlayer();
			p.setGameMode(GameMode.SPECTATOR);
			p.teleport(BlockSumo.getInstance().getGameManager().getPlayedMap().getBonus());
			PlayerInventory inventory = p.getInventory();
			inventory.clear();
			inventory.setArmorContents(null);
			if(lastdamager != null) {
				Player damager = Bukkit.getPlayer(lastdamager);
				if(damager != null) {
					damager.sendMessage("§c§lCRAFTOK §8» §c§l+2 §7coins pour avoir tuer §c"+name);
					CoreCommon.getCommon().getUserManager().getUserByUniqueId(damager.getUniqueId()).addCoins(2);
					setLastDamager(null);
				}
			}
			Bukkit.broadcastMessage("§c§lCRAFTOK §8» §c§l"+name+" §7est §limin§(e) !");
			new PlayerUtils(p).sendTitle(10, 20, 10, "§c§lVous avez", "§c§lPERDU");
			BlockSumo.getInstance().getGameManager().checkWin();
			BSPlayerManager.updateSB();
			return;
		}else {
			Player p = getPlayer();
			BlockSumo.getInstance().getGameManager().getPlayedMap().teleport(p);
			for (PotionEffect effect : p.getActivePotionEffects()) {
				p.removePotionEffect(effect.getType());
			}
			invincibility = true;
			for (int i = 0; i < 35; i++){
				ItemStack item = p.getInventory().getItem(i);
				if(item == null) continue;
				if(!(item.getType() == Material.WOOL) && !(item.getType() == Material.SHEARS)) {
					p.getInventory().setItem(i, new ItemStack(Material.AIR));
				}
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(BlockSumo.getInstance(), () -> {
				invincibility = false;
			}, 60L);
			if(lastdamager == null) {
				Bukkit.broadcastMessage("§c§lCRAFTOK §8» §c§l"+name+" §7est mort, il lui reste §c§l"+life+" §7vie(s) !");
			}else {
				Bukkit.broadcastMessage("§c§lCRAFTOK §8» §c§l"+name+" §7a été tuer par §c"+lastdamager+"§7, il lui reste §c§l"+life+" §7vie(s) !");
				
				Player damager = Bukkit.getPlayer(lastdamager);
				if(damager != null) {
					damager.sendMessage("§c§lCRAFTOK §8» §c§l+2 §7coins pour avoir tuer §c"+name);
					CoreCommon.getCommon().getUserManager().getUserByUniqueId(damager.getUniqueId()).addCoins(2);
					setLastDamager(null);
				}
			}
			BSPlayerManager.updateSB();
		}
	}
}
