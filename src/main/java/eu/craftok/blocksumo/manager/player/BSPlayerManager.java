package eu.craftok.blocksumo.manager.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.manager.GameManager;

public class BSPlayerManager {
	
	private static int playerssize = 0;
	private static HashMap<String, BSPlayer> players = new HashMap<>();
	
	public static void addPlayer(BSPlayer bp) {
		players.put(bp.getName(), bp);
		if(!bp.isSpectator()) {
			playerssize++;
		}
	}
	
	public static void removePlayer(String name) {
		BSPlayer bp = getPlayer(name);
		if(!bp.isSpectator()) {
			playerssize--;
		}
		players.remove(name);
	}
	
	public static int getPlayersNB() {
		return playerssize;
	}
	
	public static BSPlayer getPlayer(String name) {
		return players.get(name);
	}
	
	public static List<BSPlayer> getAlivePlayers(){
		return players.values().stream().filter(b -> !b.isSpectator()).collect(Collectors.toList());
	}
	
	public static List<BSPlayer> getDeadPlayers(){
		return players.values().stream().filter(b -> b.isSpectator()).collect(Collectors.toList());
	}
	
	public static ArrayList<BSPlayer> getAllPlayers(){
		return new ArrayList<BSPlayer>(players.values());
	}
	
	public static void updateSB() {
		GameManager g = BlockSumo.getInstance().getGameManager();
		List<String> lines = new ArrayList<>();
		lines.add(" ");
		lines.add("§c§lPARTIE");
		if (g.getState() == GameManager.STATE.LOBBY) {
			lines.add(" §fJoueurs §3» §b" + BSPlayerManager.getPlayersNB() + "/8");
		    lines.add(" §fStatus §3» §bAttente");
		}
		else {
			lines.add(" §fJoueurs §3» §b" + BSPlayerManager.getAlivePlayers().size() + "/8");
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
		players.values().forEach(b -> b.updateLines(lines));
	}
	
	public static void updateActionBar() {
		players.values().forEach(b -> b.updateActionBar());
	}
}
