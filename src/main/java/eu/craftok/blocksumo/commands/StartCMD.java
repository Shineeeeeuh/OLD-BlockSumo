package eu.craftok.blocksumo.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.events.player.LobbyPlayerEvents;
import eu.craftok.blocksumo.game.Game;

public class StartCMD implements CommandExecutor {
	
	private BlockSumo instance;
	
	public StartCMD(BlockSumo instance) {
		this.instance = instance;
	}

	public boolean onCommand(CommandSender s, Command cmd, String str, String[] args) {
		if(s.hasPermission("adminblocksumo.startgame")) {
			Player p = (Player) s;
			Game g = instance.getGameManager().getGameByWorld(p.getLocation().getWorld().getName());
			LobbyPlayerEvents.getStartTaskID().get(g.getID()).setTimer(5);
			return true;
		}else {
			s.sendMessage("§cVous n'avez pas la permission d'executer cette commande !");
		}
		return false;
	}

}
