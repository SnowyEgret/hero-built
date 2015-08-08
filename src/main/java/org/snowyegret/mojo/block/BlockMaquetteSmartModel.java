package org.snowyegret.mojo.block;

import java.util.List;
import java.util.Map;

import org.snowyegret.mojo.item.spell.other.SpellMaquette;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import com.google.common.collect.Maps;

public class BlockMaquetteSmartModel implements ISmartBlockModel {

	// Model cache
	private Map<String, IBakedModel> models = Maps.newHashMap();
	private IBakedModel model;

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		NBTTagCompound tag = ((IExtendedBlockState) state).getValue(BlockMaquette.PROPERTY_TAG);
		// TODO Name must be unique
		if (tag == null) {
			System.out.println("Could not get tag from extended block state. tag=" + tag);
			return new GeneratedModel();
		}
		String name = tag.getString(SpellMaquette.KEY_NAME);
		if (!models.containsKey(name)) {
			System.out.println("Creating model for path=" + name);
			models.put(name, new GeneratedModel(tag));
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
