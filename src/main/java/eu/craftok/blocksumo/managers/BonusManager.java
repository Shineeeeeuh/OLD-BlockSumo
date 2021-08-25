package eu.craftok.blocksumo.managers;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import eu.craftok.utils.ItemCreator;

public class BonusManager {
	
	private ArrayList<ItemStack> items;
	
	public BonusManager() {
		items = new ArrayList<>();
		items.add(new ItemStack(Material.TNT));
		items.add(new ItemStack(Material.POTION, 1, (short) 8235));
		items.add(new ItemStack(Material.SNOW_BALL, 5));
		items.add(new ItemStack(Material.ENDER_PEARL));
		items.add(new ItemCreator(Material.IRON_SWORD).addEnchantment(Enchantment.KNOCKBACK, 1).setDurability(Material.IRON_SWORD.getMaxDurability()).getItemstack());
	}
	
	public ItemStack getRandomItem() {
		int randomIndex = new Random().nextInt(items.size());
		return items.get(randomIndex);
	}

}
