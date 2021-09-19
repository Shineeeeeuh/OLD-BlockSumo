package eu.craftok.blocksumo.guis;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.craftok.blocksumo.BlockSumo;
import eu.craftok.blocksumo.enums.GameState;
import eu.craftok.blocksumo.game.Game;
import eu.craftok.utils.ItemCreator;
import eu.craftok.utils.inventory.CustomInventory;
import eu.craftok.utils.inventory.item.ActionItem;

public class GameGui extends CustomInventory{
	
	private BlockSumo instance;

	public GameGui(Player p, BlockSumo instance) {
		super(p, "§e- §6Parties §e-", 1, 0);
		this.instance = instance;
	}

	@Override
	public void setupMenu() {
		int slot = 0;
		for(Game g : instance.getGameManager().getGames().values()) {
			ItemStack it = new ItemCreator(getStateItem(g.getState())).setName("§6Parties : §e"+g.getID()).addLore("§eJoueurs: §6"+g.getPlayers().size()).getItemstack();
			addActionItem(new ActionItem(slot, it) {
				
				@Override
				public void onClick(InventoryClickEvent e) {
					player.closeInventory();
					player.getInventory().clear();
					player.teleport(g.getBonusLoc().add(0,2,0));
				}
			});
			slot++;
		}
	}
	
	public ItemStack getStateItem(GameState g) {
		if(g == GameState.FINISH) {
			return new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);
		}
		if(g == GameState.TIMER || g == GameState.INGAME) {
			return new ItemStack(Material.STAINED_CLAY,1, (byte) 1);
		}else {
			return new ItemStack(Material.STAINED_CLAY, 1, (byte) 13);
		}
		
	}

}
