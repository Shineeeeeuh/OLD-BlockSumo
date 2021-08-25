package eu.craftok.blocksumo.events.player;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.enums.GameState;
import eu.craftok.blocksumo.managers.GameManager;
import eu.craftok.blocksumo.player.BSPlayer;
import eu.craftok.blocksumo.player.BSPlayerManager;

public class InGameEvents implements Listener{
	
	private GameManager gamemanager;
	private BSPlayerManager playermanager;
	
	public InGameEvents(BlockSumo instance) {
		this.gamemanager = instance.getGameManager();
		this.playermanager = instance.getPlayerManager();
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(gamemanager.getState() == GameState.INGAME) {
			if(e.getCause() == DamageCause.VOID) {
				e.setCancelled(true);
				BSPlayer bsp = playermanager.getPlayer(e.getEntity().getName());
				bsp.kill();
			}
			e.setDamage(0D);
		}else {
			return;
		}
	}
	
	@EventHandler
	public void onDamageByPlayer(EntityDamageByEntityEvent e) {
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
	public void onExplode(EntityExplodeEvent e) {
		if(e.getEntityType() == EntityType.PRIMED_TNT) {
			List<Block> d = e.blockList();
			Iterator<Block> it = d.iterator();
			while (it.hasNext()) {
				Block b = (Block) it.next();
				if(!(b.getType() == Material.WOOL)) {
					it.remove();
				}else {
					if(gamemanager.getBlocksPlaced().containsKey(b.getLocation())) {
						Bukkit.getScheduler().cancelTask(gamemanager.getBlocksPlaced().get(b.getLocation()));
						gamemanager.getBlocksPlaced().remove(b.getLocation());
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(e.getBlock().getType() == Material.TNT) {
			e.getBlock().setType(Material.AIR);
			e.getBlock().getLocation().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.PRIMED_TNT);
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
	

}
