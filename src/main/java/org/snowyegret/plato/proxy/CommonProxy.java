package org.snowyegret.plato.proxy;

import org.snowyegret.plato.block.PrevStateTileEntity;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void registerEventHandlers() {
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(PrevStateTileEntity.class, PrevStateTileEntity.class.getSimpleName()); 
	}
}
