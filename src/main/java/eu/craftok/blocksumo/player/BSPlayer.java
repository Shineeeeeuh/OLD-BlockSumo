package eu.craftok.blocksumo.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.enums.GameState;
import eu.craftok.blocksumo.scoreboard.ScoreboardBuilder;
import eu.craftok.utils.ItemCreator;
import eu.craftok.utils.PlayerUtils;

public class BSPlayer {
	private int life;
	private String playername;
	private boolean isSpectator;
	private BlockSumo instance;
	private ScoreboardBuilder sb;
	
	public BSPlayer(String player, BlockSumo instance) {
		this.playername = player;
		this.life = 5;
		this.isSpectator = false;
		this.instance = instance;
	}
	
	public BSPlayer(String player, boolean isSpectator, BlockSumo instance) {
		this.isSpectator = isSpectator;
		this.life = 0;
		this.playername = player;
		this.instance = instance;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(playername);
	}
	
	public boolean isSpectator() {
		return isSpectator;
	}
	
	public int getLife() {
		return life;
	}
	
	public String getPlayerName() {
		return playername;
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
	
	public void loadWaitingItems() {
		Player p = getPlayer();
		PlayerInventory inventory = p.getInventory();
		inventory.setArmorContents(null);
		inventory.clear();
		inventory.setHeldItemSlot(4);
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
	
	public void kill() {
		PlayerUtils utils = new PlayerUtils(getPlayer());
		this.life = life-1;
		if(life >= 1) {
			Bukkit.broadcastMessage("§c§lCRAFTOK §8» §c§l"+playername+" §7est mort, il lui reste §c§l"+life+" §7vie(s) !");
			instance.getGameManager().getPlayedMap().teleport(getPlayer());
			instance.getPlayerManager().updateSB();
			for (PotionEffect effect : getPlayer().getActivePotionEffects()) {
				getPlayer().removePotionEffect(effect.getType());
			}
			utils.sendActionBar("§f"+life+" §c\u2764 §7restante(s).");
			for (int i = 0; i < 35; i++){
				ItemStack item = getPlayer().getInventory().getItem(i);
				if(!(item.getType() == Material.WOOL) && !(item.getType() == Material.SHEARS)) {
					getPlayer().getInventory().setItem(i, new ItemStack(Material.AIR));
				}
			}
		}
		if(life == 0) {
			loadSpectator();
			instance.getPlayerManager().updateSB();
			utils.sendTitle(10, 20, 10, "§c§lVous avez", "§c§lPERDU");
			instance.getGameManager().checkWin();
			utils.sendActionBar("§cVous êtes mort !");
			Bukkit.broadcastMessage("§c§lCRAFTOK §8» §c§l"+playername+" §7est éliminé(e) !");
			return;
		}
	}
	
	public void loadSpectator() {
		isSpectator = true;
		Player p = getPlayer();
		PlayerInventory inventory = p.getInventory();
		inventory.clear();
		inventory.setArmorContents(null);
		p.teleport(new Location(Bukkit.getWorld(instance.getGameManager().getPlayedMap().getWorld()), 0.5, 50.5, 0.5));
		p.setGameMode(GameMode.SPECTATOR);
	}
	
	public void loadScoreboard() {
		sb = new ScoreboardBuilder(getPlayer());
		List<String> lines = new ArrayList<>();
		sb.updateTitle("§c§lBLOCKSUMO");
		lines.add(" ");
		lines.add("§c§lPARTIE");
		lines.add(" §fJoueurs §3» §b" + this.instance.getPlayerManager().getAlivePlayers().size() + "/8");
		if (instance.getGameManager().getState() == GameState.WAITING) {
		    lines.add(" §fStatus §3» §bAttente");
		}
		else {
		    lines.add(" §fStatus §3» §bEn jeu");
		    lines.add(" ");
		    for (BSPlayer bsp : instance.getPlayerManager().getAlivePlayers()) {
		        lines.add("§f" + bsp.getPlayerName() + " §3» §b§l" + bsp.getLife() + " §c\u2764");
		    }
		}
		lines.add(" ");
		lines.add("§c§lJEU");
		lines.add(" §fMode §3» §bSolo");
		lines.add(" §fMap §3» §b" + instance.getGameManager().getPlayedMap().getWorld());
		lines.add(" ");
		lines.add("§b[§fcraftok.fr§c]");
		sb.updateLines(lines);
	}
	
	
	public void updateActionBar() {
        PlayerUtils utils = new PlayerUtils(getPlayer());
        if (this.life >= 1) {
            utils.sendActionBar("§f" + this.life + " §c\u2764 §7restante(s).");
            return;
        }
        utils.sendActionBar("§cVous êtes mort !");
    }
	
	public void updateSB(List<String> lines) {
		sb.updateTitle("§c§lBLOCKSUMO");
		sb.updateLines(lines);
	}
}
