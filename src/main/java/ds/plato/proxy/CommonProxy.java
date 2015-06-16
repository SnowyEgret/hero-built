package ds.plato.proxy;

import net.minecraftforge.fml.common.registry.GameRegistry;
import ds.plato.block.PrevStateTileEntity;

public class CommonProxy {

	public void registerEventHandlers() {
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(PrevStateTileEntity.class, PrevStateTileEntity.class.getSimpleName()); 
	}
}
