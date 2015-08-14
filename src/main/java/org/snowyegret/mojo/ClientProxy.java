package org.snowyegret.mojo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.snowyegret.mojo.block.BlockMaquette;
import org.snowyegret.mojo.block.BlockPicked;
import org.snowyegret.mojo.block.BlockHighlight;
import org.snowyegret.mojo.util.ModelResourceLocations;
import org.snowyegret.mojo.util.StringUtils;

public class ClientProxy extends CommonProxy {


	// https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe15_item_smartitemmodel/StartupClientOnly.java
	@Override
	public void registerItemModels() {
		System.out.println("Registering Item models...");
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		for (Item i : items) {
			ModelResourceLocation mrl = ModelResourceLocations.forClass(i.getClass());
			mesher.register(i, 0, mrl);
			// System.out.println(i.getUnlocalizedName() + " model=" + mesher.getModelManager().getModel(mrl));
		}
	}

	// https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe04_block_smartblockmodel1/StartupClientOnly.java
	// This is currently necessary in order to make your block render properly when it is an item (i.e. in the inventory
	// or in your hand or thrown on the ground).
	// Minecraft knows to look for the item model based on the GameRegistry.registerBlock. However the registration of
	// the model for each item is normally done by RenderItem.registerItems(), and this is not currently aware
	// of any extra items you have created. Hence you have to do it manually. This will probably change in future.
	// It must be done in the init phase, not preinit, and must be done on client only.
	public void registerItemBlockModels() {
		System.out.println("Registering ItemBlock models...");
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		String name = StringUtils.underscoreNameFor(BlockMaquette.class);
		Item item = GameRegistry.findItem(MoJo.MODID, name);
		ModelResourceLocation mrl = new ModelResourceLocation(MoJo.MODID + ":" + name, "inventory");
		mesher.register(item, 0, mrl);

		System.out.println("item=" + item);
		System.out.println("mrl=" + mrl);
		System.out.println("model=" + mesher.getModelManager().getModel(mrl));
		// BlockRendererDispatcher d;
	}

	@Override
	public void setCustomStateMappers() {
		System.out.println("Setting custom state mappers...");
		ModelLoader.setCustomStateMapper(MoJo.blockHighlight, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return ModelResourceLocations.forClass(BlockHighlight.class);
			}
		});
		// ModelLoader.setCustomStateMapper(MoJo.blockPicked, new StateMapperBase() {
		// @Override
		// protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
		// return ModelResourceLocations.forClass(BlockPicked.class);
		// }
		// });
		ModelLoader.setCustomStateMapper(MoJo.blockMaquette, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return ModelResourceLocations.forClass(BlockMaquette.class);
				// Same
				 //return new ModelResourceLocation("mojo:block_maquette#normal");
				// return new ModelResourceLocation("mojo:block_maquette");
			}
		});
	}

}
