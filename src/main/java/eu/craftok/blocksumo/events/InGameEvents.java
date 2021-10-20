package eu.craftok.blocksumo.events;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.manager.GameManager;
import eu.craftok.blocksumo.manager.GameManager.STATE;
import eu.craftok.blocksumo.manager.player.BSPlayer;
import eu.craftok.blocksumo.manager.player.BSPlayerManager;
import eu.craftok.blocksumo.manager.timers.TimerManager;
import eu.craftok.utils.PlayerUtils;
import eu.craftok.utils.particles.ParticleUtils;
import eu.craftok.utils.particles.Particles;

public class InGameEvents implements Listener {
	
	public static int y = 0;
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(BlockSumo.getInstance().getGameManager().getState() != STATE.INGAME) return;
		if(e.getEntity() instanceof Player) {
			
			Player p = (Player) e.getEntity();
			BSPlayer b = BSPlayerManager.getPlayer(p.getName());
			
			//TODO : KILL DETECT
			
			if(e.getCause() == DamageCause.VOID || (e.getCause() == DamageCause.SUFFOCATION && TimerManager.isFinished("deathmatch"))) {
				e.setCancelled(true);
				b.kill();
			}
			
			//TODO: DEL VIE
			
			e.setDamage(0D);
		}else {
			return;
		}
	}
	
	@EventHandler
	public void onPlaceTooHigh(BlockPlaceEvent e) {
		if(e.getBlock().getLocation().getY() > y) e.setCancelled(true);
		return;
	}
	
	@EventHandler
	public void onPvP(EntityDamageByEntityEvent e) {
		if(BlockSumo.getInstance().getGameManager().getState() != STATE.INGAME) return;
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			BSPlayer b = BSPlayerManager.getPlayer(p.getName());
			if(b.isInvincibility()) {
				e.setCancelled(true);
				return;
			}
			if(e.getDamager() instanceof Player) {
				Player d = (Player) e.getDamager();
				p.setVelocity(d.getLocation().getDirection().multiply(0.42).setY(0));
				b.setLastDamager(d.getName());
			}
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
		if(it.getType() == Material.FEATHER) {
			if(act != Action.RIGHT_CLICK_AIR && act != Action.RIGHT_CLICK_BLOCK) return;
			e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(1.5).setY(1.25));
			new PlayerUtils(e.getPlayer()).sendSound(Sound.FIREWORK_LAUNCH, 1F);
			ParticleUtils.drawParticleLine(e.getPlayer().getLocation().add(2, 0, 0), e.getPlayer().getLocation().subtract(2, 0, 2), Particles.FLAME, 15, Color.WHITE);
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
    public void onConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType().equals(Material.POTION)) {
        	Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(BlockSumo.getInstance(), new Runnable() {
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
	public void onExplode(EntityExplodeEvent e) {
		if(e.getEntityType() == EntityType.ENDER_DRAGON) {
			e.blockList().clear();
			return;
		}
		if(e.getEntityType() == EntityType.PRIMED_TNT || e.getEntityType() == EntityType.FIREBALL) {
			GameManager g = BlockSumo.getInstance().getGameManager();
			List<Block> d = e.blockList();
			Iterator<Block> it = d.iterator();
			while (it.hasNext()) {
				Block b = (Block) it.next();
				if(!(b.getType() == Material.WOOL)) {
					it.remove();
				}else {
					if(g.getBlocks().containsKey(b.getLocation())) {
						g.getBlocks().remove(b.getLocation());
					}
				}
			}
		}
	}
	
	
}
