package org.snowyegret.mojo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import org.snowyegret.mojo.block.BlockPicked;
import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.event.EventHandler;
import org.snowyegret.mojo.event.KeyHandler;
import org.snowyegret.mojo.event.MouseHandler;
import org.snowyegret.mojo.gui.GuiHandler;
import org.snowyegret.mojo.util.ModelResourceLocations;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		MinecraftForge.EVENT_BUS.register(new MouseHandler());
		FMLCommonHandler.instance().bus().register(new KeyHandler());
	}

	// https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe15_item_smartitemmodel/StartupClientOnly.java
	@Override
	public void registerItemModels() {

		final int META = 0;
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		for (Item i : items) {
			ModelResourceLocation l = ModelResourceLocations.get(i.getClass());
			mesher.register(i, META, l);
			System.out.println(i.getUnlocalizedName() + " model=" + mesher.getModelManager().getModel(l));
		}
	}

	@Override
	public void setCustomStateMappers() {
		// Create custom state mappers for BlockSelected and BlockPicked models
		ModelLoader.setCustomStateMapper(MoJo.blockSelected, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return ModelResourceLocations.get(BlockSelected.class);
			}
		});
		ModelLoader.setCustomStateMapper(MoJo.blockPicked, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return ModelResourceLocations.get(BlockPicked.class);
			}
		});
	}

	@Override
	public void registerGuiHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(MoJo.instance, new GuiHandler());
	}
}
