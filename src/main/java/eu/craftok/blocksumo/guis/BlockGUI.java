package eu.craftok.blocksumo.guis;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.google.common.collect.Lists;

import eu.craftok.blocksumo.manager.block.BlockManager;
import eu.craftok.blocksumo.manager.block.Blocks;
import eu.craftok.utils.ItemCreator;
import eu.craftok.utils.inventory.CustomInventory;
import eu.craftok.utils.inventory.item.ActionItem;
import eu.craftok.utils.inventory.item.StaticItem;

public class BlockGUI extends CustomInventory{
	private int slot;

	public BlockGUI(Player p) {
		super(p, "§6Block §f- §eChoix", 2, 0);
	}

	@Override
	public void setupMenu() {
		slot = 0;
		Lists.newArrayList(Blocks.values()).forEach(b -> {
			if(BlockManager.isBlockAssigned(b)) {
				addItem(new StaticItem(slot, new ItemCreator(Material.WOOL).setName(b.getName()+" §c(Pris)").setDurability((short) b.getDamage()).addLore("- §6"+BlockManager.getPlayerBlock(b)).getItemstack()));
				slot++;
				return;
			}else {
				addActionItem(new ActionItem(slot, new ItemCreator(Material.WOOL).setName(b.getName()).setDurability((short) b.getDamage()).getItemstack()) {
					
					@Override
					public void onClick(InventoryClickEvent e) {
						if(BlockManager.isBlockAssigned(b)) {
							refresh();
							return;
						}else {
							Player p = (Player) e.getWhoClicked();
							BlockManager.assignBlockToPlayer(p, b);
							p.sendMessage("§c§lCraftok §8» §6Tu as choisis le block "+b.getName()+" §6!");
							p.closeInventory();
						}
						
					}
				});
				slot++;
			}
		});
	}

}
