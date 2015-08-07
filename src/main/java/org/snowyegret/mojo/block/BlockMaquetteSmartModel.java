package org.snowyegret.mojo.block;

import java.util.List;
import java.util.Map;

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
		String path = ((IExtendedBlockState) state).getValue(BlockMaquette.PROPERTY_PATH);
		NBTTagCompound tag = ((IExtendedBlockState) state).getValue(BlockMaquette.PROPERTY_TAG);
		if (!models.containsKey(path)) {
			System.out.println("Creating model for path=" + path);
			// models.put(path, new GeneratedModel(path));
			models.put(path, new GeneratedModel(tag));
		}
		// Seems we can lookup a null string. Model will just have no quads
		model = models.get(path);
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
		return model.getTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return model.getItemCameraTransforms();
	}
}
