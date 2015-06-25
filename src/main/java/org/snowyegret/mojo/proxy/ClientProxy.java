package org.snowyegret.mojo.proxy;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import org.snowyegret.mojo.event.EventHandler;
import org.snowyegret.mojo.event.KeyHandler;
import org.snowyegret.mojo.event.MouseHandler;
import org.snowyegret.mojo.item.ModelResourceLocations;
import org.snowyegret.mojo.item.staff.Staff;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		MinecraftForge.EVENT_BUS.register(new MouseHandler());
		FMLCommonHandler.instance().bus().register(new KeyHandler());
	}

	// https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe15_item_smartitemmodel/StartupClientOnly.java
	@Override
	public void registerItemModels(List<Item> items) {
		final int META = 0;
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		
		for (Item i : items) {
			ModelResourceLocation l = ModelResourceLocations.get(i.getClass());
			mesher.register(i, META, l);
			System.out.println("Registered model=" + mesher.getModelManager().getModel(l));
		}
	}

}
