package ds.plato.player;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import ds.plato.Plato;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.Modifiers;
import ds.plato.pick.PickManager;
import ds.plato.select.SelectionManager;
import ds.plato.undo.UndoManager;

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
	public void saveNBTData(NBTTagCompound compound) {
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
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
