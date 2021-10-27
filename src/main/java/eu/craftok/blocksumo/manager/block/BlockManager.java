package eu.craftok.blocksumo.manager.block;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import eu.craftok.blocksumo.manager.player.BSPlayer;
import eu.craftok.blocksumo.manager.player.BSPlayerManager;

public class BlockManager {
	
	private static HashMap<Blocks, String> blockowner = new HashMap<>();
	
	public static void assignBlockToPlayer(Player p, Blocks bl) {
		BSPlayer b = BSPlayerManager.getPlayer(p.getName());
		b.setBlock(bl);
		blockowner.put(bl, p.getName());
	}
	
	public static void assignRandomBlock(List<BSPlayer> list) {
		List<Blocks> blocks = Lists.newArrayList(Blocks.values()).stream().filter(b -> !blockowner.containsKey(b)).collect(Collectors.toList());
		list.forEach(b -> {
			int randomindex = new Random().nextInt(blocks.size());
			b.setBlock(blocks.get(randomindex));
			blocks.remove(randomindex);
		});
	}
	
	public static boolean isBlockAssigned(Blocks b) {
		return blockowner.containsKey(b);
	}
	
	public static String getPlayerBlock(Blocks b) {
		return blockowner.get(b);
	}
	

}
