package ds.plato.proxy;

import java.util.List;

import ds.plato.Plato;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.ISpell;
import ds.plato.api.IStaff;
import ds.plato.api.IUndo;
import ds.plato.item.spell.Spell;
import ds.plato.item.staff.Staff;

public class CommonProxy {

	public void setCustomRenderers(ISelect selectionManager, IPick pickManager, Iterable<Staff> staffs, Iterable<Spell> spells) {
	}

	public void registerEventHandlers(Plato plato, ISelect select, IUndo undo, IPick pick) {
	}

	public void setCustomRenderers(ISelect selectionManager, IPick pickManager, List<IStaff> staffs, List<ISpell> spells) {
		// TODO Auto-generated method stub
		
	}

	// @Override
	// public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
	// switch (ID) {
	// case 0:
	// return null;
	// case 3:
	// //From http://www.minecraftforge.net/wiki/Containers_and_GUIs:
	// //Note that the client returns a Gui while the server returns a Container
	// return new GuiStaffContainer(player.inventory, (IInventory) player.getHeldItem().getItem());
	// default:
	// return null;
	// }
	// }
	//
	// @Override
	// public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
	// return null;
	// }

}
