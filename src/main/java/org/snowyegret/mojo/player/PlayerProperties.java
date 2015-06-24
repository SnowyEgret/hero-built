package org.snowyegret.mojo.player;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.item.spell.ISpell;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.PickManager;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.undo.TransactionManager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerProperties implements IExtendedEntityProperties {

	public final static String NAME = "PlayerProperties";

	private Modifiers modifiers;
	private SelectionManager selectionManager;
	private PickManager pickManager;
	private TransactionManager transactionManager;
	private Clipboard clipboard;
	private ISpell lastSpell;

	private IPlayer player;

	public PlayerProperties(EntityPlayer entity) {
		player = Player.instance(entity);

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
	}

	@Override
	public void loadNBTData(NBTTagCompound tag) {
	}

	@Override
	public void init(Entity entity, World world) {
		modifiers = new Modifiers();
		selectionManager = new SelectionManager(player, MoJo.blockSelected);
		pickManager = new PickManager(player, MoJo.blockPicked);
		transactionManager = new TransactionManager(player);
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
