package eu.craftok.blocksumo.managers;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.craftok.utils.ItemCreator;

public class BonusManager {
	
	private ArrayList<ItemStack> items;
	
	public BonusManager() {
		items = new ArrayList<>();
		items.add(new ItemStack(Material.TNT));
		items.add(getCustomPotion(PotionEffectType.JUMP, 300, 1, "§ePotion de §6Jump"));
		items.add(getCustomPotion(PotionEffectType.SPEED, 300, 1, "§ePotion de §6Speed"));
		items.add(new ItemStack(Material.SNOW_BALL, 5));
		items.add(new ItemStack(Material.ENDER_PEARL));
		items.add(new ItemCreator(Material.IRON_SWORD).addEnchantment(Enchantment.KNOCKBACK, 1).setDurability(Material.IRON_SWORD.getMaxDurability()).getItemstack());
	}
	
	public ItemStack getRandomItem() {
		int randomIndex = new Random().nextInt(items.size());
		return items.get(randomIndex);
	}
	
	private ItemStack getCustomPotion(PotionEffectType type, int dura, int amp, String name) {
		ItemStack it = new ItemStack(Material.POTION);
		PotionMeta pm = (PotionMeta) it.getItemMeta();
		pm.addCustomEffect(new PotionEffect(type, dura, amp), true);
		pm.setDisplayName(name);
		it.setItemMeta(pm);
		return it;
	}

}
