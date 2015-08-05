package org.snowyegret.mojo.player;

import java.awt.Font;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.pick.PickManager;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.undo.TransactionManager;
import org.snowyegret.mojo.util.StringUtils;

public class PlayerProperties implements IExtendedEntityProperties {

	private Player player;
	public final static String NAME = "PlayerProperties";
	private static final String KEY_FONT = "font";

	private Modifiers modifiers;
	private SelectionManager selectionManager;
	private PickManager pickManager;
	private TransactionManager transactionManager;
	private Clipboard clipboard;
	private Spell lastSpell;
	private Spell lastInvokedSpell;
	private String blockSavedPath;
	private Font font;

	public PlayerProperties(EntityPlayer entity) {
		player = new Player(entity);
	}

	public Modifiers getModifiers() {
		return modifiers;
	}

	public TransactionManager getUndoManager() {
		return transactionManager;
	}

	public SelectionManager getSelectionManager() {
		return selectionManager;
	}

	public PickManager getPickManager() {
		return pickManager;
	}

	@Override
	public void saveNBTData(NBTTagCompound tag) {
		if (font != null) {
			tag.setString(KEY_FONT, StringUtils.encodeFont(font));
		}
	}

	@Override
	public void loadNBTData(NBTTagCompound tag) {
		String fontString = tag.getString(KEY_FONT);
		if (fontString != null) {
			font = Font.decode(fontString);
		}
		//System.out.println("tag=" + tag);
	}

	@Override
	public void init(Entity entity, World world) {
		modifiers = new Modifiers();
		selectionManager = new SelectionManager(player, MoJo.blockSelected);
		pickManager = new PickManager(player, MoJo.blockPicked);
		transactionManager = new TransactionManager(player);
		clipboard = new Clipboard();
		int fontSize = 24;
		String fontName = "Arial";
		int fontStyle = Font.PLAIN;
		font = new Font(fontName, fontStyle, fontSize);
	}

	public void setLastSpell(Spell spell) {
		this.lastSpell = spell;
	}

	public Spell getLastSpell() {
		return lastSpell;
	}

	public void setLastInvokedSpell(Spell spell) {
		this.lastInvokedSpell = spell;
	}

	public Spell getLastInvokedSpell() {
		return lastInvokedSpell;
	}

	public Clipboard getClipboard() {
		return clipboard;
	}

	public void setBlockSavedPath(String blockSavedPath) {
		this.blockSavedPath = blockSavedPath;
	}

	public String getBlockSavedPath() {
		return blockSavedPath;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Font getFont() {
		return font;
	}

}
