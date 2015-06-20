package org.snowyegret.plato.player;

import org.snowyegret.plato.Plato;
import org.snowyegret.plato.item.spell.ISpell;
import org.snowyegret.plato.item.spell.Modifiers;
import org.snowyegret.plato.pick.PickManager;
import org.snowyegret.plato.select.SelectionManager;
import org.snowyegret.plato.undo.UndoManager;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerProperties implements IExtendedEntityProperties {

	public final static String NAME = "PlayerProperties";

	private Modifiers modifiers;
	private SelectionManager selectionManager;
	private PickManager pickManager;
	private UndoManager undoManager;
	private Clipboard clipboard;
	private ISpell lastSpell;

	public Modifiers getModifiers() {
		return modifiers;
	}

	public UndoManager getUndoManager() {
		return undoManager;
	}

	public SelectionManager getSelectionManager() {
		return selectionManager;
	}

	public PickManager getPickManager() {
		return pickManager;
	}

	@Override
	public void saveNBTData(NBTTagCompound tag) {
	}

	@Override
	public void loadNBTData(NBTTagCompound tag) {
	}

	@Override
	public void init(Entity entity, World world) {
		modifiers = new Modifiers();
		selectionManager = new SelectionManager(Plato.blockSelected);
		pickManager = new PickManager(Plato.blockPicked);
		undoManager = new UndoManager();
		clipboard = new Clipboard();
	}

	public void setLastSpell(ISpell spell) {
		this.lastSpell = spell;
	}

	public ISpell getLastSpell() {
		return lastSpell;
	}

	public Clipboard getClipboard() {
		return clipboard;
	}

}
