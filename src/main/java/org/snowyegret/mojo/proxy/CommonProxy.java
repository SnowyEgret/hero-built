package org.snowyegret.mojo.proxy;

import java.util.List;

import org.snowyegret.mojo.block.PrevStateTileEntity;
import org.snowyegret.mojo.item.staff.Staff;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void registerEventHandlers() {
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(PrevStateTileEntity.class, PrevStateTileEntity.class.getSimpleName()); 
	}

	public void registerItemModels(List<Item> items) {
	}
}
