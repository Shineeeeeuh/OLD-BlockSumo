package eu.craftok.blocksumo.tasks;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.enums.GameState;
import eu.craftok.blocksumo.game.Game;
import eu.craftok.blocksumo.player.BSPlayer;
import eu.craftok.utils.PlayerUtils;

public class BonusTask extends BukkitRunnable{

	private BlockSumo instance;
	private int gameid;
	
	public BonusTask(BlockSumo instance, Game g) {
		this.instance = instance;
		this.gameid = g.getID();
	}
	
	
	@Override
	public void run() {
		Game g = instance.getGameManager().getGameByID(gameid);
		if(g == null) {
			cancel();
			return;
		}
		if(g.getState() == GameState.FINISH) {
			cancel();
			return;
		}
        Location l = g.getBonusLoc();
        l.getWorld().dropItem(l, this.instance.getBonusManager().getRandomItem());
        g.broadcastMessage("§c§lCRAFTOK §8» §7Un bonus vient d'apparaître au millieu de la map !");
        for (BSPlayer bsp : g.getPlayers()) {
            new PlayerUtils(bsp.getPlayer()).sendSound(Sound.LEVEL_UP, 1.0f);
        }
	}

}
