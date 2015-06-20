package org.snowyegret.mojo.proxy;

import org.snowyegret.mojo.block.PrevStateTileEntity;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void registerEventHandlers() {
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(PrevStateTileEntity.class, PrevStateTileEntity.class.getSimpleName()); 
	}
}
