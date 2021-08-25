package eu.craftok.blocksumo.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import eu.craftok.blocksumo.tasks.StartTask;

public class StartCMD implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command cmd, String str, String[] args) {
		if(s.hasPermission("adminblocksumo.startgame")) {
			StartTask.setTimer(5);
			return true;
		}else {
			s.sendMessage("§cVous n'avez pas la permission d'executer cette commande !");
		}
		return false;
	}

}
