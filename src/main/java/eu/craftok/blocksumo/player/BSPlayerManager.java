package eu.craftok.blocksumo.player;

import java.util.ArrayList;
import java.util.List;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.enums.GameState;

public class BSPlayerManager {
	private ArrayList<BSPlayer> players = new ArrayList<>();
	private BlockSumo instance;
	
	public BSPlayerManager(BlockSumo instance) {
		this.instance = instance;
	}
	
	public ArrayList<BSPlayer> getPlayers() {
		return players;
	}
	
	public void addPlayers(BSPlayer player) {
		players.add(player);
	}
	
	public void removePlayers(BSPlayer player) {
		players.remove(player);
	}
	
	public BSPlayer getPlayer(String name) {
		for(BSPlayer p : players) {
			if(p.getPlayerName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}
	
	public ArrayList<BSPlayer> getAlivePlayers(){
		ArrayList<BSPlayer> aliveplayers = new ArrayList<>();
		for(BSPlayer bsp : players) {
			if(bsp == null) continue;
			if(!bsp.isSpectator()) {
				aliveplayers.add(bsp);
			}
		}
		return aliveplayers;
	}
	
	public ArrayList<BSPlayer> getDeadPlayers(){
		ArrayList<BSPlayer> deadplayers = new ArrayList<>();
		for(BSPlayer bsp : players) {
			if(bsp.isSpectator()) {
				deadplayers.add(bsp);
			}
		}
		return deadplayers;
	}
	
	public void updateSB() {
		List<String> lines = new ArrayList<>();
		lines.add(" ");
		lines.add("§c§lPARTIE");
		lines.add(" §fJoueurs §3» §b" + this.instance.getPlayerManager().getAlivePlayers().size() + "/8");
		if (instance.getGameManager().getState() == GameState.WAITING) {
		    lines.add(" §fStatus §3» §bAttente");
		}
		else {
		    lines.add(" §fStatus §3» §bEn jeu");
		    lines.add(" ");
		    for (BSPlayer bsp : getAlivePlayers()) {
		    	if(bsp.getPlayerName().length() >= 13) {
		    		lines.add("§f" + bsp.getPlayerName().substring(0, 13) + " §3» §b§l" + bsp.getLife() + " §c\u2764");
		    	}else {
		    		lines.add("§f" + bsp.getPlayerName() + " §3» §b§l" + bsp.getLife() + " §c\u2764");
		    	}
		    }
		}
		lines.add(" ");
		lines.add("§c§lJEU");
		lines.add(" §fMode §3» §bSolo");
		lines.add(" §fMap §3» §b" + instance.getGameManager().getPlayedMap().getWorld());
		lines.add(" ");
		lines.add("§b[§fcraftok.fr§c]");
		for(BSPlayer bsp : getPlayers()) {
			bsp.updateSB(lines);
		}
	}
}