package ds.plato.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import ds.plato.item.staff.InventoryStaff;

public class GuiHandler implements IGuiHandler {
	
	public static final int GUI_DIALOG = -1;
	public static final int GUI_TEXT_INPUT_DIALOG = 0;
	public static final int GUI_SPELL_TEXT = 2;
	public static final int GUI_STAFF = 3;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World w, int x, int y, int z) {
		switch (id) {
		case GUI_STAFF:
			//int i = player.inventory.currentItem;
			///NBTTagCompound tag = player.inventory.getCurrentItem().getTagCompound();
			return new GuiStaffContainer(player.inventory, new InventoryStaff(player.inventory,
					player.inventory.currentItem));
		default:
			throw new IllegalArgumentException("GUI id " + id + " is undefined");
		}
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch (id) {
		case GUI_DIALOG:
			return new GuiDialog(player, "Ok", "Cancel");
		case GUI_TEXT_INPUT_DIALOG:
			return new GuiTextInputDialog(player);
		case GUI_SPELL_TEXT:
			return new GuiSpellText(player);
		case GUI_STAFF:
			return new GuiStaff(new GuiStaffContainer(player.inventory, new InventoryStaff(player.inventory,
					player.inventory.currentItem)));
		default:
			throw new IllegalArgumentException("GUI id " + id + " is undefined");
		}
	}
}
