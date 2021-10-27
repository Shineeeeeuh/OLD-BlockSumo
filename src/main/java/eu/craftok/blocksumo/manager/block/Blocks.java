package eu.craftok.blocksumo.manager.block;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Blocks {
	WHITE("§fBlanc", (byte) 0),
	ORANGE("§6Orange", (byte) 1),
	MAGENTA("§dMagenta", (byte) 2),
	BLUE_C("§aBleu", (byte) 3),
	YELLOW("§eJaune", (byte) 4),
	GREEN_C("§aVert", (byte) 5),
	PINK("§dRose", (byte) 6),
	GRAY("§8Gris", (byte) 7),
	GRAY_C("§7Gris", (byte) 8),
	CYAN("§3Cyan", (byte) 9),
	PURPLE("§5Violet", (byte) 10),
	BLUE("§9Bleu", (byte) 11),
	BROWN("§6Marron", (byte) 12),
	GREEN("§2Vert", (byte) 13),
	RED("§cRouge", (byte) 14),
	BLACK("§0Noir", (byte) 15);
	
	private byte d;
	private String name;
	
	private Blocks(String name, byte d) {
		this.name = name;
		this.d = d;
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack toItem() {
		return new ItemStack(Material.WOOL, 64, d);
	}
	
	public byte getDamage() {
		return d;
	}
}
