package eu.craftok.blocksumo.manager.bonus;

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
	private static ArrayList<ItemStack> normalitems = new ArrayList<>();
	private static ArrayList<ItemStack> megaitems = new ArrayList<>();
	
	public static void registerItems() {
		normalitems.add(new ItemStack(Material.TNT));
		normalitems.add(getCustomPotion(PotionEffectType.JUMP, 300, 1, "§ePotion de §6Jump"));
		normalitems.add(getCustomPotion(PotionEffectType.SPEED, 300, 1, "§ePotion de §6Speed"));
		normalitems.add(new ItemStack(Material.SNOW_BALL, 5));
		normalitems.add(new ItemStack(Material.ENDER_PEARL));
		megaitems.add(new ItemCreator(Material.IRON_SWORD).addEnchantment(Enchantment.KNOCKBACK, 1).setDurability(Material.IRON_SWORD.getMaxDurability()).getItemstack());
		megaitems.add(new ItemCreator(Material.FIREBALL).setName("§6Fire§eBall").getItemstack());
		megaitems.add(new ItemCreator(Material.FEATHER).setName("§6§lDouble Jump").setGlow(true).getItemstack());
	}
	
	public static ItemStack getNormalRandomItem() {
		int randomIndex = new Random().nextInt(normalitems.size());
		return normalitems.get(randomIndex);
	}
	
	public static ItemStack getMegaRandomItems() {
		int randomIndex = new Random().nextInt(normalitems.size());
		return normalitems.get(randomIndex);
	}
	
	private static ItemStack getCustomPotion(PotionEffectType type, int dura, int amp, String name) {
		ItemStack it = new ItemStack(Material.POTION);
		PotionMeta pm = (PotionMeta) it.getItemMeta();
		pm.addCustomEffect(new PotionEffect(type, dura, amp), true);
		pm.setDisplayName(name);
		it.setItemMeta(pm);
		return it;
	}
}
