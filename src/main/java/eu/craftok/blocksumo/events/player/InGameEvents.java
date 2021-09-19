package eu.craftok.blocksumo.events.player;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.enums.GameState;
import eu.craftok.blocksumo.game.Game;
import eu.craftok.blocksumo.guis.GameGui;
import eu.craftok.blocksumo.managers.GameManager;
import eu.craftok.blocksumo.player.BSPlayer;
import eu.craftok.utils.PlayerUtils;
import eu.craftok.utils.inventory.CustomInventory;

public class InGameEvents implements Listener{
	
	private GameManager gamemanager;
	private BlockSumo instance;
	
	public InGameEvents(BlockSumo instance) {
		this.gamemanager = instance.getGameManager();
		this.instance = instance;
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity().getType() != EntityType.PLAYER) return;
		if(instance.isVanished((Player) e.getEntity())) {
			e.setCancelled(true);
			return;
		}
		Game g = gamemanager.getGameByPlayer((Player) e.getEntity());
		if(g == null) return;
		if(g.getState() == GameState.INGAME) {
			if(e.getCause() == DamageCause.VOID) {
				e.setCancelled(true);
				BSPlayer bsp = g.getPlayer(e.getEntity().getName());
				bsp.kill();
			}
			e.setDamage(0D);
		}else {
			return;
		}
	}
	
	@EventHandler
	public void onDamageByPlayer(EntityDamageByEntityEvent e) {
		if(instance.isVanished((Player) e.getEntity())) {
			e.setCancelled(true);
			return;
		}
		Game g = instance.getGameManager().getGameByPlayer((Player) e.getEntity());
		if(g.getPlayer(e.getEntity().getName()).isInvicibility()) {
			e.setCancelled(true);
			return;
		}
		if(g.getState() != GameState.INGAME) {
			return;
		}
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			Player v = (Player) e.getDamager();
			p.setVelocity(v.getLocation().getDirection().multiply(0.42).setY(0));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBuild(BlockPlaceEvent e) {
		if(e.getBlock().getType() == Material.WOOL) {
			e.getPlayer().setItemInHand(new ItemStack(Material.WOOL, 64, (byte) new Random().nextInt(15)));
		}
		if(e.getBlock().getLocation().getY() >= 30) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		ItemStack it = e.getItem();
		if(it == null) return;
		Action act = e.getAction();
		if(it.getType() == Material.FIREBALL) {
			e.setCancelled(true);
			if(act != Action.RIGHT_CLICK_AIR && act != Action.RIGHT_CLICK_BLOCK) return;
			spawnFireBall(e.getPlayer());
			if(e.getPlayer().getItemInHand().getAmount()-1 == 0) {
				e.getPlayer().setItemInHand(null);
			}else {
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
			}
			return;
		}
		if(it.getType() == Material.COMPASS) {
			if(act != Action.RIGHT_CLICK_AIR && act != Action.RIGHT_CLICK_BLOCK) return;
			CustomInventory cs = new GameGui(e.getPlayer(), instance);
			cs.openMenu();
			return;
		}
		if(it.getType() == Material.FEATHER) {
			if(act != Action.RIGHT_CLICK_AIR && act != Action.RIGHT_CLICK_BLOCK) return;
			e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(1.5).setY(1.25));
			new PlayerUtils(e.getPlayer()).sendSound(Sound.FIREWORK_LAUNCH, 1F);
			e.getPlayer().getWorld().playEffect(e.getPlayer().getLocation(), Effect.SMOKE, 5);
			e.getPlayer().getWorld().strikeLightningEffect(e.getPlayer().getLocation());
			if(e.getPlayer().getItemInHand().getAmount()-1 == 0) {
				e.getPlayer().setItemInHand(null);
			}else {
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
			}
			return;
		}
		return;
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		if(e.getEntityType() == EntityType.PRIMED_TNT || e.getEntityType() == EntityType.FIREBALL) {
			Game g = instance.getGameManager().getGameByWorld(e.getLocation().getWorld().getName());
			List<Block> d = e.blockList();
			Iterator<Block> it = d.iterator();
			while (it.hasNext()) {
				Block b = (Block) it.next();
				if(!(b.getType() == Material.WOOL)) {
					it.remove();
				}else {
					if(g.getBlocksPlaced().containsKey(b.getLocation())) {
						g.getBlocksPlaced().remove(b.getLocation());
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(e.getBlock().getType() == Material.TNT) {
			e.getBlock().setType(Material.AIR);
			e.getBlock().getLocation().getWorld().spawnEntity(e.getBlock().getLocation().add(0.5, 0, 0.5), EntityType.PRIMED_TNT);
			if(e.getPlayer().getItemInHand().getAmount()-1 == 0) {
				e.getPlayer().setItemInHand(null);
			}else {
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
			}
			return;
		}
	}
	
	@EventHandler
    public void onDamageByOtherEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof TNTPrimed && e.getEntity() instanceof Player) {
			TNTPrimed tnt = (TNTPrimed) e.getDamager();
			Player player = (Player) e.getEntity();
			 for (Entity entity : tnt.getNearbyEntities(1.5, 1.5, 1.5)) {
				 if(entity instanceof Player) {
					 Player p = (Player) entity;
					 if(!p.equals(player)) {
						 continue;
					 }
					 player.setVelocity(player.getLocation().getDirection().multiply(0.7).setY(0.7));
				 }
			 }
		}
	}
	
	@EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType().equals(Material.POTION)) {
        	Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(instance, new Runnable() {
                @Override
                public void run() {
                	for (int i = 0; i < 9; i++){
        				ItemStack item = e.getPlayer().getInventory().getItem(i);
        				if(item == null) continue;
        				if(item.getType() == Material.GLASS_BOTTLE) {
        					e.getPlayer().getInventory().setItem(i, new ItemStack(Material.AIR));
        				}
        			}
                }
            }, 1L);
        }
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		e.setCancelled(true);
	}
	
	//Author: Sithey
	public void spawnFireBall(Player p) {
		Location e = p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(1.2));
		Fireball fb = (Fireball) p.getWorld().spawnEntity(e, EntityType.FIREBALL);
		fb.setVelocity(fb.getDirection().normalize().multiply(2));
		fb.setShooter(p);
		fb.setIsIncendiary(false);
		fb.setYield(5F);
	}
	
	@EventHandler
	public void onPickUPItem(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		ItemStack it = e.getItem().getItemStack();
		if(it.getType() == Material.FEATHER) {
			for (int i = 0; i < 35; i++){
				ItemStack item = p.getInventory().getItem(i);
				if(item == null) continue;
				if(item.getType() == Material.FEATHER) {
					p.sendMessage("§cVous avez déjà un boost de double jump ! Utiliser le pour en reutiliser un !");
					e.setCancelled(true);
					return;
				}
			}
			return;
		}
		return;
	}
	
}
