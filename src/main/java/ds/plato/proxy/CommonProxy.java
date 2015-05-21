package ds.plato.proxy;

import java.util.List;

import ds.plato.Plato;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.Spell;
import ds.plato.item.staff.IStaff;
import ds.plato.item.staff.Staff;
import ds.plato.pick.IPick;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

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
