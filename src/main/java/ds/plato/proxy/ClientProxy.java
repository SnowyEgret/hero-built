package ds.plato.proxy;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import ds.plato.Plato;
import ds.plato.event.ForgeEventHandler;
import ds.plato.event.KeyHandler;
import ds.plato.event.MouseHandler;
import ds.plato.gui.Overlay;
import ds.plato.item.spell.Spell;
import ds.plato.item.spell.SpellRenderer;
import ds.plato.item.staff.Staff;
import ds.plato.item.staff.StaffRenderer;
import ds.plato.pick.IPick;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

public class ClientProxy extends CommonProxy {

	@Deprecated
	@Override
	public void setCustomRenderers(ISelect select, IPick pick, Iterable<Staff> staffs, Iterable<Spell> spells) {
		for (Staff s : staffs) {
			if (s.getModelResourceLocation() == null) {
				System.out.println("Missing model for class=" + s.getClass());
			} else {
				MinecraftForgeClient.registerItemRenderer(s, new StaffRenderer(s));
			}
		}
		for (Spell s : spells) {
			if (s.getModelResourceLocation() == null) {
				System.out.println("Missing model for class=" + s.getClass());
			} else {
				MinecraftForgeClient.registerItemRenderer(s, new SpellRenderer(s));
			}
		}
	}

	@Override
	public void registerEventHandlers(Plato plato, ISelect select, IUndo undo, IPick pick) {
		Overlay overlay = new Overlay(select);
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler(select, pick, overlay));
		MinecraftForge.EVENT_BUS.register(new MouseHandler(undo, select, pick));
		FMLCommonHandler.instance().bus().register(new KeyHandler(undo, select, pick));
	}
}
