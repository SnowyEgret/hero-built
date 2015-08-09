package org.snowyegret.mojo.block;

import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import org.snowyegret.mojo.select.Selection;

import com.google.common.collect.Maps;

public class BlockMaquetteSmartModel implements ISmartBlockModel {

	// Model cache
	private Map<String, IBakedModel> models = Maps.newHashMap();
	private IBakedModel model;

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		// NBTTagCompound tag = ((IExtendedBlockState) state).getValue(BlockMaquette.PROPERTY_TAG);
		Iterable<Selection> selections = ((IExtendedBlockState) state).getValue(BlockMaquette.PROPERTY_SELECTIONS);
		String name = ((IExtendedBlockState) state).getValue(BlockMaquette.PROPERTY_NAME);
		// TODO Name must be unique
		if (selections == null) {
			System.out.println("Could not get selections from extended block state. selections=" + selections);
			return new GeneratedModel();
		}
		if (name == null) {
			System.out.println("Could not get name from extended block state. name=" + name);
			return new GeneratedModel();
		}
		if (!models.containsKey(name)) {
			IBakedModel m = models.put(name, new GeneratedModel(selections));
			System.out.println("Created new GeneratedModel");
		}
		// Seems we can lookup a null string. Model will just have no quads
		model = models.get(name);
		System.out.println("model=" + model);
		return model;
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
}
