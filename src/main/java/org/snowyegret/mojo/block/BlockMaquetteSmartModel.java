package org.snowyegret.mojo.block;

import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import org.snowyegret.mojo.select.Selection;

import com.google.common.collect.Maps;

public class BlockMaquetteSmartModel implements ISmartBlockModel, ISmartItemModel {

	// GenerateModel cache
	private Map<String, GeneratedModel> models = Maps.newHashMap();
	private IBakedModel model;

	@Override
	public IBakedModel handleBlockState(IBlockState state) {

		String name = ((IExtendedBlockState) state).getValue(BlockMaquette.PROP_NAME);
		Iterable<Selection> selections = ((IExtendedBlockState) state).getValue(BlockMaquette.PROP_SELECTIONS);
		EnumFacing facing = (EnumFacing) state.getValue(BlockMaquette.PROP_FACING);

		generateModel(name, selections, facing);
		return this;
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack) {

		BlockMaquetteTileEntity te = new BlockMaquetteTileEntity();
		te.readFromNBT(stack.getTagCompound().getCompoundTag("BlockEntityTag"));

		String name = te.getName();
		Iterable<Selection> selections = te.getSelections();
		// EnumFacing facing = te.getFacing();
		EnumFacing facing = EnumFacing.NORTH;

		generateModel(name, selections, facing);
		return this;
	}

	@Override
	public List getFaceQuads(EnumFacing side) {
		return model.getFaceQuads(side);
	}

	@Override
	public List getGeneralQuads() {
		// System.out.println("this=" + this);
		return model.getGeneralQuads();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return model.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return model.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return model.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getTexture() {
		// This method is first called if model is null because handleBlockState is not called
		// Normally should not happen
		if (model == null) {
			System.out.println("Could not get texture. model=" + model);
			System.out.println("Creating default model");
			model = new GeneratedModel();
		}
		return model.getTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return model.getItemCameraTransforms();
	}

	// TODO Model should be oriented in same direction as selections when first placed
	// TODO Models should be deleted from cache when item is deleted or expires
	// TODO Name must be unique when maquette is created or the other model for that name will be retrieved
	private void generateModel(String name, Iterable<Selection> selections, EnumFacing facing) {

		if (selections == null || !selections.iterator().hasNext() || name == null || facing == null) {
			System.out.println("selections=" + selections);
			System.out.println("name=" + name);
			System.out.println("facing=" + facing);
			System.out.println("Returning a model with no quads.");
			model = new GeneratedModel();
			return;
		}

		String n = name + "_" + facing.toString();
		//System.out.println("n=" + n);
		if (!models.containsKey(n)) {
			// if (!models.containsKey(name)) {
			for (EnumFacing f : EnumFacing.HORIZONTALS) {
				models.put(name + "_" + f.toString(), new GeneratedModel(selections, f));
				System.out.println("Cached GeneratedModel. name=" + name + "_" + f.toString());
			}
		}
		// Seems we can lookup a null string. Model will just have no quads
		model = models.get(n);
		// System.out.println("model=" + model);
	}
}
